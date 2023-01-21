package com.hciws22.obslite.setting;

import android.content.Context;
import android.content.SharedPreferences;

public enum Translation {

    // Settings
    ERROR_NO_SYNC_DATE_FOUND("Es konnte keinen gültigen obs link gefunden werdne", "No valid obs link could be found"),
    TITLE_SETTINGS("Einstellungen", "Settings"),
    DARK_MODE("dunkel", "dark"),
    LIGHT_MODE("hell", "light"),
    LANGUAGE_GERMAN("Deutsch", "German"),
    LANGUAGE_ENGLISH("Englisch", "English"),

    NO_LINK_WARNUNG(
            "Deinen OBS-Link ist auf der OBS-Website:\nTerminliste > Abonnieren > Link kopieren",
            "Get your OBS-Link from the OBS-Website:\nSchedule > Subscribe > Copy link"),
    INSERT_PREVIEW(" OBS Link einfügen"," Insert OBS Link"),
    // Notifications
    NOTIFICATION_SUB_TITLE_NEW_APP("Neuer Termin:  ", "New Appointment:  "),
    NOTIFICATION_SUB_TITLE_CHANGED_APP("Änderung:  ", "Update:  "),
    NOTIFICATION_SUB_TITLE_DELETED_MOD("Modul gelöscht: ", "Module deleted: "),
    NOTIFICATION_SUB_TITLE_DELETED_APP("Termin gelöscht:  ", "Appointment deleted:  "),
    NOTIFICATION_LOCATION("Ort: ", "Location: "),
    NOTIFICATION_DATE("Datum: ","Date: "),
    NOTIFICATION_TIME("Zeit: ", "Time: "),
    NOTIFICATION_SUBTITLE("Hochschule Darmstadt, FBI", "University of applied science Darmstadt, FBI"),
    NOTIFICATION_SUB_TITLE(" Benachrichtigungen:", "Notification:"),
    NOTIFICATION_LANGUAGE_SUB_TITLE("Sprache:", "Language:"),
    NOTIFICATION_TOGGLE(" Benachrichtigungen\n ausschalten ", "deactivate\nnotifications "),
    NOTIFICATION_LANGUAGE_TOGGLE("Englisch ", "German "),
    NOTIFICATION_DAILY_ASSISTANT("OBS Assistent ", "OBS Assistant"),
    // Sync
    ERROR_OBS_LINK_UPDATE("OBS link konnte nicht aktualisiert werden.\nPrüfe deine Internet Verbindung","Could not update obs link.\nCheck your internet connection"),
    ERROR_INVALID_OBS_LINK("Bitte gebe eine gültige Url ein ", "Please Provide a valid url"),
    SYNC_DATE_FORMAT("Zuletzt aktualisert: ", "Last sync: "),
    RIGHT_NOW_SUCCESS_MSG("gerade jetzt", "just now"),

    //chips
    CURRENT_WEEK("AKTUELL", "CURRENT"),
    ALL_WEEK("GESAMT", "ALL"),
    EXAMS("PRÜFUNGEN", "EXAMS"),

    TODAY("HEUTE","TODAY"),
    TOMORROW("MORGEN","TOMORROW"),
    THIS_WEEK("DIESE WOCHE","THIS WEEK"),


    //Toasts
    SUCCESS_TOAST("Erfolgreich", "Success"),
    NO_APPOINTMENTS("Keine Termine gelistet", "No Appointments listed"),



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
