package com.hciws22.obslite.sync;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.AppointmentEntity;
import com.hciws22.obslite.entities.ModuleEntity;
import com.hciws22.obslite.entities.SyncEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SyncDbService {

    private static final String TABLE_MODULE = "Module";
    private static final String[] COLUMNS_FOR_MODULE = {"id", "name", "semester"};

    private static final String TABLE_APPOINTMENT = "Appointment";
    private static final String[] COLUMNS_FOR_APPOINTMENT = {"id", "startAt", "endAt", "location", "type", "nr", "moduleID"};

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
                TABLE_APPOINTMENT +" ("+  COLUMNS_FOR_APPOINTMENT[0] + ", " +
                COLUMNS_FOR_APPOINTMENT[1] + ", "+ COLUMNS_FOR_APPOINTMENT[2]  +", "+
                COLUMNS_FOR_APPOINTMENT[3] + ", "+ COLUMNS_FOR_APPOINTMENT[4] + ", "+
                COLUMNS_FOR_APPOINTMENT[5] +", "+ COLUMNS_FOR_APPOINTMENT[6] +" ) values ";
    }

    private String selectLastSyncRecordTemplate(){
        return "SELECT * FROM " + TABLE_SYNC + " ORDER BY "+ COLUMNS_FOR_SYNC[0] + " DESC LIMIT 1;";
    }

    private String updateSyncTableTemplate(){
        return "insert or replace into " +
                TABLE_SYNC +" ("+ COLUMNS_FOR_SYNC[1]  +", "+ COLUMNS_FOR_SYNC[2] + " ) values ";
    }

    private String truncateAppointmentTemplate(){
        return "DELETE FROM " + TABLE_APPOINTMENT + ";";
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

            int id = 0;


            for (Map.Entry<String, List<AppointmentEntity>> entry : appointments.entrySet()) {

                StringBuilder sql2 = new StringBuilder(updateAppointmentTemplate());
                for (AppointmentEntity a : entry.getValue()) {
                    sql2.append("('").append(id++).append("','").append(a.getStartAt()).append("','").append(a.getEndAt()).append("','").append(a.getLocation()).append("','").append(a.getType()).append("','").append(a.getNr()).append("','").append(a.getModuleID()).append("'),");
                }
                // execute set of insert for each module

                sql2 = new StringBuilder(sql2.substring(0, sql2.length() - 1) + ";");
                db.execSQL(sql2.toString());

                System.out.println("Appointment: " + sql2);
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
