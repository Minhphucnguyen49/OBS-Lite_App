package com.hciws22.obslite.sync;

import com.hciws22.obslite.application.Module;
import com.hciws22.obslite.entities.AppointmentEntity;
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

    final List<Module> obsList = new ArrayList<>();

    private final Set<ModuleEntity> moduleEntities = new HashSet<>();
    private final Map<String, List<AppointmentEntity>> appointments = new LinkedHashMap<>();



    public void convertToModule(@NotNull List<String> filteredList){

        obsList.clear();
        // generate application representation
        Module module = new Module();
        for (String variable: filteredList) {
            ContentTypeFactory.cut(variable, module);

            if(variable.contains(ContentTypeFactory.CATEGORIES.name())){
                obsList.add(module);
                module = new Module();
            }
        }

    }
    // generate entity representation
    void generateEntityRepresentation(){

        moduleEntities.clear();
        appointments.clear();

        for (Module m : obsList) {
            moduleEntities.add(ModuleEntity.fromModule(m));
            appointments.put(m.getId(), new ArrayList<>());

            obsList.stream()
                    .filter(obsItem -> obsItem.getId().equals(m.getId()))
                    .forEach(obsItem -> appointments
                            .get(m.getId())
                            .add(AppointmentEntity.fromAppointment(
                                    obsItem.getAppointment()
                                    ,obsItem.getId()
                                    ,obsItem.getAppointment().getType())
                            ));
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
