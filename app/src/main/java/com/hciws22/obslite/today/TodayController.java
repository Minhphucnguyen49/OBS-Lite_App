package com.hciws22.obslite.today;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.todo.Todo;
import com.hciws22.obslite.todo.TodoDbService;

import java.util.ArrayList;
import java.util.List;

public class TodayController {

    private final TodayDbService todayDbService;

    public TodayController(SqLiteHelper sqLiteHelper) {
        this.todayDbService = new TodayDbService(sqLiteHelper);
    }

    public List<Today> getToDay(){
        return todayDbService.selectToDayAppointments();
    }
}
