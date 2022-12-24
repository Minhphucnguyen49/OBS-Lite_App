package com.hciws22.obslite.sync;

import android.view.View;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;

public class SyncController {

    ResponseService responseService = new ResponseService();
    FileService fileService = new FileService();
    SyncDbService syncDbService = new SyncDbService();
    Boolean isAvailable = true;


    public void init(View sendbtn) {

        fetchDataFromOBS();

        sendbtn.setOnClickListener(this::manualSynchronize);

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
            // db service
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
