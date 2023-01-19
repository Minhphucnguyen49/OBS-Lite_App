package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

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

        TextView title = findViewById(R.id.title_SETTING);
        EditText editText = findViewById(R.id.obs_link);
        TextView syncTime = findViewById(R.id.sync_time);

        Button sendButton = findViewById(R.id.send);
        SwitchCompat languages = findViewById(R.id.language_toggle);
        SwitchCompat notifications = findViewById(R.id.notification_toggle);
        SwitchCompat dailyAssistant = findViewById(R.id.daily_assistant_toggle);

        TextView warningNoLink = findViewById(R.id.no_link_warning);
        TextView notificationTitle = findViewById(R.id.Notifications_Area);
        TextView languageTitle = findViewById(R.id.Language_Area);

        ImageView warningSign = findViewById(R.id.image_warning);


        settingcontroller.init(
                title, editText,syncTime,
                sendButton,
                warningNoLink, languageTitle, notificationTitle, warningSign,
                languages, notifications, dailyAssistant);

        settingcontroller.applyAllChanges(title,warningNoLink,editText,syncTime,warningSign, languageTitle, notificationTitle,languages, notifications, dailyAssistant );

    }

}