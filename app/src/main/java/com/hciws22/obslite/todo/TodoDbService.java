package com.hciws22.obslite.todo;

import static java.time.temporal.TemporalAdjusters.previousOrSame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.ExtraInfoEntity;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TodoDbService {

    private static final String TABLE_APPOINTMENT = "Appointment";
    private final SqLiteHelper sqLiteHelper;
    private static final String[] TO_DO = {"Ü", "P", "LN", "EX", "E"};

    private static final String TABLE_EXTRA_INFO = "Extra";
    private static final String[] COLUMNS_FOR_EXTRA_INFO = { "name", "percentage", "note"};
    private static final String[] COLUMNS_FOR_APPOINTMENT = {"startAt", "endAt", "location", "nr", "moduleID"};
    private String insertExtraInfoTemplate() {
        return "insert or replace into " +
                TABLE_EXTRA_INFO + " (" +
                COLUMNS_FOR_EXTRA_INFO[0] + ", " +
                COLUMNS_FOR_EXTRA_INFO[1] + ", " +
                COLUMNS_FOR_EXTRA_INFO[2] + " ) values ";
    }

    private String updateExtraInfoTemplate(String percentage, String name){
        return " UPDATE " + TABLE_EXTRA_INFO
                + " SET " + COLUMNS_FOR_EXTRA_INFO[1] + " = '" + percentage
                + "' WHERE " + COLUMNS_FOR_EXTRA_INFO[0] + " = '" + name + "';";
    }

    public String selectExamsPattern(){
        return "SELECT " +
                COLUMNS_FOR_APPOINTMENT[0] + "," +
                COLUMNS_FOR_APPOINTMENT[1] + "," +
                COLUMNS_FOR_APPOINTMENT[2] + "," +
                COLUMNS_FOR_APPOINTMENT[3] + "," +
                COLUMNS_FOR_APPOINTMENT[4] +

                " FROM " + TABLE_APPOINTMENT + " WHERE " +
                "( type = '" + TO_DO[2] + "'" +
                " OR type = '" + TO_DO[3] +  "' )" + ";";
    }
    private final Set<ExtraInfoEntity> extraInfo = new HashSet<>();

    public TodoDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    public String selectTodoPattern(ZonedDateTime future){
        return "SELECT " +
                COLUMNS_FOR_APPOINTMENT[0] + "," +
                COLUMNS_FOR_APPOINTMENT[1] + "," +
                COLUMNS_FOR_APPOINTMENT[2] + "," +
                COLUMNS_FOR_APPOINTMENT[3] + "," +
                COLUMNS_FOR_APPOINTMENT[4] +

                " FROM " + TABLE_APPOINTMENT + " WHERE " +
                "( type = '" + TO_DO[0] + "'" +
                " OR type = '" + TO_DO[1] +  "'" +
                " OR type = '" + TO_DO[2] +  "'" +
                " OR type = '" + TO_DO[3] +  "'" +
                " OR type = '" + TO_DO[4] +  "')" + " AND " +
                COLUMNS_FOR_APPOINTMENT[0] +
                " BETWEEN '" + ZonedDateTime.now(ZoneId.of("Europe/Berlin")).with(previousOrSame(DayOfWeek.MONDAY)) + "'" +
                " AND '" + future.toLocalDate() + "'" +
                " ORDER BY " + COLUMNS_FOR_APPOINTMENT[0] + ";";
    }

    public String selectTodoAll(){
        return "SELECT " +
                COLUMNS_FOR_APPOINTMENT[0] + "," +
                COLUMNS_FOR_APPOINTMENT[1] + "," +
                COLUMNS_FOR_APPOINTMENT[2] + "," +
                COLUMNS_FOR_APPOINTMENT[3] + "," +
                COLUMNS_FOR_APPOINTMENT[4] +

                " FROM " + TABLE_APPOINTMENT + " WHERE " +
                "( type = '" + TO_DO[0] + "'" +
                " OR type = '" + TO_DO[1] +  "'" +
                " OR type = '" + TO_DO[2] +  "'" +
                " OR type = '" + TO_DO[3] +  "'" +
                " OR type = '" + TO_DO[4] +  "');";
    }
    public List<Todo> selectCurrentWeek(){
        String queryString = selectTodoPattern(ZonedDateTime.now(ZoneId.of("Europe/Berlin")).with(previousOrSame(DayOfWeek.MONDAY)).plusDays(14));
        return selectTodoAppointmentsPattern(queryString);
    }

    public List<Todo> selectAllWeek(){
        String queryString = selectTodoAll();
        return selectTodoAppointmentsPattern(queryString);
    }
    public List<Todo> selectExams(){
        return selectTodoAppointmentsPattern(selectExamsPattern());
    }

    public List<Todo> selectTodoAppointmentsPattern(String queryString) {
        List<Todo> todoList = new ArrayList<>();
        // close both cursor and the db.
        // Try-with-resources will always close all kinds of connection
        // after the Try-block has reached his end
        try(SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryString, null)) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Todo todo = new Todo(
                        getName(cursor.getString(4)) + " " + cursor.getString(3),
                        "",
                        getDate(cursor.getString(0)),
                        getTimePeriod(cursor.getString(0), cursor.getString(1)),
                        cursor.getString(2));
                todoList.add(todo);
            }
        }
        return todoList;
    }

    public String selectExtraPattern(){
        return "SELECT " +
                COLUMNS_FOR_EXTRA_INFO[0] + ", " +
                COLUMNS_FOR_EXTRA_INFO[1] +
                " FROM " + TABLE_EXTRA_INFO + ";";
    }

    public List<Todo> selectExtra() {
        List<Todo> todoList = new ArrayList<>();
        String queryString = selectExtraPattern();

        // close both cursor and the db.
        // Try-with-resources will always close all kinds of connection
        // after the Try-block has reached his end
        try(SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryString, null)) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Todo todo = new Todo(
                        cursor.getString(0),
                        cursor.getString(1),
                        "",
                        "",
                        "");
                todoList.add(todo);
            }
        }
        return todoList;
    }


    private String getName(String name){
        return name.substring(0,name.lastIndexOf(' '));
    }

    private String getTimePeriod(String startAt, String endAt) {
        ZonedDateTime localDateTime1 = parseFormat(startAt);
        ZonedDateTime localDateTime2 = parseFormat(endAt);

        return localDateTime1.toLocalTime().plusSeconds(localDateTime1.getOffset().getTotalSeconds()) + " - " + localDateTime2.toLocalTime().plusSeconds(localDateTime2.getOffset().getTotalSeconds());

    }

    private String getDate(String dateToString) {
        ZonedDateTime localDateTime = parseFormat(dateToString);

        return localDateTime
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.getDefault()).substring(0,2) + " " + localDateTime.toLocalDate().toString().replace("-",".");
    }

    private ZonedDateTime parseFormat(String dateToString){
        return ZonedDateTime.parse(dateToString);
    }

    //================ Execute multiple insert statements once ===============
    public void insertExtraInfo(List<Todo> extraInfoEntities){

        if(extraInfoEntities.isEmpty()){
            return;
        }

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = insertExtraInfoTemplate();

            String percentage = "0";
            String notes = "";
            for (Todo todo : extraInfoEntities) {
                sql += "('" + todo.getName() + "','" + percentage + "','" + notes + "'),";
            }
            // remove last "," and add ";"---
            sql = sql.substring(0, sql.length() - 1) + ";";

            // System.out.println("LOG: " + sql);
            db.execSQL(sql);
            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateExtra(Todo module){
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = updateExtraInfoTemplate(module.getPercentage(), module.getName());

            db.execSQL(sql);
            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
            db.close();
        }
    }


    /*
    void generateInternalEntityRepresentation(){
        extraInfo.clear();

        //obsList.forEach(obsItem -> extraInfo
        //                        .add(ExtraInfoEntity.build1(obsItem.getId(), obsItem.getAppointment().getNr()))
        //                );

        fileService..forEach( (key,value) -> {
            for (AppointmentEntity appointment : value) {
                if (appointment.getType().contains("P") || appointment.getType().contains("Ü") || appointment.getType().contains("LN")) {
                    extraInfo.add(ExtraInfoEntity.build1(key, appointment.getNr()));
                }
            }
        });
        //Log.d("appointments: ", appointments.toString());

        //        obsList.stream()
        //                .forEach(obsItem -> extraInfo
        //                        .add(ExtraInfoEntity.build(obsItem))
        //                );


    }

     */

}
