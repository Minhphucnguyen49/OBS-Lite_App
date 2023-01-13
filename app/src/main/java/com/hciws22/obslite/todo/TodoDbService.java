package com.hciws22.obslite.todo;

import static java.time.temporal.TemporalAdjusters.previousOrSame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.ExtraInfoEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public String selectTodoPattern(LocalDate future){
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
                " BETWEEN '" + LocalDate.now().with(previousOrSame(DayOfWeek.MONDAY)) + "'" +
                " AND '" + future + "'" +
                " ORDER BY " + COLUMNS_FOR_APPOINTMENT[0] + ";";
    }
    public List<Todo> selectTodoTwoWeek(){
        String queryString = selectTodoPattern(LocalDate.now().plusDays(14));
        return selectTodoAppointmentsPattern(queryString);
    }

    public List<Todo> selectTodoOneWeek(){
        String queryString = selectTodoPattern(LocalDate.now().plusDays(7));
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


    private String getName(String name){
        return name.substring(0,name.lastIndexOf(' '));
    }

    private String getTimePeriod(String startAt, String endAt) {
        LocalDateTime localDateTime1 = parseFormat(startAt);
        LocalDateTime localDateTime2 = parseFormat(endAt);

        return localDateTime1.toLocalTime() + " - " + localDateTime2.toLocalTime();

    }

    private String getDate(String dateToString) {
        LocalDateTime localDateTime = parseFormat(dateToString);

        return localDateTime
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.getDefault()).substring(0,3) + " " + localDateTime.toLocalDate().toString().replace("-",".");
    }

    private LocalDateTime parseFormat(String dateToString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(dateToString, formatter);
    }

    //================ Execute multiple insert statements once ===============
    public void insertExtraInfo(List<Todo> extraInfoEntities){

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            String sql = insertExtraInfoTemplate();

            for (Todo todo : extraInfoEntities) {
                sql += "('" + todo.getName() + "','" + " " + "','" + " " + "'),";
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
