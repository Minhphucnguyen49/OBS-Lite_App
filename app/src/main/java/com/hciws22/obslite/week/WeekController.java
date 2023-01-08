package com.hciws22.obslite.week;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.today.TodayDbService;

import java.util.List;

public class WeekController {
    private final WeekDbService weekDbService;

    public WeekController(SqLiteHelper sqLiteHelper) {
        this.weekDbService = new WeekDbService(sqLiteHelper);
    }

    public List<Week> getWeekList() { return weekDbService.selectWeekAppointments(); }
}
