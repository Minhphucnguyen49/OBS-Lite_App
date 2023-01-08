package com.hciws22.obslite.sync;

import android.util.Log;
import android.view.View;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.jobs.ResponseService;

import java.io.IOException;
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

    public void init(View sendbtn) {

        //fetchDataFromOBS();
        sendbtn.setOnClickListener(view -> manualSynchronize());

    }


    public boolean fetchDataFromOBS()  {

        try {
            responseService.getDataFromObs();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("fetch", "fetch data has failed");
            return true;
        }
        Log.d("fetch", "fetch data has succeeded");
        return false;

    }
    public boolean manualSynchronize(){

        if(fetchDataFromOBS()){
            return true;
        }

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

        return false;

    }

}
