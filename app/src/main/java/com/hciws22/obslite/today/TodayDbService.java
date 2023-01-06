package com.hciws22.obslite.today;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.hciws22.obslite.db.SqLiteHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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


    private String selectTodayPattern(){
        return "SELECT " +
                COLUMNS_FOR_APPOINTMENT[0] + "," +
                COLUMNS_FOR_APPOINTMENT[1] + "," +
                COLUMNS_FOR_APPOINTMENT[2] + "," +
                COLUMNS_FOR_APPOINTMENT[3] + "," +
                COLUMNS_FOR_APPOINTMENT[4] + "," +
                COLUMNS_FOR_APPOINTMENT[5] +
                " FROM " + TABLE_APPOINTMENT + " WHERE " +
                COLUMNS_FOR_APPOINTMENT[0] + " LIKE '" + LocalDate.now() + "%';";
    }

    @Deprecated(since=
            "Only for development purpose " +
            "(in vacation we don't have today objects). " +
            "delete function if not needed anymore")
    private String selectPattern(){
        return "SELECT " +
                COLUMNS_FOR_APPOINTMENT[0] + "," +
                COLUMNS_FOR_APPOINTMENT[1] + "," +
                COLUMNS_FOR_APPOINTMENT[2] + "," +
                COLUMNS_FOR_APPOINTMENT[3] + "," +
                COLUMNS_FOR_APPOINTMENT[4] + "," +
                COLUMNS_FOR_APPOINTMENT[5] +
                " FROM " + TABLE_APPOINTMENT + ";";
    }

    public List<Today> selectToDayAppointments() {
        List<Today> todayList = new ArrayList<>();
        String queryString = selectPattern();
        //TODO: String queryString = selectTodayPattern();

        // close both cursor and the db.
        // Try-with-resources will always close all kinds of connection
        // after the Try-block has reached his end
        try(SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
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


    private String getName(String name){
        return name.substring(0,name.lastIndexOf(' '));
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
                .getDisplayName(TextStyle.FULL, Locale.getDefault()) + " - " + localDateTime.toLocalDate().toString().replace("-",".");
    }

    private LocalDateTime parseFormat(String dateToString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(dateToString, formatter);
    }



}
