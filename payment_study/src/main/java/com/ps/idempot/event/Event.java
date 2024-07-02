package com.ps.idempot.event;

public abstract class Event {
    private final Long timestamp;

    protected Event() {
        this.timestamp = System.currentTimeMillis();
    }
}
