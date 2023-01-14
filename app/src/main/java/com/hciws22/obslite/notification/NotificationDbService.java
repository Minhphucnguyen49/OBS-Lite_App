package com.hciws22.obslite.notification;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.todo.Todo;

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

    private String whereCondition(int newAdded, int oldChanged,int oldDeleted){
        return " n WHERE n." + COLUMNS_FOR_NOTIFICATION[2] + " = " + newAdded + " " +
                "OR " + " n WHERE n." + COLUMNS_FOR_NOTIFICATION[3] + " = " + oldChanged + " " +
                "OR " + " n WHERE n." + COLUMNS_FOR_NOTIFICATION[4] + " = " + oldDeleted + ";";
    }

    private String deleteFromNotificationTableTemplate(int newAdded, int oldChanged,int oldDeleted ){
        return "DELETE FROM " + TABLE_NOTIFICATION + whereCondition(newAdded,oldChanged,oldDeleted);
    }

    private String selectNotificationTableTemplate(int newAdded, int oldChanged,int oldDeleted ) {
        return "SELECT * FROM " + TABLE_NOTIFICATION + whereCondition(newAdded, oldChanged, oldDeleted);
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

    public void truncateNotifications(){

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = truncateNotificationTableTemplate();
            db.execSQL(sql);
            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
            db.close();
        }
    }
    public void removeNotifications(boolean newAdded, boolean oldChanged, boolean oldDeleted){
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = deleteFromNotificationTableTemplate(convertBoolToInt(newAdded),convertBoolToInt(oldChanged),convertBoolToInt(oldDeleted));
            db.execSQL(sql);
            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
            db.close();
        }
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
