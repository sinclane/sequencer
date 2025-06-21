
class TimerTask implements Comparable<TimerTask> {
    long executeAtMillis;
    TimerCallback callback;
    boolean inUse = false;

    public void init(long delayMillis, TimerCallback callback) {
        this.executeAtMillis = System.currentTimeMillis() + delayMillis;
        this.callback = callback;
        this.inUse = true;
    }

    public void reset() {
        this.callback = null;
        this.inUse = false;
    }

    @Override
    public int compareTo(TimerTask other) {
        return Long.compare(this.executeAtMillis, other.executeAtMillis);
    }

    public boolean isReady() {
        return System.currentTimeMillis() >= executeAtMillis;
    }

    public void execute() {
        if (callback != null) {
            callback.onTimeout();
        }
    }
}