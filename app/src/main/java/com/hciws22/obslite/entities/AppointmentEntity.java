package com.hciws22.obslite.entities;

import com.hciws22.obslite.application.Appointment;
import com.hciws22.obslite.application.Module;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public class AppointmentEntity {

    private Integer id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String location;
    private String type;
    private String nr;

    @NotNull
    private String moduleID;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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


    public static AppointmentEntity fromAppointment(Appointment a, String u_id, String type){
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setModuleID(u_id);
        appointmentEntity.setLocation(a.getLocation());
        appointmentEntity.setStartAt(a.getStartAt());
        appointmentEntity.setEndAt(a.getEndAt());
        appointmentEntity.setType(type);
        appointmentEntity.setNr(a.getNr());
        return appointmentEntity;
    }
}
