package com.hciws22.obslite.notification;

public class Notification {

    private final Integer id;
    private String type;
    private String location;
    private final String moduleTitle;
    private final boolean newAdded;
    private final boolean oldChanged;
    private final boolean oldDeleted;
    private final boolean isDisabled;
    private String message;


    public Notification(Integer id, String type, String location, String moduleTitle, boolean newAdded, boolean oldDeleted, boolean oldChanged, boolean isDisabled, String message) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.moduleTitle = moduleTitle;
        this.newAdded = newAdded;
        this.oldChanged = oldChanged;
        this.oldDeleted = oldDeleted;
        this.isDisabled = isDisabled;
        this.message = message;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
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

    public boolean isDisabled() {
        return isDisabled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
