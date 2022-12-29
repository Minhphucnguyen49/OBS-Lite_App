package com.hciws22.obslite.today;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;
import com.hciws22.obslite.db.SqLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodayDbService {

    private static final String TABLE_APPOINTMENT = "Appointment";
    public SqLiteHelper sqLiteHelper;

    public TodayDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    public List<Today> getToDay() {
        ArrayList<Today> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_APPOINTMENT;

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (!cursor.moveToFirst()) {
            return Collections.emptyList();
        }

        for (cursor.moveToFirst(); !cursor.isLast(); cursor.moveToNext()){
            Today today = new Today(
                    cursor.getString(6),
                    cursor.getString(4),
                    LocalDateTime.parse(cursor.getString(1)),
                    LocalDateTime.parse(cursor.getString(2)),
                    cursor.getString(3));
        }
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

            return dayOfWeek + " - " + outputString;
        }
        catch (ParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
        }
        return null;
    }
}
