package com.hciws22.obslite.sync;

import android.view.View;

import com.hciws22.obslite.Todo;
import com.hciws22.obslite.db.SqLiteHelper;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class SyncController {

    ResponseService responseService;
    FileService fileService;
    SyncDbService syncDbService;
    Boolean isAvailable = true;

    public SyncController(SqLiteHelper sqLiteHelper) {
        responseService = new ResponseService();
        fileService = new FileService();
        syncDbService = new SyncDbService(sqLiteHelper);
    }


    public void init(View sendbtn) {

        fetchDataFromOBS();

        sendbtn.setOnClickListener(this::manualSynchronize);

    }
    public ArrayList<Todo> getToDo(){
        return syncDbService.getToDo();
    }

    public void fetchDataFromOBS()  {

        try {
            responseService.getDataFromObs();
        } catch (IOException e) {
            String errorMessage = "No internet connection";
        }

    }
    public void manualSynchronize(View e){

        // prevent multiple clicks
        if(isAvailable) {
            isAvailable = false;
            fetchDataFromOBS();

            synchronized (responseService.getFilteredList()) {
                fileService.convertToModule(responseService.getFilteredList());
                isAvailable = true;
            }

            fileService.generateEntityRepresentation();
            syncDbService.insertModule(fileService.getModules());
            syncDbService.insertAppointments(fileService.getAllAppointments());
        }

    }

    // This function should be called automatically every midnight
    // it will just call the manualSynchronize function
    public void synchronize(View e){

        int current = LocalTime
                .now(ZoneId.of("Europe/Berlin"))
                .getHour();

        if(current == LocalTime.MIDNIGHT.getHour()){
            manualSynchronize(e);
        }
    }
}
