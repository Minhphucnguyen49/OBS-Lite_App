package com.hciws22.obslite.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;

import com.hciws22.obslite.R;
import com.hciws22.obslite.setting.Translation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class NotificationModel {

    private final NotificationManager notificationManager;
    private static final String CHANNEL_ID = "appointment_notification";
    private static final String NAME = "appointment_notification";
    private static final String OBS_TITLE = "OBS Sync Update";
    private static final String DESCRIPTION = "Appointment table has changed";
    private static final String MODULE_DELETED_MESSAGE = "DELETED";
    private static final int NOTIFICATION_ID = 234;
    private final Context context;

    public NotificationModel(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void buildChannel() {

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, NAME, importance);
        mChannel.setDescription(DESCRIPTION);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mChannel.setShowBadge(false);
        notificationManager.createNotificationChannel(mChannel);

    }

    public void buildNotification(List<Notification> notifications){

        String organisation = Translation.getTranslation( Translation.NOTIFICATION_SUBTITLE, Translation.loadMode(context));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sync)
                .setContentTitle(OBS_TITLE)
                .setSubText(organisation);

        String content = buildNotificationMessage(notifications);

        builder.setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().setSummaryText(content));

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private String buildNotificationMessage(List<Notification> notifications){

        String time = Translation.getTranslation( Translation.NOTIFICATION_TIME, Translation.loadMode(context));

        StringBuilder content = new StringBuilder();
        String alreadySent = "";
        for(int i = 0; i < notifications.size(); i++) {

            Notification notification = notifications.get(i);

            if(!notification.getModuleTitle().equals(alreadySent)){
                alreadySent = notification.getModuleTitle();
                content.append(getContentType(notification));

                if(!notification.getMessage().equals(MODULE_DELETED_MESSAGE)){
                    content.append(notification.getType()).append(": ");
                }

                content.append(alreadySent.substring(0, alreadySent.lastIndexOf(" "))).append("\n");

                if (notification.getMessage().equals(MODULE_DELETED_MESSAGE)){
                    content.append("\n");
                    continue;
                }

                content.append(time)
                        .append(getNotificationTime(notification))
                        .append(getLocation(notification)).append("\n\n");
            }
        }

        return content.toString();

    }

    private String getContentType(Notification notification){

        String message = "";

        if(notification.isOldChanged())
            message = Translation.getTranslation( Translation.NOTIFICATION_SUB_TITLE_CHANGED_APP, Translation.loadMode(context));

        if(notification.isNewAdded())
            message = Translation.getTranslation( Translation.NOTIFICATION_SUB_TITLE_NEW_APP, Translation.loadMode(context));

        if(notification.getMessage().equals(MODULE_DELETED_MESSAGE))
            message = Translation.getTranslation( Translation.NOTIFICATION_SUB_TITLE_DELETED_MOD, Translation.loadMode(context));

        if(notification.isOldDeleted() && !notification.getMessage().equals(MODULE_DELETED_MESSAGE))
            message = Translation.getTranslation( Translation.NOTIFICATION_SUB_TITLE_DELETED_APP, Translation.loadMode(context));

        return message;

    }

    private String getLocation(Notification notification){
        if(notification.getLocation().isEmpty()) {
            return "";
        }

        return Translation.getTranslation( Translation.NOTIFICATION_LOCATION, Translation.loadMode(context)) + notification.getLocation();
    }

    private String getNotificationTime(Notification notification){

        //2022-10-03T06:30+02:00[Europe/Berlin] 2022-10-03T13:45+02:00[Europe/Berlin]
        String date1 = notification.getMessage().substring(0, notification.getMessage().lastIndexOf(" "));
        String date2 = notification.getMessage().substring(notification.getMessage().lastIndexOf(" ")+1);

        ZonedDateTime zonedDateTime1 = ZonedDateTime.parse(date1);
        ZonedDateTime zonedDateTime2 = ZonedDateTime.parse(date2);

        return " " + calculateOffset(zonedDateTime1) + " - " + calculateOffset(zonedDateTime2) + ", ";
    }


    private String calculateOffset(ZonedDateTime zonedDateTime){
        return zonedDateTime.toLocalTime().plusSeconds(zonedDateTime.getOffset().getTotalSeconds()).toString();
    }

    private String getNotificationDate(Notification notification){
        String dateFormat = extractDate(notification.getMessage(), 0, notification.getMessage().indexOf("T"));

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN );

        try {
            Date date = originalFormat.parse(dateFormat);

            dateFormat = targetFormat.format(date);
        }catch(ParseException p){
            p.printStackTrace();
        }

        return dateFormat;
    }


    private String extractDate(String dateFormat, int from, int to){
        dateFormat = dateFormat.substring(from, to);
        return dateFormat;
    }
}
