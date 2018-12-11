package jslEngine;

public class jslTimer {

    // Animation duration in millis
    private long duration;
    private long timer;

    private boolean isRunning = false;

    public jslTimer(float duration) {
        setDuration(duration);
        reset();
    }

    public boolean update() {
        if(!isRunning) {
            return false;
        }

        if(System.currentTimeMillis() >= timer) {
            reset();
            return true;
        }

        return false;
    }

    public void restart() {
        isRunning = false;
        start();
    }

    public void reset() {
        timer = System.currentTimeMillis() + duration;
    }

    public void stop() {
        isRunning = false;
    }

    public void start() {
        if(!isRunning) {
            reset();
            isRunning = true;
        }
    }

    public float getProgress() {
        long start = timer - duration;
        long diff = System.currentTimeMillis() - start;

        return (float)diff / duration;
    }

    public void setDuration(float duration) {
        this.duration = (long)(duration * 1000.0f);
    }

    public boolean isRunning() { return isRunning; }
    public float getDuration() { return duration * 0.001f; }
    public long getTimer() { return timer; }
}
