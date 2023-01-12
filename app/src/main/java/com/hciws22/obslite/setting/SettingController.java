package com.hciws22.obslite.setting;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hciws22.obslite.TodayActivity;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.SyncEntity;
import com.hciws22.obslite.sync.SyncController;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

public class SettingController {

    private final SyncController syncController;
    private final SettingsDbService settingsDbService;
    private final SettingsModel settingsModel;

    public SettingController(SyncController syncController, SqLiteHelper sqLiteHelper, SettingsModel settingsModel) {
        this.syncController = syncController;
        this.settingsModel = settingsModel;
        this.settingsDbService = new SettingsDbService(sqLiteHelper);
    }


    public void init(Button sendBtn, TextView title, Button toggle, EditText editText, TextView synctime, Context context){

        //syncController.init(sendBtn, editText, synctime);

        sendBtn.setOnClickListener(view -> updateSyncTime(synctime, editText));

        SyncEntity sync = settingsDbService.selectSyncData();
        Optional<ZonedDateTime> date = sync.getLocalDateTime();

        if (date.isPresent()){
            editText.setText(sync.getObsLink());
            synctime.setText(settingsModel.generateCurrentDate(date.get()));
        }

        //TODO: synctime needs to be translated
        toggle.setOnClickListener(view -> {
            toggleLanguage(context);
            applyChanges(title, context);
            //applyChanges(title, sendBtn, context);
        });
    }

    private void updateSyncTime(TextView syncTime, EditText editText) {
        boolean isValidUrl = syncController.checkUrlForm(editText.getText().toString());

        if(!isValidUrl){
            String errorMsg = "Please Provide a valid url";
            syncTime.setText(errorMsg);
            return;
        }
        syncTime.setText(syncController.updateSyncLabel(editText.getText().toString()));


    }

    public void toggleLanguage(Context context){
        //TODO: Check current mode

        //toggle auf english
        settingsModel.saveMode(context, !settingsModel.loadMode(context));
    }
    public void applyChanges (TextView title, Context context){
        title.setText(Translation.getTranslation( Translation.TITLE_SETTINGS, settingsModel.loadMode(context) ));
    }
//Optional<SharedPreferences> sharedPreferences = Optional.ofNullable(context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE));

}
