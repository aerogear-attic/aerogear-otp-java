package org.abstractj.cuckootp.api;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Clock {

    private int interval = 30;

    public Clock(){}

    public Clock(int interval) {
        this.interval = interval;
    }

    public long getCurrentInterval() {
        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        long currentTimeSeconds = calendar.getTimeInMillis() / 1000;
        return currentTimeSeconds / interval;
    }
}
