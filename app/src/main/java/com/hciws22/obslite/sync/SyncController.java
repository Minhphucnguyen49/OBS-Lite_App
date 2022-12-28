package com.hciws22.obslite.sync;

import android.view.View;
import com.hciws22.obslite.db.SqLiteHelper;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;

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

        fetchDataFromOBS();

        synchronized (responseService.getFilteredList()) {
            fileService.convertToModule(responseService.getFilteredList());
        }

        fileService.generateEntityRepresentation();
        syncDbService.insertModule(fileService.getModules());
        syncDbService.insertAppointments(fileService.getAllAppointments());

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
