package com.hciws22.obslite.sync;

import android.util.Log;

import com.hciws22.obslite.entities.AppointmentEntity;
import com.hciws22.obslite.entities.ExtraInfoEntity;
import com.hciws22.obslite.entities.ModuleEntity;
import com.hciws22.obslite.enums.ContentTypeFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;


public class FileService {

    private final List<OBSItem> obsList = new ArrayList<>();
    private final Set<ModuleEntity> moduleEntities = new HashSet<>();
    private final Map<String, List<AppointmentEntity>> appointments = new LinkedHashMap<>();



    // this function will be executed inside a synchronize Block (synchronously)
    public void convertToModule(@NotNull List<String> filteredList){

        obsList.clear();
        Log.d("File Service", String.valueOf(filteredList.size()));
        // generate application representations
        OBSItem obsItem = new OBSItem();
        for (String variable: filteredList) {

            if(ContentTypeFactory.cut(variable, obsItem)){
                obsList.add(obsItem);
                obsItem = new OBSItem();
            }
        }

        Log.d("File Service OBSList", String.valueOf(obsList.size()));

    }

    // this function will be executed outside the synchronize block (Asynchronously).
    // generate entity representation
    // convert OBS List with multiple OBS Items into a small Map<ModuleID, List<Appointment>>
    void convertOBStoEntityRepresentation(){

        moduleEntities.clear();
        appointments.clear();

        for (OBSItem obsItem : obsList) {
            moduleEntities.add(ModuleEntity.build(obsItem));
            appointments.put(obsItem.getId(), new ArrayList<>());

            obsList.stream()
                    .filter(obsItem2 -> obsItem2.getId().equals(obsItem.getId()))
                    .forEach(obsItem2 -> appointments
                            .get(obsItem.getId())
                            .add(AppointmentEntity.build(
                                    obsItem2.getAppointment()
                                    , obsItem2.getId()
                                    , obsItem2.getAppointment().getType())
                            )
                    );
        }
    }


    public Set<ModuleEntity> getModules(){
        return moduleEntities;
    }

    public Map<String, List<AppointmentEntity>> getAllAppointments(){
        return appointments;
    }

    public List<AppointmentEntity> getAppointmentsOfOneModule(String moduleID){
        if(!appointments.containsKey(moduleID)) return Collections.emptyList();
        return appointments.get(moduleID);
    }
}
