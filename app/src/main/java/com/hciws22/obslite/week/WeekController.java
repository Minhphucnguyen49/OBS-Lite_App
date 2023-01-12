package com.hciws22.obslite.week;

import com.hciws22.obslite.db.SqLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class WeekController {
    private final WeekDbService weekDbService;

    public WeekController(SqLiteHelper sqLiteHelper) {
        this.weekDbService = new WeekDbService(sqLiteHelper);
    }

    public List<Week> getWeekList() { return weekDbService.selectWeekAppointments(); }
    public ArrayList<Week> getWeekArrayList() {return weekDbService.selectWeekAppointmentsArray(); }

    public String shortenName(String fullName){
        String moduleInitials = "";
        if(fullName.contains(" ")) {
            //mehr als ein Wort
            for (String s : fullName.split(" ")) {
                moduleInitials += s.charAt(0);
            }
            return moduleInitials;
        }
        if(fullName.contains("Datenbanken")){
            return "DB";
        }
        if(fullName.contains("Betriebssysteme")){
            return "BS";
        }


        return fullName;
    }
    public ArrayList<String> getWeekString (ArrayList<Week> weekArrayList){
        ArrayList<String> weekString = new ArrayList<>();

        weekArrayList.forEach(s -> {
            weekString.add(s.getModuleType()+":"+shortenName(s.getName())+" "+s.getLocation()+" "+s.getTime());
        });

        return weekString;
    }
}
