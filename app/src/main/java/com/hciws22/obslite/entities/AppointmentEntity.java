package com.hciws22.obslite.entities;

import com.hciws22.obslite.sync.Appointment;

import org.jetbrains.annotations.NotNull;
import java.time.ZonedDateTime;
import java.util.Optional;

public class AppointmentEntity {

    private String id;
    private ZonedDateTime startAt;
    private ZonedDateTime endAt;
    private String location;
    private String type;
    private String nr;

    @NotNull
    private String moduleID;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNr() {
        return Optional.ofNullable(nr).orElse("");
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(@NotNull String moduleID) {
        this.moduleID = moduleID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return Optional.ofNullable(type).orElse("");
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


    public static AppointmentEntity build(Appointment a, String moduleID, String type){
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(a.getID());
        appointmentEntity.setModuleID(moduleID);
        appointmentEntity.setLocation(a.getLocation());
        appointmentEntity.setStartAt(a.getStartAt());
        appointmentEntity.setEndAt(a.getEndAt());
        appointmentEntity.setType(type);
        appointmentEntity.setNr(a.getNr());
        return appointmentEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentEntity that = (AppointmentEntity) o;
        return startAt.toString().equals(that.startAt.toString())
                && endAt.toString().equals(that.endAt.toString())
                && moduleID.equals(that.moduleID);
    }

}
