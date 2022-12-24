package com.hciws22.obslite.entities;

import com.hciws22.obslite.application.Appointment;
import com.hciws22.obslite.application.Module;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public class AppointmentEntity {


    @NotNull
    private String moduleID;

    private LocalDateTime from;
    private LocalDateTime to;
    private String location;
    private String type;
    private String nr;



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


    public static AppointmentEntity fromAppointment(Appointment a, String u_id, String type){
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setModuleID(u_id);
        appointmentEntity.setLocation(a.getLocation());
        appointmentEntity.setFrom(a.getFrom());
        appointmentEntity.setTo(a.getTo());
        appointmentEntity.setType(type);
        appointmentEntity.setNr(a.getNr());
        return appointmentEntity;
    }
}
