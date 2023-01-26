package com.hciws22.obslite.today;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.setting.Translation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public void showDate(TextView dateToday, boolean language){
        //dateToday.setText(getDate(dateToDay));
        dateToday.setText(getFormattedDate(ZonedDateTime.now(ZoneId.of("Europe/Berlin")), language));
    }
    public void showDateTomorrow(TextView dateToday, boolean language){
        //dateToday.setText(getDate(dateToDay));
        dateToday.setText(getFormattedDate(ZonedDateTime.now(ZoneId.of("Europe/Berlin")).plusDays(1), language));
    }

    private String getFormattedDate(ZonedDateTime zonedDateTime, boolean language) {
        // Parse the date string to create a LocalDate object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(zonedDateTime.toLocalDate().toString(), formatter);

        // Get the full name of the day of the week
        String dayName = "";
        if(language){
            dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMAN);
        }else {
            dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        }


        // Format the date in the desired format
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // Return the full name of the day of the week and the formatted date
        return dayName + " - " + formattedDate;
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
