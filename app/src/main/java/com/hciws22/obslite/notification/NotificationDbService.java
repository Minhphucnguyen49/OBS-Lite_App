package com.hciws22.obslite.notification;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hciws22.obslite.db.SqLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationDbService {

    private static final String TABLE_NOTIFICATION = "Notification";
    private static final String[] COLUMNS_FOR_NOTIFICATION = {"id", "moduleTitle", "newAdded", "oldChanged", "oldDeleted", "isDisabled", "message"};

    public SqLiteHelper sqLiteHelper;

    public NotificationDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }


    private String truncateNotificationTableTemplate(){
        return "DELETE FROM " + TABLE_NOTIFICATION + ";";
    }

    private String selectNotificationTableTemplate(int newAdded, int oldChanged,int oldDeleted ){
        return "SELECT * FROM " + TABLE_NOTIFICATION + " n WHERE n." + COLUMNS_FOR_NOTIFICATION[2] + " = " + newAdded + " " +
                "OR " + " n WHERE n." + COLUMNS_FOR_NOTIFICATION[3] + " = " + oldChanged + " " +
                "OR " + " n WHERE n." + COLUMNS_FOR_NOTIFICATION[4] + " = " + oldDeleted + ";";
    }

    private int convertBoolToInt(Boolean bool){
      return  bool ? 1 : 0;
    }

    private Boolean convertIntToBool(int bool){
        return bool == 1;
    }


    private String getName(String name){
        return name.substring(0,name.lastIndexOf(' '));
    }

    public List<Notification> selectNotifications(boolean newAdded, boolean oldChanged, boolean oldDeleted){
        String sql = selectNotificationTableTemplate(convertBoolToInt(newAdded), convertBoolToInt(oldChanged), convertBoolToInt(oldDeleted));

        List<Notification> notifications = new ArrayList<>();

        try(SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null)) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Notification notification = new Notification(
                        cursor.getInt(0),
                        getName(cursor.getString(1)),
                        convertIntToBool(cursor.getInt(2)),
                        convertIntToBool(cursor.getInt(3)),
                        convertIntToBool(cursor.getInt(4)),
                        convertIntToBool(cursor.getInt(5))
                );

                notifications.add(notification);
            }
        }

        return notifications;
    }





}
