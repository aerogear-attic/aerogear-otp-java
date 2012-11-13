package org.abstractj.cuckootp;

public class Clock {

    private int interval = 30;

    public Clock(){}

    public Clock(int interval) {
        this.interval = interval;
    }

    public long getCurrentInterval() {
        System.out.println("My millis: " + System.currentTimeMillis());
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        return currentTimeSeconds / interval;
    }
}
