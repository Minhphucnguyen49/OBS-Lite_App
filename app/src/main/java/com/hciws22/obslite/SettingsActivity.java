package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.setting.SettingController;
import com.hciws22.obslite.setting.SettingsModel;
import com.hciws22.obslite.setting.Translation;
import com.hciws22.obslite.sync.SyncController;

public class SettingsActivity extends AppCompatActivity {

    private static String mode = "de";

    SyncController syncController = new SyncController(new SqLiteHelper(SettingsActivity.this));
    SettingController settingcontroller = new SettingController(syncController, new SettingsModel());
    //SharedPreferences sharedPreferences = getSharedPreferences("Mode", SettingsActivity.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button sendButton = findViewById(R.id.send);
        Button toggleBtn = findViewById(R.id.button_toggle);
        TextView title = findViewById(R.id.title_SETTING);

        settingcontroller.init(sendButton,title, toggleBtn, this );
        settingcontroller.applyChanges(title, sendButton, this);
    }







}