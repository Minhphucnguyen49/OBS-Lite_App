package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

        TextView title = findViewById(R.id.title_SETTING);
        EditText editText = findViewById(R.id.obs_link);
        TextView syncTime = findViewById(R.id.sync_time);

        Button sendButton = findViewById(R.id.send);
        Button toggleBtn = findViewById(R.id.button_toggle);

        TextView warningNoLink = findViewById(R.id.no_link_warning);
        ImageView warningSign = findViewById(R.id.image_warning);


        settingcontroller.init(
                title, editText,syncTime,
                sendButton,
                warningNoLink, warningSign,
                toggleBtn);
        settingcontroller.applyAllChanges(title,warningNoLink,editText,syncTime,warningSign);

    }

}