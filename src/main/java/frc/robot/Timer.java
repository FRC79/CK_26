package frc.robot;

import java.time.Instant;

public class Timer {
    private Instant startTime;
    private int period_ms;

    public Timer(int period_ms) {
        this.startTime = Instant.now();
        this.period_ms = period_ms;
    }

    public boolean isReady() {
        Instant now = Instant.now();
        return (now.toEpochMilli() - startTime.toEpochMilli()) >= period_ms;
    }

    public void clear() {
        this.startTime = Instant.now();
    }

    public float TimeElasped(){
        Instant now = Instant.now();
        return (now.toEpochMilli() - startTime.toEpochMilli());
    }
}
