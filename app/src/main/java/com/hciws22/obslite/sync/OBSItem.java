package com.hciws22.obslite.sync;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class OBSItem {

    @NotNull
    private String id;
    private String name;
    private String semester;
    private Appointment appointment;

    public OBSItem() {
        appointment = new Appointment();

    }

    public String getId() {
        return Optional.ofNullable(id).orElse("");
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getName() {
        return Optional.ofNullable(name).orElse("");
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSemester() {
        return Optional.ofNullable(semester).orElse("");
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

}
