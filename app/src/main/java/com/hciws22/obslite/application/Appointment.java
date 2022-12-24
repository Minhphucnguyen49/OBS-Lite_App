package com.hciws22.obslite.application;

import java.time.LocalDateTime;
import java.util.Optional;

public class Appointment {

    private LocalDateTime from;
    private LocalDateTime to;
    private String location;
    private String type;
    private String nr;

    public Appointment() {

    }

    public String getType() {
        return Optional.ofNullable(type).orElse("");
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNr() {

        return Optional.ofNullable(nr).orElse("");
    }

    public void setNr(String nr) {
        this.nr = nr;
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
        return Optional.ofNullable(location).orElse("");
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
