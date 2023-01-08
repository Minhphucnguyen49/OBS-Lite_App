package com.hciws22.obslite.entities;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public class SyncEntity {

    private  Integer id;

    private  String obsLink ;

    private  LocalDateTime localDateTime;

    public SyncEntity() {

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setObsLink(String obsLink) {
        this.obsLink = obsLink;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Integer getId() {
        return Optional.ofNullable(id).orElseThrow(NoSuchFieldError::new);
    }

    public Optional<LocalDateTime> getLocalDateTime() {
        return Optional.ofNullable(localDateTime);
    }

    public String getObsLink() {
        return Optional.ofNullable(obsLink).orElse("");
    }

}
