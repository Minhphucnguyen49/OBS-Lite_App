package com.hciws22.obslite.notification;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hciws22.obslite.R;

public class NotificationModel {


    private final NotificationCompat.Builder builder;

    public NotificationModel(NotificationCompat.Builder builder) {
        this.builder = builder;
    }


    public void buildNotification(Context context){

        /*builder
                .setSmallIcon(R.drawable.ic_sync)
                .setChannelId("myChannel")
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());*/

        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O){
            return;
        }

        CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        mChannel.setDescription(Description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mChannel.setShowBadge(false);
        notificationManager.createNotificationChannel(mChannel);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Hey was geht")
                .setContentText("huhuuhu");


        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }


    public void sendNotification(Context context) {
        NotificationChannel channel = new NotificationChannel("myChannel", "myChannel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = context.getSystemService(NotificationManager.class);


        manager.createNotificationChannel(channel);

    }
}
