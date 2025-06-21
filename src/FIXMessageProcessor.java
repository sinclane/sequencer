import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

/**
 * Processes incoming FIX messages from a SocketChannel.
 */
class FIXMessageProcessor implements IOCallback {

    private final SocketChannel channel;
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(4096); // direct buffer to reduce GC
    private final MessageStore store;
    private int expectedSeqNum = 1;

    public FIXMessageProcessor(SocketChannel channel, MessageStore store) {

        this.channel = channel;

        this.store = store;

    }

    @Override
    public void onIOReady(SelectableChannel ch) throws IOException {
        buffer.clear();
        int read = channel.read(buffer);
        if (read == -1) {
            System.out.println("Connection closed by peer");
            channel.close();
            return;
        }

        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        FIXMessage msg = new FIXMessage(bytes);
        store.add(msg);

        String msgType = msg.getValue(FIXTags.MsgType);
        String seqNumStr = msg.getValue(FIXTags.MsgSeqNum);
        int seqNum = (seqNumStr != null) ? Integer.parseInt(seqNumStr) : -1;

        if (seqNum != expectedSeqNum) {
            System.out.printf("Sequence number mismatch: expected %d but got %d%n", expectedSeqNum, seqNum);
            // TODO: initiate resend request
        }
        expectedSeqNum = seqNum + 1;

        handleMessage(msg, msgType);
    }

    private void handleMessage(FIXMessage msg, String msgType) {
        switch (msgType) {
            case FIXTags.HEARTBEAT:
                System.out.println("Heartbeat received");
                break;
            case FIXTags.LOGON:
                System.out.println("Logon received");
                break;
            case FIXTags.LOGOUT:
                System.out.println("Logout received");
                break;
            case FIXTags.NEW_ORDER_SINGLE:
                handleNewOrder(msg);
                break;
            case FIXTags.ORDER_CANCEL_REQUEST:
                handleCancelRequest(msg);
                break;
            case FIXTags.TEST_REQUEST:
                System.out.println("Test Request received");
                break;
            case FIXTags.RESEND_REQUEST:
                System.out.println("Resend Request received");
                break;
            case FIXTags.REJECT:
                System.out.println("Reject received");
                break;
            case FIXTags.SEQUENCE_RESET:
                System.out.println("Sequence Reset received");
                break;
            default:
                System.out.println("Unhandled MsgType: " + msgType);
        }
    }

    private void handleNewOrder(FIXMessage msg) {
        String clOrdId = msg.getValue(FIXTags.ClOrdID);
        String symbol = msg.getValue(FIXTags.Symbol);
        String qty = msg.getValue(FIXTags.OrderQty);
        String price = msg.getValue(FIXTags.Price);
        System.out.printf("NewOrderSingle: ClOrdID=%s Symbol=%s Qty=%s Price=%s%n", clOrdId, symbol, qty, price);
    }

    private void handleCancelRequest(FIXMessage msg) {
        String origClOrdId = msg.getValue(FIXTags.OrigClOrdID);
        String clOrdId = msg.getValue(FIXTags.ClOrdID);
        System.out.printf("CancelRequest: OrigClOrdID=%s ClOrdID=%s%n", origClOrdId, clOrdId);
    }
}
