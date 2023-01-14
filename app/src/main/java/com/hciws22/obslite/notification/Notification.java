package com.hciws22.obslite.notification;

public class Notification {

    private final Integer id;
    private final String moduleTitle;
    private final boolean newAdded;
    private final boolean oldChanged;
    private final boolean oldDeleted;
    private final boolean isDisabled;
    private String message;


    public Notification(Integer id, String moduleTitle, boolean newAdded, boolean oldChanged, boolean oldDeleted, boolean isDisabled) {
        this.id = id;
        this.moduleTitle = moduleTitle;
        this.newAdded = newAdded;
        this.oldChanged = oldChanged;
        this.oldDeleted = oldDeleted;
        this.isDisabled = isDisabled;
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
