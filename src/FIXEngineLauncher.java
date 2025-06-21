
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;

public class FIXEngineLauncher {

    public static void main(String[] args) throws IOException {

        EventLoop eventLoop = new EventLoop();

        MessageStore store = new MessageStore();

        // Replace with actual client/server logic based on args
        SocketChannel dummyChannel = SocketChannel.open();

        dummyChannel.configureBlocking(false);

        dummyChannel.connect(new java.net.InetSocketAddress("localhost", 9876));

        eventLoop.registerIO(dummyChannel, SelectionKey.OP_CONNECT, channel -> {

            if (dummyChannel.finishConnect()) {

                System.out.println("Connected to server.");

                eventLoop.registerIO(dummyChannel, SelectionKey.OP_READ, new FIXMessageProcessor(dummyChannel, store));

            }
        });

        eventLoop.run();
    }
}