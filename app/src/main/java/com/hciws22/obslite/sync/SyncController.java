package com.hciws22.obslite.sync;

import android.content.Context;
import android.util.Log;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.entities.AppointmentEntity;
import com.hciws22.obslite.entities.ModuleEntity;
import com.hciws22.obslite.jobs.ResponseService;
import com.hciws22.obslite.jobs.SocketConnectionService;
import com.hciws22.obslite.notification.NotificationController;
import com.hciws22.obslite.setting.Translation;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SyncController {

    private final ResponseService responseService;
    private final FileService fileService;
    private final SyncDbService syncDbService;
    private final NotificationController notificationController;

    private final Context context;
    public SyncController(Context context) {
        this.context = context;

        SqLiteHelper sqLiteHelper = new SqLiteHelper(context);
        responseService = new ResponseService();
        fileService = new FileService();
        syncDbService = new SyncDbService(sqLiteHelper);
        notificationController = new NotificationController(context);
    }


    public String updateSyncLabel(String editText){

        boolean hasFailed = manualSynchronize(editText, true);

        String errorMsg = Translation.getTranslation( Translation.ERROR_OBS_LINK_UPDATE, Translation.loadMode(context));
        String success =
                Translation.getTranslation( Translation.SYNC_DATE_FORMAT, Translation.loadMode(context)) +
                Translation.getTranslation( Translation.RIGHT_NOW_SUCCESS_MSG, Translation.loadMode(context));

        return hasFailed ? errorMsg : success;


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
        }

        if(fetchDataFromOBS(url)){
            Log.d("manual sync: ", "fetch not succeeded");
            return true;

        }

        if(isNewLink){
            syncDbService.truncateAppointments();
        }

        syncDbService.insertOrUpdateTable(url, ZonedDateTime.now());
        updateData(isNewLink);
        return false;

    }

    private void updateData(Boolean isNewLink){

        List<String> filteredList = responseService.getFilteredList();
        fileService.convertToModule(filteredList);

        fileService.convertOBStoEntityRepresentation();


        boolean isValid = lookForInvalidModules(fileService.getAllAppointments());

        if(isNewLink || !isValid) {
            syncDbService.insertOrUpdateModule(fileService.getModules());
            syncDbService.initAppointments(fileService.getAllAppointments());
            return;
        }

        removeUnchangedDataFromPayload(fileService.getAllAppointments());

        syncDbService.insertOrUpdateModule(fileService.getModules());

        if(!fileService.getAllAppointments().isEmpty()){
            updateChangedData(fileService.getAllAppointments());
        }

    }


    public void updateChangedData(Map<String,List<AppointmentEntity>> appointments){

        for (Map.Entry<String, List<AppointmentEntity>> entry : appointments.entrySet()) {

            List<AppointmentEntity> old = syncDbService.readRegisteredAppointments(entry.getValue().get(0));
            List<AppointmentEntity> payload = entry.getValue();

            if (payload.size() > old.size()) {
                syncDbService.insertAppointments(payload, true);
                continue;
            }

            Optional<AppointmentEntity> oldApp = findChangedData(payload, old);

            if (payload.size() < old.size() && oldApp.isPresent()) {

               syncDbService.deleteInvalidAppointments(oldApp.get(), true);
               syncDbService.insertAppointments(payload, false);
                continue;
            }

            if(oldApp.isPresent()){
                syncDbService.deleteInvalidAppointments(oldApp.get(), false);
                syncDbService.updateAppointments(payload, true);
            }


        }
    }

    private Optional<AppointmentEntity> findChangedData(List<AppointmentEntity> payload, List<AppointmentEntity> old) {

       return old.stream().filter(o -> !payload.contains(o)).findFirst();

    }

    public void removeUnchangedDataFromPayload(Map<String,List<AppointmentEntity>> appointments){
        /*String key = "";
        for (Map.Entry<String, List<AppointmentEntity>> entry : appointments.entrySet()) {
             key = entry.getKey();
            break;
        }

        appointments.get(key).remove(0);*/


        ArrayList<String> removeKeys = new ArrayList<>();
        for (Map.Entry<String, List<AppointmentEntity>> entry : appointments.entrySet()) {
            List<AppointmentEntity> old = syncDbService.readRegisteredAppointments(entry.getValue().get(0));
            if(old.isEmpty()) continue;

            if(entry.getValue().containsAll(old) && old.size() == entry.getValue().size()){
                removeKeys.add(entry.getKey());
            }

        }

        removeKeys.forEach(appointments::remove);

    }


    private boolean lookForInvalidModules(Map<String,List<AppointmentEntity>> modules){

        List<ModuleEntity> oldList = syncDbService.readRegisteredModules();

        Optional<ModuleEntity> optional = oldList.stream()
                .filter(o -> modules.containsKey(o.getId()))
                .findFirst();

        // if language has changed drop everything
        if(!optional.isPresent()){
            syncDbService.resetDatabaseTemplate();
            return false;
        }

        // if payload contains less appointments than database
        syncDbService.deleteInvalidModules(modules, oldList);
        return true;

    }

}
