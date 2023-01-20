package com.hciws22.obslite.today;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.setting.Translation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class TodayController {

    private final TodayDbService todayDbService;

    public TodayController(SqLiteHelper sqLiteHelper) {
        this.todayDbService = new TodayDbService(sqLiteHelper);
    }

    public List<Today> getToDay(){
        return todayDbService.selectToDayAppointments();
    }
    public List<Today> getTomorrow(){
        return todayDbService.selectTomorrowAppointments();
    }

    private static final String PREFERENCES_NAME = "com.hciws22.obslite";
    private static final String PREF_KEY = "mode";//true = deutsch, false = english

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
    public boolean loadMode(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(PREF_KEY, true);//true is german
    }

    public void applyAllChanges(Chip today, Chip tomorrow, Chip thisWeek, Context context){
        today.setText(Translation.getTranslation( Translation.TODAY,this.loadMode(context)));
        tomorrow.setText(Translation.getTranslation( Translation.TOMORROW,this.loadMode(context)));
        thisWeek.setText(Translation.getTranslation( Translation.THIS_WEEK,this.loadMode(context)));
    }


}
