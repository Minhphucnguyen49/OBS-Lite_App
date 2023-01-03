package com.hciws22.obslite.setting;

public enum Translation {
    SEND_BTN("schicken", "send"),
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
