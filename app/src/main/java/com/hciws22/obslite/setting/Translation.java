package com.hciws22.obslite.setting;

public enum Translation {

    // Settings
    ERROR_NO_SYNC_DATE_FOUND("Es konnte keinen gültigen obs link gefunden werdne", "No valid obs link could be found"),
    TITLE_SETTINGS("Einstellungen", "Settings"),
    DARK_MODE("dunkel", "dark"),
    LIGHT_MODE("hell", "light"),
    NO_TRANSLATION_PROVIDED("keine Übersetzung", "no translation");

    private String de;//true
    private String en;//false

    public static String getTranslation(Translation word, boolean language) {
        return word.getValue(language);
    }
    private String getValue(boolean language) {
        return language ? de : en;
    }

    Translation(String de, String en) {
        this.de = de;
        this.en = en;
    }
}
