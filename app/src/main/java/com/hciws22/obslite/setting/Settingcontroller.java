package com.hciws22.obslite.setting;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.hciws22.obslite.SettingsActivity;
import com.hciws22.obslite.jobs.AutoSyncService;
import com.hciws22.obslite.sync.SyncController;

public class Settingcontroller {

    private final SyncController syncController;
    private final AutoSyncService autoSyncService;

    public Settingcontroller(SyncController syncController) {
        this.syncController = syncController;
        this.autoSyncService = new AutoSyncService();
    }


    public void init(Context c, View view){
        syncController.init(view);
        scheduleSync(c);


    }

    public void scheduleSync(Context c){
        int resultCode = autoSyncService.setUpScheduler(c);

        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d("SettingController", "job succeeded");
        }else{
            Log.d("SettingController", "job succeeded");
        }

    }

}
