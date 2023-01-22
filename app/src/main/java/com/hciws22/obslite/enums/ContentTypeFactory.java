package com.hciws22.obslite.enums;

import com.hciws22.obslite.sync.OBSItem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

public enum ContentTypeFactory {
   UID, SUMMARY, LOCATION, DTSTART, DTEND, CATEGORIES, VEVENT;

    public static boolean isValid(String s){

        return  s.toUpperCase(Locale.ROOT).contains(UID.name()) ||
                s.toUpperCase(Locale.ROOT).contains(LOCATION.name()) ||
                s.toUpperCase(Locale.ROOT).contains(DTSTART.name()) ||
                s.toUpperCase(Locale.ROOT).contains(DTEND.name()) ||
                s.toUpperCase(Locale.ROOT).contains(CATEGORIES.name()) ||
                s.toUpperCase(Locale.ROOT).contains(SUMMARY.name()) ||
                s.toUpperCase(Locale.ROOT).contains("END:" + VEVENT.name());
    }

    public static boolean cut(String s, OBSItem obsItem){

        if(s.toUpperCase(Locale.ROOT).contains(UID.name())){
            obsItem.getAppointment().setId(s.substring(4));
            return false;
        }

        if(s.toUpperCase(Locale.ROOT).contains(SUMMARY.name())){
            extractFileNameFromLine(obsItem,s);
            obsItem.getAppointment().setType(s.substring(s.indexOf(":")+1, s.lastIndexOf(":")));
            return false;
        }

        if(s.toUpperCase(Locale.ROOT).contains(LOCATION.name())){
            obsItem.getAppointment().setLocation(s.substring(s.indexOf(":")+1));
             return false;
        }

        if(s.toUpperCase(Locale.ROOT).contains(CATEGORIES.name())){
            obsItem.setSemester(s.replace(CATEGORIES.name() + ":", ""));
             return false;
        }

        if(s.toUpperCase(Locale.ROOT).contains(DTSTART.name())){
            String from =  s.replace(DTSTART.name() + ":", "");
            obsItem.getAppointment().setStartAt(dateFormat(from));
            return false;
        }

        if(s.toUpperCase(Locale.ROOT).contains(DTEND.name())){
            String end =  s.replace(DTEND.name() + ":", "");
            obsItem.getAppointment().setEndAt(dateFormat(end));
            return false;
        }

        // return only true when all relevant attributes are correctly set and the current line
        // marked the end of one attribute group of an obs item
        if(s.toUpperCase(Locale.ROOT).contains("END:" + VEVENT.name())){
            obsItem.setId(obsItem.getName() + " " + obsItem.getSemester());
            return true;
        }

        return false;
    }

    private static ZonedDateTime dateFormat(String s){

        return ZonedDateTime.of(
                LocalDate.of(Integer.parseInt(s.substring(0,4)), Integer.parseInt(s.substring(4,6)), Integer.parseInt(s.substring(6,8))),
                LocalTime.of(Integer.parseInt(s.substring(9, 11)),Integer.parseInt(s.substring(11,13)),0),
                ZoneId.of("Europe/Berlin")

        );
    }

    private static void extractFileNameFromLine(OBSItem obsItem, String s){
        if(s.contains("#")){
            obsItem.setName(s.substring(s.lastIndexOf(":")+2,s.lastIndexOf("#")-1));
            obsItem.getAppointment().setNr(s.substring(s.lastIndexOf("#")));
        }else{
            obsItem.setName(s.substring(s.lastIndexOf(":")+2));
        }

        obsItem.getAppointment().setType(s.substring(s.indexOf(":")+1, s.lastIndexOf(":")));

    }


}
