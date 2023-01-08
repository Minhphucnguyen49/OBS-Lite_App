package com.hciws22.obslite.sync;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.jobs.ResponseService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class SyncController {

    ResponseService responseService;
    FileService fileService;
    SyncDbService syncDbService;

    public SyncController(SqLiteHelper sqLiteHelper) {
        responseService = new ResponseService();
        fileService = new FileService();
        syncDbService = new SyncDbService(sqLiteHelper);
    }

    public void init(View sendbtn, EditText editText, TextView syncTime) {

        sendbtn.setOnClickListener(view -> {
            manualSynchronize(editText.getText().toString());
            String label = "Last sync: just now";
            syncTime.setText(label);
        }

        );
    }




    public boolean fetchDataFromOBS(String obsLink)  {

        try {
            responseService.getDataFromObs(obsLink);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("fetch", "fetch data has failed");
            return true;
        }
        Log.d("fetch", "fetch data has succeeded");
        return false;

    }

    public boolean autoSynchronize(){

        String obsLink = syncDbService.selectSyncData().getObsLink();

        if(obsLink.isEmpty()){
            Log.d("auto sync","No data for auto sync");
            return false;
        }

        return manualSynchronize(obsLink);

    }
    public boolean manualSynchronize(String url){

        //SEHR WICHTIG: IF CHECKURL FALSE -> RETURN FALSE
        if(!responseService.checkUrl(url)){
            Log.d("manual sync:", "not succeeded");
            return false;
        };


        if(fetchDataFromOBS(url)){
            Log.d("manual sync:", "fetch not succeeded");
            return true;

        }

        syncDbService.insertOrUpdateTable(url, LocalDateTime.now());
        updateData();
        return false;

    }

    private void updateData(){

        List<String> filteredList = responseService.getFilteredList();
        Log.d(Thread.currentThread().getName() + ": synccontroller", "Converting");
        synchronized (filteredList) {
            Log.d(Thread.currentThread().getName() + ": synccontroller", "start Converting");
            fileService.convertToModule(filteredList);
        }

        Log.d(Thread.currentThread().getName() + ": FilteredList Size: ", String.valueOf(responseService.getFilteredList().size()));

        fileService.convertOBStoEntityRepresentation();
        syncDbService.insertModule(fileService.getModules());
        syncDbService.insertAppointments(fileService.getAllAppointments());
    }

}
