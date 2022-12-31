package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.setting.SettingController;
import com.hciws22.obslite.sync.SyncController;

public class SettingsActivity extends AppCompatActivity {

    SyncController syncController = new SyncController(new SqLiteHelper(SettingsActivity.this));
    SettingController settingcontroller = new SettingController(syncController);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        settingcontroller.init(findViewById(R.id.send));
    }

}