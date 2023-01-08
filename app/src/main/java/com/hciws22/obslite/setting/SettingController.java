package com.hciws22.obslite.setting;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hciws22.obslite.sync.SyncController;

public class SettingController {

    private final SyncController syncController;
    private final SettingsModel settingsModel;

    public SettingController(SyncController syncController, SettingsModel settingsModel) {
        this.syncController = syncController;
        this.settingsModel = settingsModel;
    }


    public void init(View sendBtn, TextView title, Button toggle, EditText editText, Context context){

        syncController.init(sendBtn, editText);
        toggle.setOnClickListener(view -> {
            toggleLanguage(context);
            applyChanges(title, sendBtn, context);
        });
    }
    public void toggleLanguage(Context context){
        //TODO: Check current mode

        if (settingsModel.loadMode(context)){
            //toggle auf english
            settingsModel.saveMode(context,false);
        } else {
            settingsModel.saveMode(context,true);
        }
    }
    public void applyChanges (TextView title, View sendBtn, Context context){
        title.setText(Translation.getTranslation( Translation.TITLE_SETTINGS, settingsModel.loadMode(context) ));
        ( (Button)sendBtn ).setText( Translation.getTranslation( Translation.SEND_BTN, settingsModel.loadMode(context) ));
    }
//Optional<SharedPreferences> sharedPreferences = Optional.ofNullable(context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE));

}
