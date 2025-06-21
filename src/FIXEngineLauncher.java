
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;

public class FIXEngineLauncher {

    public static void main(String[] args) throws IOException {

        Config conf  = new Config("c:\\Users\\neils\\IdeaProjects\\Sequencer2\\src\\sequencer.cfg");

        ByteBuffer para = ByteBuffer.allocate(24);

        conf.putInByteBuffer("Server.host",para);

        String hostname = conf.getAsString("Server.hostname");

        int port = conf.getAsInt("Server.port",0);

        EventLoop eventLoop = new EventLoop();

        MessageStore store = new MessageStore();

        // Replace with actual client/server logic based on args
        SocketChannel dummyChannel = SocketChannel.open();

        dummyChannel.configureBlocking(false);

        dummyChannel.connect(new java.net.InetSocketAddress(hostname, port));

        eventLoop.registerIO(dummyChannel, SelectionKey.OP_CONNECT, channel -> {

            if (dummyChannel.finishConnect()) {

                System.out.println("Connected to server.");

                eventLoop.registerIO(dummyChannel, SelectionKey.OP_READ, new FIXMessageProcessor(dummyChannel, store));

            }
        });

        eventLoop.run();
    }
}