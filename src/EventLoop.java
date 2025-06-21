import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.channels.*;
import java.util.*;

// EventLoop class (minimized GC)
class EventLoop {
    private final PriorityQueue<TimerTask> timerQueue = new PriorityQueue<>();
    private final Map<SelectableChannel, IOCallback> ioCallbacks = new IdentityHashMap<>();
    private final Selector selector;
    private final TimerTaskPool timerTaskPool = new TimerTaskPool(128);

    public EventLoop() throws IOException {
        this.selector = Selector.open();
    }

    public void registerTimer(long delayMillis, TimerCallback callback) {
        TimerTask task = timerTaskPool.get(delayMillis, callback);
        timerQueue.add(task);
    }

    public void registerIO(SelectableChannel channel, int ops, IOCallback callback) throws IOException {
        channel.configureBlocking(false);
        channel.register(selector, ops);
        ioCallbacks.put(channel, callback);
    }

    public void run() {
        final Set<SelectionKey> keySet = selector.selectedKeys();

        while (true) {
            processTimers();

            try {
                selector.select(100);
                for (SelectionKey key : keySet) {
                    if (key.isReadable() || key.isWritable() || key.isAcceptable() || key.isConnectable()) {
                        IOCallback callback = ioCallbacks.get(key.channel());
                        if (callback != null) {
                            callback.onIOReady(key.channel());
                        }
                    }
                }
                keySet.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processTimers() {
        while (!timerQueue.isEmpty() && timerQueue.peek().isReady()) {
            TimerTask task = timerQueue.poll();
            task.execute();
            timerTaskPool.release(task);
        }
    }
}