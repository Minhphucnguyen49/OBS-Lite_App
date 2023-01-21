package com.hciws22.obslite.sync;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.AppointmentEntity;
import com.hciws22.obslite.entities.ModuleEntity;
import com.hciws22.obslite.entities.SyncEntity;
import java.time.ZonedDateTime;
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

    private static final int NO_INSERT_POSSIBLE = 0;
    private static final int INSERT_POSSIBLE = 1;
    private static final int UNSUCCESSFUL_DELETE = 0;
    private static final int UNSUCCESSFUL_UPDATE = 0;


    public SqLiteHelper sqLiteHelper;

    public SyncDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    private String insertModulesTemplate(){
        return "INSERT OR IGNORE INTO " +
                TABLE_MODULE +" ("+
                COLUMNS_FOR_MODULE[0] + ", "+
                COLUMNS_FOR_MODULE[1]  +", "+
                COLUMNS_FOR_MODULE[2] + ") VALUES ";
    }
    private String selectModulesTemplate(){
        return "SELECT " + COLUMNS_FOR_MODULE[0] + ", "+ COLUMNS_FOR_MODULE[1] + ", " + COLUMNS_FOR_MODULE[2] + " FROM " + TABLE_MODULE +";";
    }

    private String selectUnmodifiedAppointment(AppointmentEntity a){
        return "SELECT * FROM " + TABLE_APPOINTMENT + " WHERE " + COLUMNS_FOR_APPOINTMENT[6] + " = '" + a.getModuleID() + "';";
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

    private String resetModuleTableTemplate(){
        return "DELETE FROM " + TABLE_MODULE + ";";
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

    private ContentValues notificationInfo(ModuleEntity m, int newAdded, int oldChanged, int oldDeleted){

        return generateKeyValues(Map.of(
                COLUMNS_FOR_NOTIFICATION[1], m.getId(),
                COLUMNS_FOR_NOTIFICATION[2], "DELETED",
                COLUMNS_FOR_NOTIFICATION[3], m.getName(),
                COLUMNS_FOR_NOTIFICATION[4],String.valueOf(newAdded), // new Added
                COLUMNS_FOR_NOTIFICATION[5],String.valueOf(oldChanged), // oldChanged
                COLUMNS_FOR_NOTIFICATION[6],String.valueOf(oldDeleted),// oldDeleted
                COLUMNS_FOR_NOTIFICATION[7],"DELETED"
        ));
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

    private ContentValues generateKeyValues(Map<String, String> map){

        ContentValues contentValues = new ContentValues();
        map.forEach(contentValues::put);
        return contentValues;

    }

    private String resetTableTemplate(String tableName){
        return "DELETE FROM " + tableName + ";";
    }

    public void resetDatabaseTemplate(){
        try (SQLiteDatabase db = sqLiteHelper.getWritableDatabase();){
            db.beginTransaction();

            String[] tablesToReset = { "Appointment", "Notification", "Module"};

            for (String s : tablesToReset) {
                String sql = resetTableTemplate(s);
                db.execSQL(sql);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
        }
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

        try(SQLiteDatabase db = sqLiteHelper.getWritableDatabase()) {
            db.beginTransaction();

            String sql = updateSyncTableTemplate() +
                    "('" + obsLink + "','" + localDateTime.toString() + "') ";

            System.out.println("Sync update link: " + sql);
            db.execSQL(sql);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public void updateAppointments(List<AppointmentEntity> appointmentEntities, Boolean getNotified){

        try(SQLiteDatabase db = sqLiteHelper.getWritableDatabase()){

            db.beginTransaction();
            for (AppointmentEntity a : appointmentEntities) {
                ContentValues insertValues = newAdded(a);
                int id = (int) db.insertWithOnConflict(TABLE_APPOINTMENT, null, insertValues, SQLiteDatabase.CONFLICT_IGNORE);

                if (id >= INSERT_POSSIBLE && getNotified) {
                    ContentValues contentValues = notificationInfo(a, 0, 1, 0);
                    db.insertWithOnConflict(TABLE_NOTIFICATION, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public void initAppointments(Map<String, List<AppointmentEntity>> appointments){

        for (Map.Entry<String, List<AppointmentEntity>> entry : appointments.entrySet()) {
             insertAppointments(entry.getValue(),true);
        }

    }

    public void insertAppointments(List<AppointmentEntity> appointmentEntities, Boolean getNotified){

        try(SQLiteDatabase db = sqLiteHelper.getWritableDatabase()) {
            db.beginTransaction();

            for (AppointmentEntity a : appointmentEntities) {

                ContentValues insertValues = newAdded(a);
                int id = (int) db.insertWithOnConflict(TABLE_APPOINTMENT, null, insertValues, SQLiteDatabase.CONFLICT_IGNORE);

                if (id >= INSERT_POSSIBLE && getNotified) {
                    ContentValues contentValues = notificationInfo(a, 1, 0, 0);
                    db.insertWithOnConflict(TABLE_NOTIFICATION, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                }

                // execute set of insert for each module
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }


    public void deleteInvalidAppointments(AppointmentEntity appointment, Boolean getNotified){

        try(SQLiteDatabase db = sqLiteHelper.getWritableDatabase()){
            db.beginTransaction();
            int deleteCode = db.delete(TABLE_APPOINTMENT, COLUMNS_FOR_APPOINTMENT[6] + "=?", new String[]{appointment.getModuleID()});

            if(deleteCode > UNSUCCESSFUL_DELETE && getNotified){
            ContentValues contentValues = notificationInfo(appointment, 0, 0,1);
            db.insertWithOnConflict(TABLE_NOTIFICATION, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }

            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public List<ModuleEntity> readRegisteredModules(){

        List<ModuleEntity> list = new ArrayList<>();
        String queryString = selectModulesTemplate();

        try(SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryString, null)) {


            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                ModuleEntity old = new ModuleEntity();
                old.setId(cursor.getString(0));
                old.setName(cursor.getString(1));
                old.setSemester(cursor.getString(2));
                list.add(old);
            }
        }

        return list;

    }

    public void deleteInvalidModules(Map<String,List<AppointmentEntity>> modules, List<ModuleEntity> oldList){

        try(SQLiteDatabase db = sqLiteHelper.getWritableDatabase()){
            db.beginTransaction();
            for (int i = 0; i < oldList.size(); i++){
                if(!modules.containsKey(oldList.get(i).getId())){
                    int deleteCode = db.delete(TABLE_MODULE, COLUMNS_FOR_MODULE[0] + "=?", new String[]{oldList.get(i).getId()});

                    if (deleteCode > UNSUCCESSFUL_DELETE) {
                        db.delete(TABLE_APPOINTMENT, COLUMNS_FOR_APPOINTMENT[6] + "=?", new String[]{oldList.get(i).getId()});
                        ContentValues contentValues = notificationInfo(oldList.get(i), 0, 0, 1);
                        db.insertWithOnConflict(TABLE_NOTIFICATION, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                    }
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }


    public List<AppointmentEntity> readRegisteredAppointments(AppointmentEntity a){

        List<AppointmentEntity> list = new ArrayList<>();
        String queryString = selectUnmodifiedAppointment(a);

        try(SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null)) {

            AppointmentEntity old = new AppointmentEntity();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                old.setStartAt(ZonedDateTime.parse(cursor.getString(1)));
                old.setEndAt(ZonedDateTime.parse(cursor.getString(2)));
                old.setModuleID(cursor.getString(6));
                list.add(old);
            }
        }

        return list;
    }

    // ================ Execute multiple insert statements once ===============
    public void insertOrUpdateModule(Set<ModuleEntity> moduleEntities){

        if(moduleEntities.isEmpty()){
            Log.d(Thread.currentThread().getName() + ": DbService: ", " no modules to update!");
            return;
        }

        try(SQLiteDatabase db = sqLiteHelper.getWritableDatabase()){

            db.beginTransaction();

            String sql = resetModuleTableTemplate();
            db.execSQL(sql);

            sql = insertModulesTemplate();

            for (ModuleEntity m : moduleEntities) {
                sql += "('" + m.getId() + "','" + m.getName() + "','" + m.getSemester() + "'),";
            }
            // remove last "," and add ";"
            sql = sql.substring(0, sql.length() - 1) + ";";

            System.out.println("LOG: " + sql);
            db.execSQL(sql);

            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public void truncateAppointments() {


        try ( SQLiteDatabase db = sqLiteHelper.getWritableDatabase()){
            db.beginTransaction();
            String sql = truncateAppointmentTemplate();
            db.execSQL(sql);

            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }
}
