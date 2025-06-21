import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class FIXEngine {

    enum Mode { SERVER, CLIENT }

    private final Mode mode;
    private final String host;
    private final int port;
    private final int heartbeatIntervalSeconds;

    private final EventLoop eventLoop;
    private final Map<SocketChannel, Long> lastReceivedMap = new HashMap<>();

    private static final String SOH = "\u0001";

    public FIXEngine(Mode mode, String host, int port, int heartbeatIntervalSeconds) throws IOException {
        this.mode = mode;
        this.host = host;
        this.port = port;
        this.heartbeatIntervalSeconds = heartbeatIntervalSeconds;
        this.eventLoop = new EventLoop();
    }

    public void start() throws IOException {
        if (mode == Mode.SERVER) {
            startServer();
        } else {
            startClient();
        }

        // Register recurring heartbeat check
        eventLoop.registerTimer(1000, new HeartbeatTimer());
        eventLoop.run();
    }

    private void startServer() throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);

        eventLoop.registerIO(serverSocket, SelectionKey.OP_ACCEPT, channel -> {
            ServerSocketChannel server = (ServerSocketChannel) channel;
            SocketChannel client = server.accept();
            if (client != null) {
                System.out.println("Accepted client: " + client.getRemoteAddress());
                eventLoop.registerIO(client, SelectionKey.OP_READ, new FIXMessageHandler(client));
                lastReceivedMap.put(client, System.currentTimeMillis());
            }
        });
    }

    private void startClient() throws IOException {
        SocketChannel client = SocketChannel.open();
        client.configureBlocking(false);
        client.connect(new InetSocketAddress(host, port));

        eventLoop.registerIO(client, SelectionKey.OP_CONNECT, channel -> {
            SocketChannel sc = (SocketChannel) channel;
            if (sc.finishConnect()) {
                System.out.println("Connected to server.");
                eventLoop.registerIO(sc, SelectionKey.OP_READ, new FIXMessageHandler(sc));
                lastReceivedMap.put(sc, System.currentTimeMillis());
                sendLogon(sc);
            }
        });
    }

    private void sendLogon(SocketChannel channel) throws IOException {
        String logon = buildFIXMessage("A", "98=0", "108=" + heartbeatIntervalSeconds);
        sendMessage(channel, logon);
    }

    private void sendHeartbeat(SocketChannel channel) throws IOException {
        String heartbeat = buildFIXMessage("0");
        sendMessage(channel, heartbeat);
    }

    private void sendMessage(SocketChannel channel, String msg) throws IOException {

        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());

        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    private String buildFIXMessage(String msgType, String... bodyFields) {
        List<String> fields = new ArrayList<>();
        fields.add("8=FIX.5.0");
        fields.add("35=" + msgType);
        fields.addAll(Arrays.asList(bodyFields));
        String body = String.join(SOH, fields);
        return body + SOH;
    }

    private class HeartbeatTimer implements TimerCallback {
        @Override
        public void onTimeout() {
            long now = System.currentTimeMillis();
            for (SocketChannel channel : lastReceivedMap.keySet()) {
                try {
                    long lastReceived = lastReceivedMap.getOrDefault(channel, 0L);
                    if (now - lastReceived > heartbeatIntervalSeconds * 1000) {
                        System.out.println("Sending heartbeat to " + channel.getRemoteAddress());
                        sendHeartbeat(channel);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Re-register timer
            eventLoop.registerTimer(1000, this);
        }
    }

    private class FIXMessageHandler implements IOCallback {
        private final SocketChannel channel;
        private final ByteBuffer buffer = ByteBuffer.allocate(1024);

        FIXMessageHandler(SocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void onIOReady(SelectableChannel ch) throws IOException {
            buffer.clear();
            int read = channel.read(buffer);
            if (read == -1) {
                System.out.println("Connection closed by: " + channel.getRemoteAddress());
                channel.close();
                return;
            }

            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            String msg = new String(data);
            System.out.println("Received: " + msg);
            lastReceivedMap.put(channel, System.currentTimeMillis());
        }
    }

    public static void main(String[] args) throws IOException {
        // Example: Client mode
        // new FIXEngine(Mode.CLIENT, "127.0.0.1", 9876, 30).start();

        // Example: Server mode
        new FIXEngine(Mode.SERVER, null, 9876, 30).start();
    }
}
