package com.hciws22.obslite.sync;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.jobs.ResponseService;
import com.hciws22.obslite.jobs.SocketConnectionService;
import com.hciws22.obslite.notification.NotificationController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public class SyncController {

    private final ResponseService responseService;
    private final FileService fileService;
    private final SyncDbService syncDbService;
    private final NotificationController notificationController;

    public SyncController(SqLiteHelper sqLiteHelper, Context context) {
        responseService = new ResponseService();
        fileService = new FileService();
        syncDbService = new SyncDbService(sqLiteHelper);
        notificationController = new NotificationController(sqLiteHelper, context);
    }


    public String updateSyncLabel(String editText){

        boolean hasFailed = manualSynchronize(editText, true);

        return hasFailed ? "could not update obs link.\nCheck your internet connection" : "Last sync: just now";


    }


    public boolean fetchDataFromOBS(String obsLink)  {

        try {
            responseService.getDataFromObs(obsLink);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("fetch: ", "fetch data has failed");
            return true;
        }
        Log.d("fetch: ", "fetch data has succeeded");
        return false;

    }

    public boolean autoSynchronize(){

        String obsLink = syncDbService.selectSyncData().getObsLink();

        if(obsLink.isEmpty()){
            Log.d("auto sync","No data for auto sync");
            return false;
        }

        return manualSynchronize(obsLink, false);

    }

    public boolean checkUrlForm(String url){
        return responseService.checkUrl(url);
    }

    public boolean manualSynchronize(String url, boolean isNewLink){

        //IF CHECKURL FALSE -> RETURN FALSE
        if(!SocketConnectionService.isConnected()){
            Log.d("manual sync: ", "No connection");
            return false;
        }


        if(!checkUrlForm(url)){
            Log.d("manual sync: ", "not succeeded");
            return true;
        };


        if(fetchDataFromOBS(url)){
            Log.d("manual sync: ", "fetch not succeeded");
            return true;

        }

        if(isNewLink){
            syncDbService.truncateAppointments();
        }

        syncDbService.insertOrUpdateTable(url, ZonedDateTime.now());
        updateData();
        return false;

    }

    private void updateData(){

        List<String> filteredList = responseService.getFilteredList();
        Log.d(Thread.currentThread().getName() + ": synccontroller", "Converting");

        Log.d(Thread.currentThread().getName() + ": synccontroller", "start Converting");
        fileService.convertToModule(filteredList);


        Log.d(Thread.currentThread().getName() + ": FilteredList Size: ", String.valueOf(responseService.getFilteredList().size()));

        fileService.convertOBStoEntityRepresentation();
        syncDbService.insertModule(fileService.getModules());
        syncDbService.insertAppointments(fileService.getAllAppointments());
    }

}
