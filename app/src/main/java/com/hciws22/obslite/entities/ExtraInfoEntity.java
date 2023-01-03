package com.hciws22.obslite.entities;

import com.hciws22.obslite.sync.Appointment;
import com.hciws22.obslite.sync.OBSItem;

import org.jetbrains.annotations.NotNull;

public class ExtraInfoEntity {
    @NotNull
    private String nr;

    @NotNull
    private String moduleID;

    private String percentage;

    private String note;

    @NotNull
    public String getNr() {
        return nr;
    }

    public void setNr(@NotNull String nr) {
        this.nr = nr;
    }

    @NotNull
    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(@NotNull String moduleID) {
        this.moduleID = moduleID;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static ExtraInfoEntity build(OBSItem obsItem){
        ExtraInfoEntity extraInfoEntity = new ExtraInfoEntity();
        extraInfoEntity.setNr(obsItem.getAppointment().getNr());
        extraInfoEntity.setModuleID(obsItem.getId());
        return extraInfoEntity;
    }

    public static ExtraInfoEntity build1(String id, String nr){
        ExtraInfoEntity extraInfoEntity = new ExtraInfoEntity();
        extraInfoEntity.setNr(nr);
        extraInfoEntity.setModuleID(id);
        return extraInfoEntity;
    }

}


