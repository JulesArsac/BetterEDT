package org.example.betteredt;
import java.time.LocalTime;
import java.time.Duration;


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
        return time.isAfter(start) && time.isBefore(end);
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
