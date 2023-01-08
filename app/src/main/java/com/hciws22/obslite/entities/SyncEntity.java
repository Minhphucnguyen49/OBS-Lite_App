package com.hciws22.obslite.entities;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public class SyncEntity {

    private final Integer id;

    private final String obsLink ;

    private final LocalDateTime localDateTime;

    public SyncEntity(Integer id, String obsLink, LocalDateTime localDateTime) {
        this.id = id;
        this.obsLink = obsLink;
        this.localDateTime = localDateTime;
    }

    public Integer getId() {
        return Optional.ofNullable(id).orElseThrow(NoSuchFieldError::new);
    }

    public LocalDateTime getLocalDateTime() {
        return Optional.ofNullable(localDateTime).orElseThrow(NullPointerException::new);
    }

    public String getObsLink() {
        return Optional.ofNullable(obsLink).orElseThrow(NullPointerException::new);
    }

}
