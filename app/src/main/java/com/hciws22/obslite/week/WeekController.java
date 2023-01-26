package com.hciws22.obslite.week;

import android.content.Context;
import android.widget.TextView;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.setting.Translation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import de.danielnaber.jwordsplitter.AbstractWordSplitter;
import de.danielnaber.jwordsplitter.GermanWordSplitter;

public class WeekController {
    private final WeekDbService weekDbService;

    public WeekController(SqLiteHelper sqLiteHelper) {
        this.weekDbService = new WeekDbService(sqLiteHelper);
    }

    public List<Week> getListDetailOf(int day) { return weekDbService.selectTodayAppointment(day); }

    public HashMap<String, List<Week>> getData(){
        LinkedHashMap<String, List<Week>> expandableListDetail = new LinkedHashMap<String, List<Week>>();

        List<Week> monday = new ArrayList<Week>(getListDetailOf(0));
        List<Week> tuesday = new ArrayList<Week>(getListDetailOf(1));
        List<Week> wednesday = new ArrayList<Week>(getListDetailOf(2));
        List<Week> thursday = new ArrayList<Week>(getListDetailOf(3));
        List<Week> friday = new ArrayList<Week>(getListDetailOf(4));

        expandableListDetail.put("MONDAY",monday);
        expandableListDetail.put("TUESDAY",tuesday);
        expandableListDetail.put("WEDNESDAY",wednesday);
        expandableListDetail.put("THURSDAY",thursday);
        expandableListDetail.put("FRIDAY",friday);

        return expandableListDetail;
    }

    public LinkedHashMap<String, List<String>> getDataString(Context context){
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List<String> monday = getWeekString(getListDetailOf(0));
        List<String> tuesday = getWeekString(getListDetailOf(1));
        List<String> wednesday = getWeekString(getListDetailOf(2));
        List<String> thursday = getWeekString(getListDetailOf(3));
        List<String> friday = getWeekString(getListDetailOf(4));

        expandableListDetail.put(Translation.getTranslation( Translation.MONDAY, Translation.loadMode(context)),monday);
        expandableListDetail.put(Translation.getTranslation( Translation.TUESDAY, Translation.loadMode(context)),tuesday);
        expandableListDetail.put(Translation.getTranslation( Translation.WEDNESDAY, Translation.loadMode(context)),wednesday);
        expandableListDetail.put(Translation.getTranslation( Translation.THURSDAY, Translation.loadMode(context)),thursday);
        expandableListDetail.put(Translation.getTranslation( Translation.FRIDAY, Translation.loadMode(context)),friday);

        return expandableListDetail;
    }

    public String shortenName(String fullName){
        String moduleInitials = "";


        if(fullName.contains("Datenbanken")){
            return fullName.replace("Datenbanken", "DB");
        }

        if(fullName.contains("-")){
            fullName.replace("-", " ");
        }

        if(fullName.contains(" ")) {
            //mehr als ein Wort
            for (String s : fullName.split(" ")) {
                moduleInitials += s.charAt(0);
            }
            return moduleInitials;
        }

        return shortGermanWord(fullName);
    }

    public String shortGermanWord(String name){


        AbstractWordSplitter splitter = null;
        try {
            splitter = new GermanWordSplitter(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collection<String> splitWords = splitter.splitWord(name);

        String abbreviation = "";
        for (String german : splitWords) {
            abbreviation += german.charAt(0);
        }

        return abbreviation.toUpperCase(Locale.ROOT);

    }
    public List<String> getWeekString (List<Week> weekArrayList){
        List<String> weekString = new ArrayList<>();
        String tab = "\t \t \t \t \t \t \t \t \t";
        weekArrayList.forEach(s -> {
            String location = s.getLocation();
            String type = s.getModuleType() + ":";

            if(type.equals(":")) type = "S:";


            weekString.add(type + shortenName(s.getName())+ tab + location + tab + s.getTime());
        });

        return weekString;
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
}
