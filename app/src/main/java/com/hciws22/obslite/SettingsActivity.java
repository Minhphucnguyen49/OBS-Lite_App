package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hciws22.obslite.setting.SettingController;
import com.hciws22.obslite.sync.SyncController;

public class SettingsActivity extends AppCompatActivity {

    private static String mode = "de";

    SyncController syncController = new SyncController( SettingsActivity.this);
    SettingController settingcontroller = new SettingController(syncController, SettingsActivity.this);
    //SharedPreferences sharedPreferences = getSharedPreferences("Mode", SettingsActivity.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button sendButton = findViewById(R.id.send);
        Button toggleBtn = findViewById(R.id.button_toggle);
        TextView title = findViewById(R.id.title_SETTING);
        EditText editText = findViewById(R.id.obs_link);
        TextView syncTime = findViewById(R.id.sync_time);


        settingcontroller.init(sendButton,title, toggleBtn, editText, syncTime, this );
        settingcontroller.applyChanges(title, this);

        sendNotification();
    }

    public void sendNotification() {
        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Hey was geht")
                .setContentText("huhuuhu");


        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

}