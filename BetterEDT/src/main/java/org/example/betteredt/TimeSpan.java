package org.example.betteredt;
import java.time.LocalTime;

public class TimeSpan {
    private LocalTime start;
    private LocalTime end;

    public TimeSpan(LocalTime start, LocalTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.start = start;
        this.end = end;
    }

    public boolean contains(LocalTime time) {
        return !time.isBefore(start) && time.isBefore(end);
    }


}
