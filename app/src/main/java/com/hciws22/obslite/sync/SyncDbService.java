package com.hciws22.obslite.sync;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.AppointmentEntity;
import com.hciws22.obslite.entities.ExtraInfoEntity;
import com.hciws22.obslite.entities.ModuleEntity;
import com.hciws22.obslite.entities.SyncEntity;
import com.hciws22.obslite.todo.Todo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SyncDbService {

    private static final String TABLE_MODULE = "Module";
    private static final String[] COLUMNS_FOR_MODULE = {"id", "name", "semester"};

    private static final String TABLE_APPOINTMENT = "Appointment";
    private static final String[] COLUMNS_FOR_APPOINTMENT = {"startAt", "endAt", "location", "type", "nr", "moduleID"};

    private static final String TABLE_SYNC = "Sync";
    private static final String[] COLUMNS_FOR_SYNC = {"id", "obsLink", "syncTime"};

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

    private String updateAppointmentTemplate(){
        return "insert or replace into " +
                TABLE_APPOINTMENT +" ("+
                COLUMNS_FOR_APPOINTMENT[0] + ", "+ COLUMNS_FOR_APPOINTMENT[1]  +", "+
                COLUMNS_FOR_APPOINTMENT[2] + ", "+ COLUMNS_FOR_APPOINTMENT[3] + ", "+
                COLUMNS_FOR_APPOINTMENT[4] +", "+ COLUMNS_FOR_APPOINTMENT[5] +" ) values ";
    }

    private String selectLastSyncRecordTemplate(){
        return "SELECT * FROM " + TABLE_SYNC + " ORDER BY "+ COLUMNS_FOR_SYNC[0] + " DESC LIMIT 1";
    }


    private String updateSyncTableTemplate(){
        return "insert or replace into " +
                TABLE_SYNC +" ("+ COLUMNS_FOR_SYNC[1]  +", "+ COLUMNS_FOR_SYNC[2] + " ) values ";
    }

    public SyncEntity selectSyncData(){

        String queryString = selectLastSyncRecordTemplate();
        SyncEntity sync;
        try(SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryString, null)) {

            cursor.moveToFirst();
            SyncEntity syncEntity = new SyncEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        LocalDateTime.parse(cursor.getString(2)));
             sync = syncEntity;

        }

        return sync;

    }

    public void insertOrUpdateTable(String obsLink, LocalDateTime localDateTime){
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

                String sql = updateAppointmentTemplate();
                for (AppointmentEntity a : entry.getValue()) {

                    sql += "('" + a.getStartAt() + "','" + a.getEndAt() + "','" +
                            a.getLocation() + "','" + a.getType() + "','" +
                            a.getNr() + "','" + a.getModuleID() + "'),";
                }
                // execute set of insert for each module
                sql = sql.substring(0,sql.length()-1) + ";";
                db.execSQL(sql);

            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
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






}
