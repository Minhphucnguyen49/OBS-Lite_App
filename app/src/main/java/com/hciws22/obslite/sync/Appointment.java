package com.hciws22.obslite.sync;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

public class Appointment {

    private ZonedDateTime startAt;
    private ZonedDateTime endAt;
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

    public ZonedDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(ZonedDateTime startAt) {
        this.startAt = startAt;
    }

    public ZonedDateTime getEndAt() {

        return endAt;
    }

    public void setEndAt(ZonedDateTime endAt) {
        this.endAt = endAt;
    }

    public String getLocation() {
        return Optional.ofNullable(location).orElse("");
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
