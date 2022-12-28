package com.hciws22.obslite.sync;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.hciws22.obslite.today.Today;
import com.hciws22.obslite.todo.Todo;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.AppointmentEntity;
import com.hciws22.obslite.entities.ModuleEntity;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
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

    public ArrayList<Todo> getToDo(){

        ArrayList<Todo> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_APPOINTMENT;

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            //loop through the cursor and create new appointment object. Put them into the returnList
            do{
                String startAt = cursor.getString(1);
                String endAt = cursor.getString(2);

                String dayOfWeek = getDayOfWeek(startAt);

                String date = getDateInFormat(startAt, dayOfWeek);

                String location = cursor.getString(3);
                String type = cursor.getString(4);

                String name = cursor.getString(6);
                name = name.substring(0, name.lastIndexOf(' '));
                name += cursor.getString(5);//name += nr;

                int index = startAt.indexOf('T');
                startAt = startAt.substring(index+1);
                endAt = endAt.substring(index+1);
                String dateString = startAt + "-" + endAt;

                Todo todo = new Todo(name ,type," ",date,dateString, location );
                returnList.add(todo);

            } while(cursor.moveToNext());
        }else{
            System.out.println("Error by loading data!!!!!!!!!!!!!");
        }
        //close both cursor and the db
        cursor.close();
        db.close();
        return returnList;

    }

    public ArrayList<Today> getToDay() {
        ArrayList<Today> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_APPOINTMENT;

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            //loop through the cursor and create new appointment object. Put them into the returnList
            do{
                String startAt = cursor.getString(1);
                String endAt = cursor.getString(2);

                String dayOfWeek = getDayOfWeek(startAt);

                String date = getDateInFormat(startAt, dayOfWeek);

                String location = cursor.getString(3);
                String type = cursor.getString(4);

                String name = cursor.getString(6);
                name = name.substring(0, name.lastIndexOf(' '));

                int index = startAt.indexOf('T');
                startAt = startAt.substring(index+1);
                endAt = endAt.substring(index+1);
                String time = startAt + "-" + endAt;

                Today today = new Today(name, type, date,time, location );
                returnList.add(today);

            } while(cursor.moveToNext());
        }else{
            System.out.println("Error by loading data!!!!!!!!!!!!!");
        }
        //close both cursor and the db
        cursor.close();
        db.close();
        return returnList;
    }
    private String getDayOfWeek(String date1) {
        String dateString = date1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        String dayOfWeek = dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        return dayOfWeek;
    }

    @Nullable
    private String getDateInFormat (String date1, String dayOfWeek) {
        try {
            // Parse the input date string
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date datelocal = inputFormat.parse(date1.substring(0, 10));
            // Format the date using the output format
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
            String outputString = outputFormat.format(datelocal);
            
            return dayOfWeek.substring(0,3) + " - " + outputString;
        }
        catch (ParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
        }
        return null;
    }



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
         /*
        ArrayList<TodoView> moduleList = new ArrayList<>();

        moduleList.add( new TodoView("HCI #4", "Practical", "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));

        moduleList.add( new TodoView("BS #5", "Practical", "20%",
                "Mon 20.11.2022", "10:15-11:45","D15/01.07."));
        moduleList.add( new TodoView("TI #6", "Practical", "20%",
                "Mon 20.11.2022", "08:30-10:00","D15/01.07."));
        moduleList.add( new TodoView("GDV #4", "Practical", "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));
        moduleList.add( new TodoView("PG2 #4", "Practical", "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));
        moduleList.add( new TodoView("DB #5", "Practical", "20%",
                "Mon 20.11.2022", "08:30-10:00","D15/01.07."));
        moduleList.add( new TodoView("TI #7", "Practical", "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));
        moduleList.add( new TodoView("MPS #5", "Practical", "20%",
                "Mon 20.11.2022", "10:15-11:45","D15/01.07."));
        return moduleList;
                */



}
