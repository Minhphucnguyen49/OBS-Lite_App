package com.hciws22.obslite.jobs;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.hciws22.obslite.MainActivity;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.sync.SyncController;

public class AutoSyncService extends JobService {

    private static final String TAG = "AutoSyncService";
    private boolean jobCancelled = false;
    private final SyncController syncController;
    private static Context context;

    public AutoSyncService() {
        this.syncController =  new SyncController(context);
    }

    public static void setContext(Context m_context) {
        context = m_context;
    }


    /*
     * If the operation duration is not time consuming we can return false. this
     * tells the system that the job is done when the methods finishes. But long consuming
     * tasks like connecting to the server, would freeze the application
     * By default, this job is running on the UI Thread. So we are responsible of scheduling our
     * tasks. When we do long tasks, we return true not false. this tells the system to keep
     * this job service awake so that he can finish executing his work. However, we need also to
     * tell the system when we are finished
     *
     */
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        executeTask(jobParameters);
        return true;
    }

    private void executeTask(JobParameters jobParameters) {


        Log.d(TAG,Thread.currentThread().getName() + ": Job is running");
        boolean isSucceeded;
         if(syncController.autoSynchronize()){
             isSucceeded = false;
             Log.d(TAG,Thread.currentThread().getName() + ": Job has failed");
         }else{
             isSucceeded = true;
             Log.d(TAG,Thread.currentThread().getName() + ": Job has succeeded");
         }

         jobFinished(jobParameters, isSucceeded);
    }

    // if our Job has failed for any raison. We can retry it by returning true;
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, Thread.currentThread().getName() + ": Job cancelled before completion");

        return false;

    }
}
