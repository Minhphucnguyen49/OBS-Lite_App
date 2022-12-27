package com.hciws22.obslite.sync;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hciws22.obslite.TodoView;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.AppointmentEntity;
import com.hciws22.obslite.entities.ModuleEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class SyncDbService {

    private static final String TABLE_MODULE = "Module";
    private static final String[] COLUMNS_FOR_MODULE = {"id", "name", "semester"};

    private static final String TABLE_APPOINTMENT = "Appointment";
    private static final String[] COLUMNS_FOR_APPOINTMENT = {"startAt", "endAt", "location", "type", "nr", "moduleID","percentage", "note"};

    /*
    private static final String TABLE_EXTRA_INFO = "ExtraInfo";
    private static final String[] COLUMNS_FOR_EXTRA_INFO = { "nr", "moduleID", "percentage", "note"};

     */
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

/*
    private String insertExtraInfoTemplate(){
        return "insert or replace into " +
                TABLE_EXTRA_INFO +" ("+
                COLUMNS_FOR_EXTRA_INFO[0] + ", "+ COLUMNS_FOR_EXTRA_INFO[1]  +", "+
                COLUMNS_FOR_EXTRA_INFO[2] + ", "+ COLUMNS_FOR_EXTRA_INFO[3]  +" ) values ";
    }


 */
    private String selectTemplate(String tableName){
    return "select * from " + tableName + ";";
}

    public void insertAppointments(Map<String,List<AppointmentEntity>> appointments) {


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
        }finally {
            db.endTransaction();
            db.close();
        }
    }
    // ================ Execute multiple insert statements once ===============
    public void insertModule(Set<ModuleEntity> moduleEntities){

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = insertModulesTemplate();

            for (ModuleEntity m : moduleEntities) {
                sql += "('" + m.getId() + "','" + m.getName() + "','" + m.getSemester() + "'),";
            }
            // remove last "," and add ";"
            sql = sql.substring(0, sql.length() - 1) + ";";

            // System.out.println("LOG: " + sql);
            db.execSQL(sql);
            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
            db.close();
        }
    }

    public ArrayList<TodoView> getToDo(){

        ArrayList<TodoView> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_APPOINTMENT;

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            //loop through the cursor and create new appointment object. Put them into the returnList
            do{
                String date1 = cursor.getString(1);
                String date2 = cursor.getString(2);

                String dateString = date1;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
                String dayOfWeek = dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

                String location = cursor.getString(3);
                String type = cursor.getString(4);

                String name = cursor.getString(6);
                name = name.substring(0, name.lastIndexOf(' '));
                name += cursor.getString(5);//name += nr;

                int index = date1.indexOf('T');
                date1 = date1.substring(index+1);
                date2 = date2.substring(index+1);
                dateString = date1 + "-" + date2;

                TodoView todo = new TodoView(name ,type," ",dayOfWeek.substring(0,3),dateString, location );
                returnList.add(todo);

            } while(cursor.moveToNext());
        }else{

        }
     /*
        ArrayList<ModuleView> moduleList = new ArrayList<>();

        moduleList.add( new ModuleView("HCI #4", "Practical", "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));

        moduleList.add( new ModuleView("BS #5", "Practical", "20%",
                "Mon 20.11.2022", "10:15-11:45","D15/01.07."));
        moduleList.add( new ModuleView("TI #6", "Practical", "20%",
                "Mon 20.11.2022", "08:30-10:00","D15/01.07."));
        moduleList.add( new ModuleView("GDV #4", "Practical", "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));
        moduleList.add( new ModuleView("PG2 #4", "Practical", "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));
        moduleList.add( new ModuleView("DB #5", "Practical", "20%",
                "Mon 20.11.2022", "08:30-10:00","D15/01.07."));
        moduleList.add( new ModuleView("TI #7", "Practical", "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));
        moduleList.add( new ModuleView("MPS #5", "Practical", "20%",
                "Mon 20.11.2022", "10:15-11:45","D15/01.07."));
                */
        return returnList;
    }
    /*
                todo.setName(name);
                date1.substring(date1.indexOf("T"));
                date2.substring(date2.indexOf("T"));
                todo.setTime(date1 + "-" + date2);
                todo.setDate(dayOfWeek.substring(0,3));
                todo.setLocation(location);
                todo.setType(type);
                todo.setNr(nr);
                todo.setPercentage("");
                todo.setNote(" ");

                 */
    /* ================ Execute multiple insert statements once ===============
    public void insertExtraInfo(Set<ExtraInfoEntity> extraInfoEntities){

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = insertExtraInfoTemplate();

            for (ExtraInfoEntity e : extraInfoEntities) {
                sql += "('" + m.getId() + "','" + m.getName() + "','" + m.getSemester() + "'),";
            }
            // remove last "," and add ";"
            sql = sql.substring(0, sql.length() - 1) + ";";

            // System.out.println("LOG: " + sql);
            db.execSQL(sql);
            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
            db.close();
        }
    }

     */

    /*
    // ================ Execute Single INSERT statements ===================

    public void insertModule(Set<ModuleEntity> moduleEntities){

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        db.beginTransaction();
        try {

            for (ModuleEntity m : moduleEntities) {
                String values = "('" + m.getId() + "','" + m.getName() + "','" + m.getSemester() + "');";
                db.execSQL(insertModulesTemplate() + values);

            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            db.close();
        }

    }

     */



}
