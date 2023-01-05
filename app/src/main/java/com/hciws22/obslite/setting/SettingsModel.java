package com.hciws22.obslite.setting;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsModel {
    private static final String PREFERENCES_NAME = "com.hciws22.obslite";
    private static final String PREF_KEY = "mode";//true = deutsch, false = english

    public void saveMode(Context context, boolean mode){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_KEY, mode);
        editor.apply();
    }
    public boolean loadMode(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(PREF_KEY, true);//true is german
    }
}
