package com.hciws22.obslite.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.hciws22.obslite.R;
import com.hciws22.obslite.setting.Translation;
import com.hciws22.obslite.today.Today;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import de.danielnaber.jwordsplitter.AbstractWordSplitter;
import de.danielnaber.jwordsplitter.GermanWordSplitter;

public class NotificationModel {

    private final NotificationManager notificationManager;
    private static final String CHANNEL_ID = "appointment_notification";
    private static final String NAME = "appointment_notification";
    private static final String OBS_TITLE = "OBS Sync Update";
    private static final String DESCRIPTION = "Appointment table has changed";
    private static final String MODULE_DELETED_MESSAGE = "DELETED";
    private static final int NOTIFICATION_ID = 234;
    private static final int MAX_WIDTH = 29;
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
    public NotificationCompat.Builder buildNotificationTitle(){
        String organisation = Translation.getTranslation( Translation.NOTIFICATION_SUBTITLE, Translation.loadMode(context));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sync)
                .setSubText(organisation);

        return builder;
    }

    public void buildNotification(List<Notification> notifications){

        NotificationCompat.Builder builder = buildNotificationTitle();
        builder.setContentTitle(OBS_TITLE);

        String content = buildNotificationMessage(notifications);

        builder.setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content));

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void buildDailyNotification(List<Today> appointments) {

        String agenda = Translation.getTranslation( Translation.NOTIFICATION_TODAY_AGENDA, Translation.loadMode(context));
        NotificationCompat.Builder builder = buildNotificationTitle();
        builder.setContentTitle(agenda);

        String content = buildDailyNotificationMessage(appointments);

        builder.setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().setSummaryText(content));

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
    private String buildDailyNotificationMessage(List<Today> appointments){
        StringBuilder content = new StringBuilder();

        for (int i = 0; i < appointments.size(); i++) {
            Today today = appointments.get(i);
            if(!today.getModuleType().isEmpty()) content.append(today.getModuleType()).append(": ");

            String name = shortenName(today.getName());
            content.append(name)
                    .append(",  ")
                    .append(today.getTime())
                    .append(",  ")
                    .append(today.getLocation())
                    .append("\n\n");
        }

        return content.toString();
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

    private String shortenName(String fullName){
        String moduleInitials = "";


        if(fullName.contains("Datenbanken")){
            return fullName.replace("Datenbanken", "DB");
        }

        if(fullName.contains("-")){
            fullName.replace("-", " ");
        }

        if(fullName.contains(" ")) {
            //mehr als ein Wort
            for (String s : fullName.split(" ")) {
                moduleInitials += s.charAt(0);
            }
            return moduleInitials;
        }

        return shortGermanWord(fullName);
    }

    private String shortGermanWord(String name){


        AbstractWordSplitter splitter = null;
        try {
            splitter = new GermanWordSplitter(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collection<String> splitWords = splitter.splitWord(name);

        String abbreviation = "";
        for (String german : splitWords) {
            abbreviation += german.charAt(0);
        }

        return abbreviation.toUpperCase(Locale.ROOT);

    }

}
