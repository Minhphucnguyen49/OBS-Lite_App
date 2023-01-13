package com.hciws22.obslite.setting;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.SyncEntity;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

public class SettingsDbService {

    private static final String TABLE_SYNC = "Sync";
    private static final String[] COLUMNS_FOR_SYNC = { "id", "obsLink", "syncTime" };

    public SqLiteHelper sqLiteHelper;

    public SettingsDbService(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    private String selectLastSyncRecordTemplate(){
        return "SELECT * FROM " + TABLE_SYNC + " ORDER BY "+ COLUMNS_FOR_SYNC[0] + " DESC LIMIT 1";
    }

    public SyncEntity selectSyncData() {

        String queryString = selectLastSyncRecordTemplate();
        SyncEntity sync = new SyncEntity();
        try (SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)) {

            if (cursor.moveToFirst()) {


                sync.setId(cursor.getInt(0));
                sync.setObsLink(cursor.getString(1));
                sync.setLocalDateTime(ZonedDateTime.parse(cursor.getString(2)));

            }

        }

        return sync;
    }

}
