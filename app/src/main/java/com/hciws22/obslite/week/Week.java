package com.hciws22.obslite.week;

import java.util.Optional;

public class Week {
    private final String date;// next to each day of the week with day
    private final String name;
    private final String moduleType;
    private final String time;
    private final String location;

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
        return location.isEmpty() ? ("no location") : location;
    }
    public String getModuleType() {
        return moduleType;
    }


}