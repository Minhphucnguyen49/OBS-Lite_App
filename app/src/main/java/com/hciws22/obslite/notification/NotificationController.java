package com.hciws22.obslite.notification;

import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.hciws22.obslite.db.SqLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationController {

    private final NotificationDbService notificationDbService;
    private final NotificationModel notificationModel;
    private static final String channelId = "TestChannel";
    private final Context context;

    public NotificationController(SqLiteHelper sqLiteHelper, Context context) {
        this.notificationDbService = new NotificationDbService(sqLiteHelper);
        this.notificationModel = new NotificationModel(new NotificationCompat.Builder(context, "my Notifikation"));

        this.context = context;
    }


    public void createNotification(){

         List<Notification> notifications = notificationDbService.selectNotifications(true,false, false);

         if(notifications.isEmpty()){
             return;
         }

         notificationModel.buildNotification(context);

         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
             notificationModel.sendNotification(context);
         }


    }


}
