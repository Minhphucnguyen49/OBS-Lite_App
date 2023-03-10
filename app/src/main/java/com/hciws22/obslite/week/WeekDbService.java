package com.hciws22.obslite.week;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hciws22.obslite.db.SqLiteHelper;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeekDbService {

    private static final String TABLE_APPOINTMENT = "Appointment";
    private static final String[] COLUMNS_FOR_APPOINTMENT = {"startAt", "endAt", "location", "type", "nr", "moduleID"};

    private final SqLiteHelper sqLiteHelper;

    public WeekDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    private String selectWeekPattern() {
        return "SELECT " +
                COLUMNS_FOR_APPOINTMENT[0] + "," +
                COLUMNS_FOR_APPOINTMENT[1] + "," +
                COLUMNS_FOR_APPOINTMENT[2] + "," +
                COLUMNS_FOR_APPOINTMENT[3] + "," +
                COLUMNS_FOR_APPOINTMENT[4] + "," +
                COLUMNS_FOR_APPOINTMENT[5] +
                " FROM " + TABLE_APPOINTMENT +
                " WHERE " + COLUMNS_FOR_APPOINTMENT[0] +
                " BETWEEN '" + ZonedDateTime.now().with(previousOrSame(DayOfWeek.MONDAY)) + "'" +
                " AND '" + ZonedDateTime.now().with(nextOrSame(DayOfWeek.SUNDAY)) + "';";
    }
    private String selectDayOfWeekPattern(int dayOfWeek) {
        return "SELECT " +
                COLUMNS_FOR_APPOINTMENT[0] + "," +
                COLUMNS_FOR_APPOINTMENT[1] + "," +
                COLUMNS_FOR_APPOINTMENT[2] + "," +
                COLUMNS_FOR_APPOINTMENT[3] + "," +
                COLUMNS_FOR_APPOINTMENT[4] + "," +
                COLUMNS_FOR_APPOINTMENT[5] +
                " FROM " + TABLE_APPOINTMENT + " WHERE " +
                COLUMNS_FOR_APPOINTMENT[0] +
                " LIKE '" + ZonedDateTime.now(ZoneId.of("Europe/Berlin")).with(previousOrSame(DayOfWeek.MONDAY)).plusDays(dayOfWeek).toLocalDate() + "%'" +
                " ORDER BY " + COLUMNS_FOR_APPOINTMENT[0] + ";";
    }

    public List<Week> selectTodayAppointment(int dayOfWeek) {
        List<Week> weekList = new ArrayList<>();
        String queryString = selectDayOfWeekPattern(dayOfWeek);

        // close both cursor and the db.
        // Try-with-resources will always close all kinds of connection
        // after the Try-block has reached his end
        try (SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Week week = new Week(
                        getName(cursor.getString(5)),
                        cursor.getString(3),
                        cursor.getString(2),
                        getDate(cursor.getString(0)),
                        getTimePeriod(cursor.getString(0), cursor.getString(1)));
                weekList.add(week);
            }
        }
        return weekList;
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
