package com.hciws22.obslite.today;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.week.Week;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AgendaDbService {

    private static final String TABLE_APPOINTMENT = "Appointment";
    private static final String[] COLUMNS_FOR_APPOINTMENT = {"startAt", "endAt", "location", "type", "nr", "moduleID"};
    private final SqLiteHelper sqLiteHelper;

    public AgendaDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }


    private String selectTodayPattern() {
        return "SELECT " +
                COLUMNS_FOR_APPOINTMENT[0] + "," +
                COLUMNS_FOR_APPOINTMENT[1] + "," +
                COLUMNS_FOR_APPOINTMENT[2] + "," +
                COLUMNS_FOR_APPOINTMENT[3] + "," +
                COLUMNS_FOR_APPOINTMENT[4] + "," +
                COLUMNS_FOR_APPOINTMENT[5] +
                " FROM " + TABLE_APPOINTMENT + " WHERE " +
                COLUMNS_FOR_APPOINTMENT[0] + " LIKE '" + LocalDate.now() + "%'"
                + " ORDER BY " + COLUMNS_FOR_APPOINTMENT[0] + ";";
    }

    public List<Agenda> selectToDayAppointments() {
        List<Agenda> agendaList = new ArrayList<>();
        //TODO: String queryString = selectTodayPattern();
        String queryString = selectTodayPattern();
        Log.d("SQL TODAY: ", queryString);
        // close both cursor and the db.
        // Try-with-resources will always close all kinds of connection
        // after the Try-block has reached his end
        try (SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)) {


            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Agenda agenda = new Agenda(
                        getName(cursor.getString(5)),
                        cursor.getString(3),
                        cursor.getString(2),
                        getDate(cursor.getString(0)),
                        getTimePeriod(cursor.getString(0), cursor.getString(1)));
                agendaList.add(agenda);
            }
        }
        return agendaList;
    }


    private String getName(String name) {
        return name.substring(0, name.lastIndexOf(' '));
    }

    private String getTimePeriod(String startAt, String endAt) {
        LocalDateTime localDateTime1 = parseFormat(startAt);
        LocalDateTime localDateTime2 = parseFormat(endAt);

        return localDateTime1.toLocalTime() + " - " + localDateTime2.toLocalTime();

    }

    private String getDate(String dateToString) {
        LocalDateTime localDateTime = parseFormat(dateToString);

        return localDateTime
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.getDefault()) + " - " + localDateTime.toLocalDate().toString().replace("-", ".");
    }

    private LocalDateTime parseFormat(String dateToString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(dateToString, formatter);
    }


}
