package com.hciws22.obslite.setting;

import android.view.View;
import com.hciws22.obslite.sync.SyncController;

public class SettingController {

    private final SyncController syncController;

    public SettingController(SyncController syncController) {
        this.syncController = syncController;
    }


    public void init(View view){
        syncController.init(view);
    }

}
