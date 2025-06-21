/*public class stuff {
}
// IOCallback interface
interface IOCallback {
    void onIOReady(SelectableChannel channel) throws IOException;
}

// TimerCallback interface
interface TimerCallback {
    void onTimeout();
}

// TimerTask for scheduling time-based callbacks
class TimerTask implements Comparable<TimerTask> {
    long executeAtMillis;
    TimerCallback callback;

    public TimerTask(long delayMillis, TimerCallback callback) {
        this.executeAtMillis = System.currentTimeMillis() + delayMillis;
        this.callback = callback;
    }

    @Override
    public int compareTo(TimerTask other) {
        return Long.compare(this.executeAtMillis, other.executeAtMillis);
    }

    public boolean isReady() {
        return System.currentTimeMillis() >= executeAtMillis;
    }

    public void execute() {
        callback.onTimeout();
    }
}

// EventLoop class
class EventLoop {
    private final PriorityQueue<TimerTask> timerQueue = new PriorityQueue<>();
    private final Map<SelectableChannel, IOCallback> ioCallbacks = new HashMap<>();
    private final Selector selector;

    public EventLoop() throws IOException {
        this.selector = Selector.open();
    }

    public void registerTimer(long delayMillis, TimerCallback callback) {
        timerQueue.add(new TimerTask(delayMillis, callback));
    }

    public void registerIO(SelectableChannel channel, int ops, IOCallback callback) throws IOException {
        channel.configureBlocking(false);
        channel.register(selector, ops);
        ioCallbacks.put(channel, callback);
    }

    public void run() {
        while (true) {
            processTimers();

            try {
                selector.select(100);
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isReadable() || key.isWritable() || key.isAcceptable() || key.isConnectable()) {
                        IOCallback callback = ioCallbacks.get(key.channel());
                        if (callback != null) {
                            callback.onIOReady(key.channel());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processTimers() {
        while (!timerQueue.isEmpty() && timerQueue.peek().isReady()) {
            TimerTask task = timerQueue.poll();
            task.execute();
        }
    }
}

// Existing FIX engine components

// ... (Retain your existing FIXTags, FIXMessage, MessageStore, and FIXMessageProcessor here)

// Entry point for FIXEngine
public class FIXEngineLauncher {

    public static void main(String[] args) throws IOException {

        EventLoop eventLoop = new EventLoop();

        MessageStore store = new MessageStore();

        // Replace with actual client/server logic based on args
        // For demo, just setting up a dummy channel
        // You should replace this with a working SocketChannel or ServerSocketChannel configuration
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
*/