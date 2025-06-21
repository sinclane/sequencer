
public class TimerTaskPool {

    private final TimerTask[] pool;

    private int index = 0;

    public TimerTaskPool(int capacity) {

        pool = new TimerTask[capacity];

        for (int i = 0; i < capacity; i++) {

            pool[i] = new TimerTask();

        }
    }

    public TimerTask get(long delayMillis, TimerCallback callback) {

        for (int i = 0; i < pool.length; i++) {

            TimerTask task = pool[i];

            if (!task.inUse) {

                task.init(delayMillis, callback);

                return task;
            }
        }
        throw new RuntimeException("No available TimerTask in pool");
    }

    public void release(TimerTask task) {

        task.reset();
    }
}