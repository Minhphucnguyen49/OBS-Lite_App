package com.hciws22.obslite.today;

import android.widget.TextView;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.week.Week;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AgendaController {

    private final AgendaDbService agendaDbService;

    public AgendaController(SqLiteHelper sqLiteHelper) {
        this.agendaDbService = new AgendaDbService(sqLiteHelper);
    }

    public List<Agenda> getToDay(){
        return agendaDbService.selectToDayAppointments();
    }

    public void showDate(TextView dateToday){
        String dateToDay = LocalDate.now().toString();
        //dateToday.setText(getDate(dateToDay));
        dateToday.setText(getFormattedDate(dateToDay));
    }

    private String getFormattedDate(String dateString) {
        // Parse the date string to create a LocalDate object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);

        // Get the full name of the day of the week
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

        // Format the date in the desired format
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // Return the full name of the day of the week and the formatted date
        return dayName + " - " + formattedDate;
    }

    public ArrayList<Week> getWeekArrayList() {return agendaDbService.selectWeekAppointmentsArray(); }

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
