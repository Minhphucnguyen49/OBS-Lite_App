package com.hciws22.obslite.today;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hciws22.obslite.db.SqLiteHelper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TodayDbService {

    private static final String TABLE_APPOINTMENT = "Appointment";
    private static final String[] COLUMNS_FOR_APPOINTMENT = {"startAt", "endAt", "location", "type", "nr", "moduleID"};
    private final SqLiteHelper sqLiteHelper;

    public TodayDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    private String selectTodayPattern(ZonedDateTime time) {
        return "SELECT " +
                COLUMNS_FOR_APPOINTMENT[0] + "," +
                COLUMNS_FOR_APPOINTMENT[1] + "," +
                COLUMNS_FOR_APPOINTMENT[2] + "," +
                COLUMNS_FOR_APPOINTMENT[3] + "," +
                COLUMNS_FOR_APPOINTMENT[4] + "," +
                COLUMNS_FOR_APPOINTMENT[5] +
                " FROM " + TABLE_APPOINTMENT + " WHERE " +
                COLUMNS_FOR_APPOINTMENT[0] + " LIKE '" + time.toLocalDate() + "%'"
                + " ORDER BY " + COLUMNS_FOR_APPOINTMENT[0] + ";";
    }

    public List<Today> selectToDayAppointments(){
        return selectFunctionPattern(selectTodayPattern(ZonedDateTime.now(ZoneId.of("Europe/Berlin"))));
    }

    public List<Today> selectTomorrowAppointments(){
        return selectFunctionPattern(selectTodayPattern(ZonedDateTime.now(ZoneId.of("Europe/Berlin")).plusDays(1)));
    }

    public List<Today> selectFunctionPattern(String queryString) {
        List<Today> todayList = new ArrayList<>();
        //TODO: String queryString = selectTodayPattern();
        Log.d("SQL TODAY: ", queryString);
        // close both cursor and the db.
        // Try-with-resources will always close all kinds of connection
        // after the Try-block has reached his end
        try (SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)) {


            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Today today = new Today(
                        getName(cursor.getString(5)),
                        cursor.getString(3),
                        cursor.getString(2),
                        getDate(cursor.getString(0)),
                        getTimePeriod(cursor.getString(0), cursor.getString(1)));
                todayList.add(today);
            }
        }
        return todayList;
    }

    private String getName(String name) {
        return name.substring(0, name.lastIndexOf(' '));
    }

    private String getTimePeriod(String startAt, String endAt) {
        ZonedDateTime localDateTime1 = parseFormat(startAt);
        ZonedDateTime localDateTime2 = parseFormat(endAt);

        return localDateTime1.toLocalTime().plusSeconds(localDateTime1.getOffset().getTotalSeconds()) + " - " + localDateTime2.toLocalTime().plusSeconds(localDateTime2.getOffset().getTotalSeconds());

    }

    private String getDate(String dateToString) {
        ZonedDateTime localDateTime = parseFormat(dateToString);

        return localDateTime
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.getDefault()) + " - " + localDateTime.toLocalDate().toString().replace("-", ".");
    }

    private ZonedDateTime parseFormat(String dateToString){
       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return ZonedDateTime.parse(dateToString);
    }


}
