package com.hciws22.obslite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/* ########## Do not implement your logic here ########
*  create your own DBService. See in MainActivity.class
*
*
*
 */

public class SqLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "obslite.db";
    private static final int DB_VERSION = 3;

    public SqLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    /* this is called the first time a database is accessed. There should be code here
    * in order to create a database
    *
    * This function will be triggered only once to see if a database exists already
    */
    @Override
    public void onCreate(SQLiteDatabase db) {
       String createModuleStatement = "" +
               "CREATE TABLE Module (" +
               "id TEXT PRIMARY KEY, " +
               "name TEXT NOT NULL, " +
               "semester TEXT NOT NULL);";

       String createAppointmentStatement = "" +
               "CREATE TABLE Appointment (" +
               "id TEXT PRIMARY KEY, " +
               "startAt TEXT, " +
               "endAt TEXT, " +
               "location TEXT, " +
               "type TEXT, " +
               "nr TEXT, " +
               "moduleID TEXT NOT NULL, " +
               "CONSTRAINT unique_appointment UNIQUE (startAt,endAt,moduleID), "+
               "FOREIGN KEY (moduleID) REFERENCES Module(id) " +
               "ON UPDATE CASCADE ON DELETE CASCADE" +
               ");";

       String createExtraInfoStatement = "" +
               "CREATE TABLE Extra (" +
               "name TEXT PRIMARY KEY, " +
               "percentage TEXT, " +
               "note TEXT );";

       String createSyncStatement = "" +
               "CREATE TABLE Sync (" +
               "id INTEGER PRIMARY KEY AUTOINCREMENT," +
               "obslink TEXT NOT NULL UNIQUE," +
               "synctime TEXT NOT NULL);";

       String createNotification = "" +
               "CREATE TABLE Notification(" +
               "id INTEGER PRIMARY KEY AUTOINCREMENT," +
               "type TEXT," +
               "location TEXT," +
               "moduleTitle TEXT," +
               "newAdded INTEGER DEFAULT 0," +
               "oldChanged INTEGER DEFAULT 0," +
               "oldDeleted INTEGER DEFAULT 0," +
               "message TEXT NOT NULL);";



        db.execSQL(createModuleStatement);
        db.execSQL(createAppointmentStatement);
        db.execSQL(createExtraInfoStatement);
        db.execSQL(createSyncStatement);
        db.execSQL(createNotification);

    }

    // this is called if the database version changes. It will automatically update the schema
    // when changing the version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
