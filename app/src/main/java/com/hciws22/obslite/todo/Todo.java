package com.hciws22.obslite.todo;

public class Todo {
    private String name;//name + prakID
    private String moduleType;

    private String percentage;

    private String date;
    //LocalDate Time;
    private String time;

    private String location;

    private boolean isExpanded;

    public Todo(String name, String moduleType, String percentage, String date, String time, String location) {
        this.name = name;
        this.moduleType = moduleType;
        this.percentage = percentage;
        this.date = date;
        this.time = time;
        this.location = location;
        isExpanded = false;
    }

    public String getName() {
        return name;
    }
    public boolean isExpanded() {
        return isExpanded;
    }
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
    public String getPercentage() {
        return percentage;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

}
