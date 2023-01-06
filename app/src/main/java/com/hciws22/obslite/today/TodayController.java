package com.hciws22.obslite.today;

import android.widget.TextView;

import com.hciws22.obslite.R;
import com.hciws22.obslite.db.SqLiteHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public void showDate(TextView dateToday){
        String dateToDay = LocalDate.now().toString();
        //dateToday.setText(getDate(dateToDay));
        dateToday.setText(getFormattedDate(dateToDay));
    }

    private String getDate(String dateToString) {
        LocalDateTime localDateTime = parseFormat(dateToString);

        return localDateTime
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.getDefault()) + " - " + localDateTime.toLocalDate().toString().replace("-",".");
    }

    private LocalDateTime parseFormat(String dateToString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDateTime.parse(dateToString, formatter);
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
