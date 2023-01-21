package com.hciws22.obslite.setting;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.SyncEntity;
import com.hciws22.obslite.notification.NotificationController;
import com.hciws22.obslite.sync.SyncController;
import java.time.ZonedDateTime;
import java.util.Optional;

public class SettingController {

    private final SyncController syncController;
    private final NotificationController notificationController;
    private final SettingsDbService settingsDbService;
    private final SettingsModel settingsModel;
    private final Context context;

    public SettingController(SyncController syncController, Context context) {
        this.context = context;
        SqLiteHelper sqLiteHelper = new SqLiteHelper(context);
        this.syncController = syncController;
        this.settingsModel = new SettingsModel();
        this.notificationController = new NotificationController(context);
        this.settingsDbService = new SettingsDbService(sqLiteHelper);
    }


    public void init(TextView title, EditText editText, TextView syncTime,
                     Button sendBtn,
                     TextView warningNoLink, TextView languageTitle, TextView notificationTitle,
                     ImageView warningSign,
                     SwitchCompat languages, SwitchCompat notification, SwitchCompat dailyAssistant){

        sendBtn.setOnClickListener(view -> updateSyncTime(syncTime, editText, warningNoLink, warningSign));

        SyncEntity sync = settingsDbService.selectSyncData();
        Optional<ZonedDateTime> date = sync.getLocalDateTime();

        if (date.isPresent()){
            //hide warning
            warningNoLink.setVisibility(View.GONE);
            warningSign.setVisibility(View.GONE);


            editText.setText(sync.getObsLink());
            syncTime.setText(settingsModel.generateCurrentDate(date.get()));
        }else{
            //editText.setText(Translation.getTranslation( Translation.ERROR_NO_SYNC_DATE_FOUND, settingsModel.loadMode(context) ));
            //show warning
            warningNoLink.setVisibility(View.VISIBLE);
            warningSign.setVisibility(View.VISIBLE);

        }

        notification.setOnClickListener(view -> {
            toggleMode("notification");
        });

        dailyAssistant.setOnClickListener(view -> {
            toggleMode("daily-assistant");
        });

        languages.setOnClickListener(view -> {
            toggleMode("mode");
            applyAllChanges(title,warningNoLink,editText,syncTime,warningSign,languageTitle, notificationTitle, languages,notification, dailyAssistant );
            //applyChanges(title, sendBtn, context);
        });
    }

    private void updateSyncTime(TextView syncTime, EditText editText, TextView warningNoLink, ImageView warningSign) {
        boolean isValidUrl = syncController.checkUrlForm(editText.getText().toString());

        if(!isValidUrl){
            warningNoLink.setVisibility(View.VISIBLE);
            warningSign.setVisibility(View.VISIBLE);
            String errorMsg =  Translation.getTranslation( Translation.ERROR_INVALID_OBS_LINK, Translation.loadMode(context));
            syncTime.setText(errorMsg);
            return;
        }
        warningNoLink.setVisibility(View.GONE);
        warningSign.setVisibility(View.GONE);
        String success_toast = Translation.getTranslation( Translation.SUCCESS_TOAST, settingsModel.loadMode(context) );
        Toast.makeText(context, success_toast, Toast.LENGTH_SHORT).show();

        settingsDbService.resetDatabaseTemplate();
        syncTime.setText(syncController.updateSyncLabel(editText.getText().toString()));
        notificationController.clear();
    }

    public boolean isLanguageOn(){
        return settingsModel.loadMode(context);
    }

    public boolean isNotificationOn(){
        return settingsModel.loadMode(context, "notification");
    }

    public boolean isDailyAssistantOn(){
        return settingsModel.loadMode(context,"daily-assistant");
    }

    public void toggleMode(String mode){
        //TODO: Check current mode

        //toggle auf english
        settingsModel.saveMode(context, !settingsModel.loadMode(context, mode), mode);
    }

    public void applyAllChanges (TextView title, TextView warning,
                                 EditText editText, TextView syncTime,ImageView warningSign, TextView languageTitle, TextView notificationTitle,
                                 SwitchCompat languages, SwitchCompat notification, SwitchCompat dailyAssistant){
        title.setText(Translation.getTranslation( Translation.TITLE_SETTINGS, settingsModel.loadMode(context)));
        warning.setText(Translation.getTranslation( Translation.NO_LINK_WARNUNG, settingsModel.loadMode(context)));
        languageTitle.setText(Translation.getTranslation( Translation.NOTIFICATION_LANGUAGE_SUB_TITLE, settingsModel.loadMode(context)));
        notificationTitle.setText(Translation.getTranslation( Translation.NOTIFICATION_SUB_TITLE, settingsModel.loadMode(context)));
        dailyAssistant.setText(Translation.getTranslation( Translation.NOTIFICATION_DAILY_ASSISTANT, settingsModel.loadMode(context)));

        languages.setText(Translation.getTranslation( Translation.NOTIFICATION_LANGUAGE_TOGGLE, settingsModel.loadMode(context)));
        notification.setText(Translation.getTranslation( Translation.NOTIFICATION_TOGGLE, settingsModel.loadMode(context)));

        editText.setHint(Translation.getTranslation( Translation.INSERT_PREVIEW, settingsModel.loadMode(context)));
        updateSyncTime(syncTime,editText,warning,warningSign);
    }

//Optional<SharedPreferences> sharedPreferences = Optional.ofNullable(context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE));

}
