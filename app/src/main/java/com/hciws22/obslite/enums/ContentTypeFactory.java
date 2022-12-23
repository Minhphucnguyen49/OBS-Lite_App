package com.hciws22.obslite.enums;

import com.hciws22.obslite.entities.Module;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

public enum ContentTypeFactory {
    UID, SUMMARY, LOCATION, DTSTART, DTEND, CATEGORIES;

    public static boolean isValid(String s){

        return  s.toUpperCase(Locale.ROOT).contains(LOCATION.name()) ||
                s.toUpperCase(Locale.ROOT).contains(DTSTART.name()) ||
                s.toUpperCase(Locale.ROOT).contains(DTEND.name()) ||
                s.toUpperCase(Locale.ROOT).contains(CATEGORIES.name()) ||
                s.toUpperCase(Locale.ROOT).contains(UID.name()) ||
                s.toUpperCase(Locale.ROOT).contains(SUMMARY.name());
    }

    public static void cut(String s, Module module){
        if(s.toUpperCase(Locale.ROOT).contains(UID.name())){
             module.setId(s.replace(UID.name() + ":", ""));
             return;
        }

        if(s.toUpperCase(Locale.ROOT).contains(SUMMARY.name())){
             module.setName(s.substring(s.lastIndexOf(":")+2));
             module.setType(s.substring(s.indexOf(":")+1, s.lastIndexOf(":")));
             return;
        }

        if(s.toUpperCase(Locale.ROOT).contains(LOCATION.name())){
             module.getAppointment().setLocation(s.substring(s.indexOf(":")+1));
             return;
        }

        if(s.toUpperCase(Locale.ROOT).contains(CATEGORIES.name())){
             module.setSemester(s.replace(CATEGORIES.name() + ":", ""));
             return;
        }

        if(s.toUpperCase(Locale.ROOT).contains(DTSTART.name())){
            String from =  s.replace(DTSTART.name() + ":", "");
            module.getAppointment().setFrom(dateFormat(from));
            return;

        }

        if(s.toUpperCase(Locale.ROOT).contains(DTEND.name())){
            String end =  s.replace(DTEND.name() + ":", "");
            module.getAppointment().setTo(dateFormat(end));
        }

    }

    public static LocalDateTime dateFormat(String s){
        return LocalDateTime.of(
                LocalDate.of(Integer.parseInt(s.substring(0,4)), Integer.parseInt(s.substring(4,6)), Integer.parseInt(s.substring(6,8))),
                LocalTime.of(Integer.parseInt(s.substring(9, 11)),Integer.parseInt(s.substring(11,13)),0)
        );
    }


}
