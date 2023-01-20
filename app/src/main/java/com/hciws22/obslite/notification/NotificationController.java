package com.hciws22.obslite.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.hciws22.obslite.db.SqLiteHelper;

import java.util.List;

public class NotificationController {

    private static final String PREFERENCES_NAME = "com.hciws22.obslite";
    private final NotificationDbService notificationDbService;
    private final NotificationModel notificationModel;
    private final SharedPreferences pref;

    public NotificationController(Context context) {
        SqLiteHelper sqLiteHelper = new SqLiteHelper(context);
        this.notificationDbService = new NotificationDbService(sqLiteHelper);
        this.notificationModel = new NotificationModel(context);
        this.pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private boolean isEnabled(){
        return pref.getBoolean("notification", true);
    }

    public boolean createNotification(){

         List<Notification> notifications = notificationDbService.selectNotifications(true,true, true);

         if (!isEnabled()){
             Log.d(" Notification: ", "notification are disabled");
             notificationDbService.removeNotifications(notifications);
             return false;
         }

         if(notifications.isEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
             Log.d("Current Notification: ", "No notifications");
             return false;
         }

         notificationModel.buildChannel();

         notificationModel.buildNotification(notifications);

         notificationDbService.removeNotifications(notifications);

         return false;
    }

    public void clear() {
        notificationDbService.truncateNotifications();
    }
}
