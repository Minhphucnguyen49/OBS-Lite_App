package com.hciws22.obslite.sync;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.AppointmentEntity;
import com.hciws22.obslite.entities.ModuleEntity;
import com.hciws22.obslite.entities.SyncEntity;
import com.hciws22.obslite.notification.Notification;
import com.hciws22.obslite.today.Today;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SyncDbService {

    private static final String TABLE_MODULE = "Module";
    private static final String[] COLUMNS_FOR_MODULE = {"id", "name", "semester"};

    private static final String TABLE_APPOINTMENT = "Appointment";
    private static final String[] COLUMNS_FOR_APPOINTMENT = {"id", "startAt", "endAt", "location", "type", "nr", "moduleID"};

    private static final String TABLE_SYNC = "Sync";
    private static final String[] COLUMNS_FOR_SYNC = {"id", "obsLink", "syncTime"};

    private static final String TABLE_NOTIFICATION = "Notification";
    private static final String[] COLUMNS_FOR_NOTIFICATION = {"id", "type", "location", "moduleTitle", "newAdded", "oldChanged", "oldDeleted", "message"};


    public SqLiteHelper sqLiteHelper;

    public SyncDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    private String updateModulesTemplate(){
        return "insert or replace into " +
                TABLE_MODULE +" ("+
                COLUMNS_FOR_MODULE[0] + ", "+
                COLUMNS_FOR_MODULE[1]  +", "+
                COLUMNS_FOR_MODULE[2] + ") values ";
    }

    private String selectUnmodifiedAppointment(AppointmentEntity a){
        return "SELECT COUNT(*) FROM " + TABLE_APPOINTMENT + " WHERE " + COLUMNS_FOR_APPOINTMENT[0]  + " = '" + a.getId() + "'" +
                " AND " + COLUMNS_FOR_APPOINTMENT[1] + " = '" + a.getStartAt().toString() + "'" +
                " AND " + COLUMNS_FOR_APPOINTMENT[2] + " = '" + a.getEndAt().toString() + "'" +
                " AND " + COLUMNS_FOR_APPOINTMENT[3] + " = '" + a.getLocation() + "'" +
                " AND " + COLUMNS_FOR_APPOINTMENT[4] + " = '" + a.getType() + "'" +
                " AND " + COLUMNS_FOR_APPOINTMENT[5] + " = '" + a.getNr() + "';";
    }

    private String selectLastSyncRecordTemplate(){
        return "SELECT * FROM " + TABLE_SYNC + " ORDER BY "+ COLUMNS_FOR_SYNC[0] + " DESC LIMIT 1;";
    }

    private String updateSyncTableTemplate(){
        return "INSERT OR REPLACE INTO " +
                TABLE_SYNC +" ("+ COLUMNS_FOR_SYNC[1]  +", "+ COLUMNS_FOR_SYNC[2] + " ) values ";
    }

    private String truncateAppointmentTemplate(){
        return "DELETE FROM " + TABLE_APPOINTMENT + ";";
    }


    private String deleteFromAppointmentTableWhereTemplate(Appointment appointment){
        return "DELETE FROM " + TABLE_APPOINTMENT + " WHERE "
                + COLUMNS_FOR_APPOINTMENT[1] + " = " + appointment.getStartAt().toString() + " AND "
                + COLUMNS_FOR_APPOINTMENT[2] + " = " + appointment.getEndAt().toString() + " AND "
                + COLUMNS_FOR_APPOINTMENT[3] + " = " + appointment.getStartAt().toString() + ";";

    }

    private ContentValues generateKeyValues(Map<String, String> map){

        ContentValues contentValues = new ContentValues();
        map.forEach(contentValues::put);
        return contentValues;

    }

    private String resetSequenceTemplate(String tableName){
        return "UPDATE sqlite_sequence SET seq = 0 WHERE name = '" + tableName +  "';";
    }


    public SyncEntity selectSyncData(){

        String queryString = selectLastSyncRecordTemplate();
        SyncEntity sync = new SyncEntity();
        try(SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryString, null)) {

            if(cursor.moveToFirst()) {


                sync.setId(cursor.getInt(0));
                sync.setObsLink(cursor.getString(1));
                sync.setLocalDateTime(ZonedDateTime.parse(cursor.getString(2)));
            }

        }

        return sync;
    }

    public void insertOrUpdateTable(String obsLink, ZonedDateTime localDateTime){
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        db.beginTransaction();
        try {


            String sql = updateSyncTableTemplate() +
                    "('" + obsLink + "','" + localDateTime.toString() + "') ";

            System.out.println("Sync update link: " + sql);
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insertAppointments(Map<String,List<AppointmentEntity>> appointments) {

        if(appointments.isEmpty()){
            Log.d(Thread.currentThread().getName() + ": DbService: ", "no appointments to update!");
            return;
        }

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Map.Entry<String, List<AppointmentEntity>> entry : appointments.entrySet()) {


                for (AppointmentEntity a : entry.getValue()) {

                    ContentValues insertValues = newAdded(a);
                    int id = (int) db.insertWithOnConflict(TABLE_APPOINTMENT, null, insertValues, SQLiteDatabase.CONFLICT_IGNORE);

                    if (id < 0 && isModifiable(a)) {

                        int insertInfo = (int) db.insertWithOnConflict(TABLE_NOTIFICATION, null, notificationInfo(a, 0, 1,0), SQLiteDatabase.CONFLICT_IGNORE);
                        int updateInfo = db.update(TABLE_APPOINTMENT, insertValues, "id=?", new String[] {COLUMNS_FOR_APPOINTMENT[0]});
                        Log.d("Update Info: ", String.valueOf(updateInfo));
                    }

                    if(id >= 1 ){
                        ContentValues contentValues = notificationInfo(a, 1, 0,0);
                        db.insertWithOnConflict(TABLE_NOTIFICATION, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                    }
                }
                // execute set of insert for each module
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private ContentValues newAdded(AppointmentEntity a){

        return generateKeyValues(Map.of(
                COLUMNS_FOR_APPOINTMENT[0], a.getId(),
                COLUMNS_FOR_APPOINTMENT[1], a.getStartAt().toString(),
                COLUMNS_FOR_APPOINTMENT[2], a.getEndAt().toString(),
                COLUMNS_FOR_APPOINTMENT[3], a.getLocation(),
                COLUMNS_FOR_APPOINTMENT[4], a.getType(),
                COLUMNS_FOR_APPOINTMENT[5], a.getNr(),
                COLUMNS_FOR_APPOINTMENT[6], a.getModuleID()
        ));
    }

    private boolean isModifiable(AppointmentEntity a){

        String queryString = selectUnmodifiedAppointment(a);

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null) ;


        if(null != cursor && cursor.moveToFirst() && cursor.getCount() > 0){
            Log.d("AUTO SYNC UPDATE: ", String.valueOf(cursor.getCount()));
            return false;
        }

        return true;

    }

    private ContentValues notificationInfo(AppointmentEntity a, int newAdded, int oldChanged, int oldDeleted){
        return generateKeyValues(Map.of(
                COLUMNS_FOR_NOTIFICATION[1],a.getType(),
                COLUMNS_FOR_NOTIFICATION[2],a.getLocation(),
                COLUMNS_FOR_NOTIFICATION[3],a.getModuleID(),
                COLUMNS_FOR_NOTIFICATION[4],String.valueOf(newAdded), // new Added
                COLUMNS_FOR_NOTIFICATION[5],String.valueOf(oldChanged), // oldChanged
                COLUMNS_FOR_NOTIFICATION[6],String.valueOf(oldDeleted), // oldDeleted
                COLUMNS_FOR_NOTIFICATION[7],a.getStartAt().toString() + " " + a.getEndAt().toString())
        );

    }

    // ================ Execute multiple insert statements once ===============
    public void insertModule(Set<ModuleEntity> moduleEntities){

        if(moduleEntities.isEmpty()){
            Log.d(Thread.currentThread().getName() + ": DbService: ", " no modules to update!");
            return;
        }

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = updateModulesTemplate();

            for (ModuleEntity m : moduleEntities) {
                sql += "('" + m.getId() + "','" + m.getName() + "','" + m.getSemester() + "'),";
            }
            // remove last "," and add ";"
            sql = sql.substring(0, sql.length() - 1) + ";";

            System.out.println("LOG: " + sql);
            db.execSQL(sql);
            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
            db.close();
        }
    }


    public void truncateAppointments() {

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        db.beginTransaction();
        try {

            String sql = truncateAppointmentTemplate();
            db.execSQL(sql);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
