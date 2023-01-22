package com.hciws22.obslite.notification;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.today.Today;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.danielnaber.jwordsplitter.AbstractWordSplitter;
import de.danielnaber.jwordsplitter.GermanWordSplitter;

public class NotificationDbService {

    private static final String TABLE_NOTIFICATION = "Notification";
    private static final String[] COLUMNS_FOR_NOTIFICATION = {"id", "type", "location", "moduleTitle", "newAdded", "oldChanged", "oldDeleted", "message"};
    private static final String MODULE_DELETED_MESSAGE = "DELETED";

    private static final String TABLE_APPOINTMENT = "Appointment";
    private static final String[] COLUMNS_FOR_APPOINTMENT = {"startAt", "endAt", "location", "type", "nr", "moduleID"};
    private static final Boolean IS_DELETED = true;
    public SqLiteHelper sqLiteHelper;

    public NotificationDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }


    private String truncateNotificationTableTemplate() {
        return "DELETE FROM " + TABLE_NOTIFICATION + ";";
    }

    private String whereCondition(int newAdded, int oldChanged, int oldDeleted) {
        return " WHERE " + COLUMNS_FOR_NOTIFICATION[4] + " = '" + newAdded + "' " +
                "OR " + COLUMNS_FOR_NOTIFICATION[5] + " = '" + oldChanged + "' " +
                "OR " + COLUMNS_FOR_NOTIFICATION[6] + " = '" + oldDeleted + "';";
    }

    private String deleteFromNotificationTableWhereTemplate(Notification notification) {
        return "DELETE FROM " + TABLE_NOTIFICATION + " WHERE id = " + notification.getId() + ";";
    }


    private String selectNotificationTableTemplate(int newAdded, int oldChanged, int oldDeleted) {
        return "SELECT * FROM " + TABLE_NOTIFICATION + whereCondition(newAdded, oldChanged, oldDeleted);
    }

    private int convertBoolToInt(Boolean bool) {
        return bool ? 1 : 0;
    }

    private Boolean convertIntToBool(int bool) {
        return bool == 1;
    }


    private String getName(String name) {
        return name.substring(0, name.lastIndexOf(' '));
    }

    public void truncateNotifications() {

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = truncateNotificationTableTemplate();
            db.execSQL(sql);
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void removeNotifications(List<Notification> notifications) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            for (Notification notification : notifications) {
                String sql = deleteFromNotificationTableWhereTemplate(notification);
                db.execSQL(sql);
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Notification> selectNotifications(boolean newAdded, boolean oldChanged, boolean oldDeleted) {
        String sql = selectNotificationTableTemplate(convertBoolToInt(newAdded), convertBoolToInt(oldChanged), convertBoolToInt(oldDeleted));

        List<Notification> notifications = new ArrayList<>();

        try (SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(sql, null)) {

            if (!cursor.moveToFirst()) return Collections.emptyList();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Notification notification = new Notification();
                notification.setId(cursor.getInt(0));
                notification.setModuleTitle(cursor.getString(3));
                notification.setNewAdded(convertIntToBool(cursor.getInt(4)));
                notification.setOldChanged(convertIntToBool(cursor.getInt(5)));
                notification.setOldDeleted(convertIntToBool(cursor.getInt(6)));

                if (!notification.getMessage().equals(MODULE_DELETED_MESSAGE)) {
                    notification.setType(cursor.getString(1));
                    notification.setLocation(cursor.getString(2));
                    notification.setMessage(cursor.getString(7));
                }

                notifications.add(notification);
            }
        }

        return notifications;
    }


    public List<Today> selectTodayAppointments() {
        List<Today> todayList = new ArrayList<>();
        //TODO: String queryString = selectTodayPattern();
        String queryString = selectTodayPattern(ZonedDateTime.now(ZoneId.of("Europe/Berlin")));
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
