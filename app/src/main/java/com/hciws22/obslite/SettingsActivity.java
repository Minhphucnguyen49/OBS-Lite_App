package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.setting.Settingcontroller;
import com.hciws22.obslite.sync.SyncController;

public class SettingsActivity extends AppCompatActivity {

    SyncController syncController = new SyncController(new SqLiteHelper(SettingsActivity.this));
    Settingcontroller settingcontroller = new Settingcontroller(syncController);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        settingcontroller.init(SettingsActivity.this, findViewById(R.id.send));
    }

}