package com.hciws22.obslite.setting;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.ZonedDateTime;

public class SettingsModel {
    private static final String PREFERENCES_NAME = "com.hciws22.obslite";
    private static final String PREF_KEY = "mode";//true = deutsch, false = english

    public void saveMode(Context context, boolean value, String mode ){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(mode, value);
        editor.apply();
    }

    public boolean loadMode(Context context){
        return loadMode(context, PREF_KEY);
    }

    public boolean loadMode(Context context, String prefKey){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(prefKey, true);//true is german
    }

    public String generateCurrentDate(ZonedDateTime date){
        return  "Last sync: " +
                date.getDayOfMonth() + "." + date.getMonthValue() + "." + date.getYear() + ", " +
                date.getHour() + ":"  + date.getMinute();
    }

}
