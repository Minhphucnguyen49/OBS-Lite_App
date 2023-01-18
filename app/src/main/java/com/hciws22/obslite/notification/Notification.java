package com.hciws22.obslite.notification;

import java.util.Optional;

public class Notification {

    private Integer id;
    private String type;
    private String location;
    private String moduleTitle;
    private boolean newAdded;
    private boolean oldDeleted;
    private boolean oldChanged;
    private String message;

    public Notification(){

    }

    public Notification(Integer id, String type, String location, String moduleTitle, boolean newAdded, boolean oldChanged, boolean oldDeleted, String message) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.moduleTitle = moduleTitle;
        this.newAdded = newAdded;
        this.oldChanged = oldChanged;
        this.oldDeleted = oldDeleted;
        this.message = message;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public void setNewAdded(boolean newAdded) {
        this.newAdded = newAdded;
    }

    public void setOldDeleted(boolean oldDeleted) {
        this.oldDeleted = oldDeleted;
    }

    public void setOldChanged(boolean oldChanged) {
        this.oldChanged = oldChanged;
    }


    public String getLocation() {
        return Optional.ofNullable(location).orElse("");
    }

    public String getType() {
        return Optional.ofNullable(type).orElse("");
    }

    public Integer getId() {
        return id;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public boolean isNewAdded() {
        return newAdded;
    }

    public boolean isOldChanged() {
        return oldChanged;
    }

    public boolean isOldDeleted() {
        return oldDeleted;
    }

    public String getMessage() {
        return Optional.ofNullable(message).orElse("");
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
