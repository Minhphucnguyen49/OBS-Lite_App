package com.hciws22.obslite.todo;

import com.google.android.material.chip.Chip;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.setting.Translation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

public class TodoController {

    private final TodoDbService todoDbService;
    //public static List<Todo> sliderSavedList = todoDbService.selectTodoAppointments();

    private static final String PREFERENCES_NAME = "com.hciws22.obslite";
    private static final String PREF_KEY = "mode";//true = deutsch, false = english

    public TodoController(SqLiteHelper sqLiteHelper) {
        this.todoDbService = new TodoDbService(sqLiteHelper);
    }

    public List<Todo> getAllWeek(){
        return todoDbService.selectAllWeek();
    }
    public List<Todo> getCurrentWeek() {
        return todoDbService.selectCurrentWeek();
    }
    public List<Todo> getExams(){return todoDbService.selectExams();}
    //public void getExtraInfo(){
        //todoDbService.insertExtraInfo(//TODO: Return all);
    //}
    public List<Todo> getExtraInfo(){
        return todoDbService.selectExtra();
    }
    public void insertExtraInfo(){
        todoDbService.insertExtraInfo(todoDbService.selectAllWeek());
    }
    public boolean loadMode(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(PREF_KEY, true);//true is german
    }
    public void applyAllChanges(Chip currentWeek, Chip allWeek, Chip exam, Context context){
        currentWeek.setText(Translation.getTranslation( Translation.CURRENT_WEEK,this.loadMode(context)));
        allWeek.setText(Translation.getTranslation( Translation.ALL_WEEK,this.loadMode(context)));
        exam.setText(Translation.getTranslation( Translation.EXAMS,this.loadMode(context)));
    }
}
