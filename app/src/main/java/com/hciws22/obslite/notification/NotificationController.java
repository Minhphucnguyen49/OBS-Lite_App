package com.hciws22.obslite.notification;

import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.hciws22.obslite.db.SqLiteHelper;

import java.util.List;

public class NotificationController {

    private final NotificationDbService notificationDbService;
    private final NotificationModel notificationModel;

    public NotificationController(SqLiteHelper sqLiteHelper, Context context) {
        this.notificationDbService = new NotificationDbService(sqLiteHelper);
        this.notificationModel = new NotificationModel(context);
    }

    public void createNotification(){

         List<Notification> notifications = notificationDbService.selectNotifications(true,false, false);

         if(notifications.isEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
             return;
         }

         notificationModel.buildChannel();

         notificationModel.buildNotification(notifications);

         notificationDbService.removeNotifications(notifications);
    }

    public void clear() {
        notificationDbService.truncateNotifications();
    }
}
