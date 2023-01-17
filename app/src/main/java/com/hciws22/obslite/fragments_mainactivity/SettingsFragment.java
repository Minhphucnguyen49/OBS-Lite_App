package com.hciws22.obslite.fragments_mainactivity;

import android.content.Context;
import android.os.Bundle;

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
    //SharedPreferences sharedPreferences = getSharedPreferences("Mode", SettingsActivity.MODE_PRIVATE);
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

        Button sendButton = view.findViewById(R.id.send);
        Button toggleBtn = view.findViewById(R.id.button_toggle);
        TextView title = view.findViewById(R.id.title_SETTING);
        EditText editText = view.findViewById(R.id.obs_link);
        TextView syncTime = view.findViewById(R.id.sync_time);
        TextView warningNoLink = view.findViewById(R.id.no_link_warning);
        ImageView warningSign = view.findViewById(R.id.image_warning);

        settingcontroller.init(sendButton, title, toggleBtn, editText,syncTime,warningNoLink,warningSign, mContext);
        settingcontroller.applyChanges(title);

        // Inflate the layout for this fragment
        return view;
    }
}