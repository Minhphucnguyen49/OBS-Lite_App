package com.hciws22.obslite.sync;

import com.hciws22.obslite.entities.Module;
import com.hciws22.obslite.enums.ContentTypeFactory;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;


public class FileService {

    private final List<Module> moduleList = new ArrayList<>();


    public void convertToModule(@NotNull List<String> filteredList){
        moduleList.clear();

        Module module = new Module();
        for (String variable: filteredList) {
            ContentTypeFactory.cut(variable, module);

            if(variable.contains(ContentTypeFactory.CATEGORIES.name())){
                moduleList.add(module);
                module = new Module();
            }
        }
    }
}
