import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FixEngineTests {

    private MessageStore store;
    private FIXMessageProcessor processor;
    private Pipe pipe;

    @BeforeEach
    public void setup() throws IOException {
        store = new MessageStore();
        pipe = Pipe.open();
        pipe.sink().configureBlocking(false);
        pipe.source().configureBlocking(false);
        //processor = new FIXMessageProcessor(pipe.source(), store);
    }

    private void sendToProcessor(String fixMessage) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(fixMessage.getBytes());
        pipe.sink().write(buffer);
        processor.onIOReady(pipe.source());
    }

    @Test
    public void testLogonMessageParsedCorrectly() throws IOException {
        String fixLogon = "8=FIX.4.2\u00019=76\u000135=A\u000134=1\u000149=SENDER\u000156=TARGET\u000198=0\u0001108=30\u000152=20240621-12:00:00\u000110=234\u0001";
        sendToProcessor(fixLogon);

        List<FIXMessage> messages = store.getMessages();
        assertEquals(1, messages.size());
        FIXMessage msg = messages.get(0);
        assertEquals("A", msg.getValue(FIXTags.MsgType));
        assertEquals("1", msg.getValue(FIXTags.MsgSeqNum));
        assertEquals("SENDER", msg.getValue(FIXTags.SenderCompID));
    }

    @Test
    public void testNewOrderSingleParsedCorrectly() throws IOException {
        String fixOrder = "8=FIX.4.2\u00019=112\u000135=D\u000134=2\u000149=BUYER\u000156=SELLER\u000111=ORDER123\u000155=AAPL\u000138=100\u000144=150.25\u000140=2\u000160=20240621-12:01:00\u000110=220\u0001";
        sendToProcessor(fixOrder);

        FIXMessage msg = store.getMessages().get(0);
        assertEquals("D", msg.getValue(FIXTags.MsgType));
        assertEquals("ORDER123", msg.getValue(FIXTags.ClOrdID));
        assertEquals("AAPL", msg.getValue(FIXTags.Symbol));
        assertEquals("100", msg.getValue(FIXTags.OrderQty));
        assertEquals("150.25", msg.getValue(FIXTags.Price));
    }

    @Test
    public void testCancelRequestParsedCorrectly() throws IOException {
        String fixCancel = "8=FIX.4.2\u00019=88\u000135=F\u000134=3\u000149=BUYER\u000156=SELLER\u000141=ORDER123\u000111=ORDER124\u000155=AAPL\u000154=1\u000160=20240621-12:02:00\u000110=145\u0001";
        sendToProcessor(fixCancel);

        FIXMessage msg = store.getMessages().get(0);
        assertEquals("F", msg.getValue(FIXTags.MsgType));
        assertEquals("ORDER123", msg.getValue(FIXTags.OrigClOrdID));
        assertEquals("ORDER124", msg.getValue(FIXTags.ClOrdID));
    }

    @Test
    public void testSequenceNumberMismatch() throws IOException {
        String fix1 = "8=FIX.4.2\u00019=50\u000135=0\u000134=1\u000149=CLIENT\u000156=SERVER\u000152=20240621-12:03:00\u000110=099\u0001";
        String fix2 = "8=FIX.4.2\u00019=50\u000135=0\u000134=3\u000149=CLIENT\u000156=SERVER\u000152=20240621-12:03:01\u000110=100\u0001";

        sendToProcessor(fix1);
        sendToProcessor(fix2); // Skips seq 2, should trigger mismatch warning

        assertEquals(2, store.getMessages().size());
    }

    @Test
    public void testTimerCallbackFires() {
        final boolean[] triggered = {false};
        TimerCallback callback = () -> triggered[0] = true;

        TimerTaskPool pool = new TimerTaskPool(10);
        TimerTask task = pool.get(0, callback);
        assertTrue(task.isReady());
        task.execute();
        assertTrue(triggered[0]);
    }
}
