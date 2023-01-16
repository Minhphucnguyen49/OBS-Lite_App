package com.hciws22.obslite.week;

import java.time.LocalDateTime;

public class Week {
    private String date;// next to each day of the week with day
    private String name;
    private String moduleType;
    private String time;
    private String location;

    private boolean isExpanded;

    public Week (String name, String moduleType, String location, String date, String time) {
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