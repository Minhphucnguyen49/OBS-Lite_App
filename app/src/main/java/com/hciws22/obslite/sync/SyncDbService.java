package com.hciws22.obslite.sync;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.AppointmentEntity;
import com.hciws22.obslite.entities.ExtraInfoEntity;
import com.hciws22.obslite.entities.ModuleEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SyncDbService {

    private static final String TABLE_MODULE = "Module";
    private static final String[] COLUMNS_FOR_MODULE = {"id", "name", "semester"};

    private static final String TABLE_APPOINTMENT = "Appointment";
    private static final String[] COLUMNS_FOR_APPOINTMENT = {"startAt", "endAt", "location", "type", "nr", "moduleID"};
    private static final String TABLE_EXTRA_INFO = "Extra";
    private static final String[] COLUMNS_FOR_EXTRA_INFO = { "nr", "id", "percentage", "note"};

    public SqLiteHelper sqLiteHelper;

    public SyncDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    private String insertModulesTemplate(){
        return "insert or replace into " +
                TABLE_MODULE +" ("+
                COLUMNS_FOR_MODULE[0] + ", "+
                COLUMNS_FOR_MODULE[1]  +", "+
                COLUMNS_FOR_MODULE[2] + ") values ";
    }

    private String insertAppointmentTemplate(){
        return "insert or replace into " +
                TABLE_APPOINTMENT +" ("+
                COLUMNS_FOR_APPOINTMENT[0] + ", "+ COLUMNS_FOR_APPOINTMENT[1]  +", "+
                COLUMNS_FOR_APPOINTMENT[2] + ", "+ COLUMNS_FOR_APPOINTMENT[3] + ", "+
                COLUMNS_FOR_APPOINTMENT[4] +", "+ COLUMNS_FOR_APPOINTMENT[5] +" ) values ";
    }

    private String insertExtraInfoTemplate() {
        return "insert or replace into " +
                TABLE_EXTRA_INFO + " (" +
                COLUMNS_FOR_EXTRA_INFO[0] + ", " + COLUMNS_FOR_EXTRA_INFO[1] + ", " +
                COLUMNS_FOR_EXTRA_INFO[2] + ", " + COLUMNS_FOR_EXTRA_INFO[3] + " ) values ";
    }

    private String selectTemplate(String tableName){
    return "select * from " + tableName + ";";
}

    public void insertAppointments(Map<String,List<AppointmentEntity>> appointments) {

        if(appointments.isEmpty()){
            Log.d(Thread.currentThread().getName() + ": DbService: ", "no records!");
            return;
        }

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Map.Entry<String, List<AppointmentEntity>> entry : appointments.entrySet()) {

                String sql = insertAppointmentTemplate();
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

        if(moduleEntities.isEmpty()) return;

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = insertModulesTemplate();

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
