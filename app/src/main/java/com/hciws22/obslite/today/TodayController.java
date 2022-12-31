package com.hciws22.obslite.today;

import android.widget.TextView;

import com.hciws22.obslite.db.SqLiteHelper;

import java.util.List;

public class TodayController {

    private final TodayDbService todayDbService;

    public TodayController(SqLiteHelper sqLiteHelper) {
        this.todayDbService = new TodayDbService(sqLiteHelper);
    }

    public List<Today> getToDay(){
        return todayDbService.selectToDayAppointments();
    }

/*
    public void getDate(TextView v, List<Today>modules){
        String date = modules.get(position).getDate();
        v.setText(date);//Eventttttttt nur für Jamil
    }


 */


}
