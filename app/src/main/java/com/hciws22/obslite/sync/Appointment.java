package com.hciws22.obslite.sync;

import java.time.LocalDateTime;
import java.util.Optional;

public class Appointment {

    private LocalDateTime startAt;
    private LocalDateTime endAt;
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

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {

        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public String getLocation() {
        return Optional.ofNullable(location).orElse("");
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
