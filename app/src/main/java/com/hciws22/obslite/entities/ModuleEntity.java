package com.hciws22.obslite.entities;

import com.hciws22.obslite.application.Appointment;
import com.hciws22.obslite.application.Module;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ModuleEntity {

    @NotNull
    private String id;

    @NotNull
    private String name;

    private String semester;

    public ModuleEntity() {

    }

    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }


    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleEntity that = (ModuleEntity) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static ModuleEntity fromModule(Module m){
        ModuleEntity moduleEntity = new ModuleEntity();
        moduleEntity.setId(m.getId());
        moduleEntity.setSemester(m.getSemester());
        moduleEntity.setName(m.getName());
        return moduleEntity;
    }
}
