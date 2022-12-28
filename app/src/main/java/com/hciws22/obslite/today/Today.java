package com.hciws22.obslite.today;

public class Today {
    private String date;// on top of TODAY Screen
    private String name;//name
    private String moduleType;
    private String time;
    private String location;

    public Today(String name, String moduleType, String date, String time, String location) {
        this.name = name;
        this.moduleType = moduleType;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public String getName() {
        return name;
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
    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }


}
