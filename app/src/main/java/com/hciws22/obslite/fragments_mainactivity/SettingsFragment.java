package com.hciws22.obslite.fragments_mainactivity;

import android.content.Context;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.hciws22.obslite.R;
import com.hciws22.obslite.setting.SettingController;
import com.hciws22.obslite.sync.SyncController;

public class SettingsFragment extends Fragment {
    private Context mContext;
    private static String mode = "de";

    SyncController syncController;
    SettingController settingcontroller;
    SwitchCompat languages;
    SwitchCompat notifications;
    SwitchCompat dailyAssistant;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        syncController = new SyncController(mContext);
        settingcontroller = new SettingController(syncController, mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);
        super.onViewCreated(view, savedInstanceState);

        TextView title = view.findViewById(R.id.title_SETTING);
        EditText editText = view.findViewById(R.id.obs_link);
        TextView syncTime = view.findViewById(R.id.sync_time);

        Button sendButton = view.findViewById(R.id.send);
        TextView syncNow = view.findViewById(R.id.Sync_button_description);

        languages = view.findViewById(R.id.language_toggle);
        notifications = view.findViewById(R.id.notification_toggle);
        dailyAssistant = view.findViewById(R.id.daily_assistant_toggle);

        TextView notificationTitle = view.findViewById(R.id.Notifications_Area);
        TextView languageTitle = view.findViewById(R.id.Language_Area);

        TextView warningNoLink = view.findViewById(R.id.no_link_warning);
        ImageView warningSign = view.findViewById(R.id.image_warning);

        settingcontroller.init(
                title, editText,syncTime,
                sendButton,
                warningNoLink, languageTitle, notificationTitle, warningSign,
                languages, notifications, dailyAssistant);
        settingcontroller.applyAllChanges(title,warningNoLink,editText,syncTime,warningSign, languageTitle, notificationTitle, languages, notifications, dailyAssistant);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        languages.setChecked(settingcontroller.isLanguageOn());
        notifications.setChecked(settingcontroller.isNotificationOn());
        dailyAssistant.setChecked(settingcontroller.isDailyAssistantOn());

    }
}