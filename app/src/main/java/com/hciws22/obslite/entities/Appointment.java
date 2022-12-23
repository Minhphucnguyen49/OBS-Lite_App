package com.hciws22.obslite.entities;

import java.time.LocalDateTime;

public class Appointment {

    private LocalDateTime from;
    private LocalDateTime to;
    private String location;

    public Appointment() {

    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
