package com.hciws22.obslite.setting;

import android.content.Context;
import android.content.SharedPreferences;

public enum Translation {

    // Settings
    ERROR_NO_SYNC_DATE_FOUND("Es konnte keinen gültigen obs link gefunden werdne", "No valid obs link could be found"),
    TITLE_SETTINGS("Einstellungen", "Settings"),
    DARK_MODE("dunkel", "dark"),
    LIGHT_MODE("hell", "light"),
    // Notifications
    NOTIFICATION_SUB_TITLE_NEW_APP("Neuer Termin", "New Appointment"),
    NOTIFICATION_SUB_TITLE_CHANGED_APP("Terminänderung", "Appointment chnaged"),
    NOTIFICATION_SUB_TITLE_DELETED_APP("Termin gelöscht", "Appointment deleted"),
    NOTIFICATION_LOCATION("\nOrt: ", "\nLocation: "),
    NOTIFICATION_DATE("Datum: ","Date: "),
    // Sync
    ERROR_OBS_LINK_UPDATE("OBS link konnte nicht aktualisiert werden.\nPrüfe deine Internet Verbindung","Could not update obs link.\nCheck your internet connection"),
    ERROR_INVALID_OBS_LINK("Ungültiges Url Format.\nBitte gebe eine gültige Url ein ", "Please Provide a valid url"),
    SYNC_DATE_FORMAT("Zuletzt aktualisert: ", "Last sync: "),
    RIGHT_NOW_SUCCESS_MSG("gerade jetzt", "just now"),


    // Default
    NO_TRANSLATION_PROVIDED("keine Übersetzung", "no translation");

    private String de;//true
    private String en;//false

    public static String getTranslation(Translation word, boolean language) {
        return word.getValue(language);
    }
    private String getValue(boolean language) {
        return language ? de : en;
    }

    public static boolean loadMode(Context context){
        SharedPreferences pref = context.getSharedPreferences("com.hciws22.obslite", Context.MODE_PRIVATE);
        return pref.getBoolean("mode", true);//true is german
    }
    Translation(String de, String en) {
        this.de = de;
        this.en = en;
    }
}
