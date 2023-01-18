package com.hciws22.obslite.jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;
import com.hciws22.obslite.notification.NotificationController;

public class AutoNotificationService extends JobService {

    private static final String TAG = "AutoNotificationService";
    private boolean jobCancelled = false;
    private final NotificationController notificationController;
    private static Context context;

    public AutoNotificationService() {
        this.notificationController =  new NotificationController(context);
    }

    public static void setContext(Context m_context) {
        context = m_context;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        executeTask(jobParameters);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, Thread.currentThread().getName() + ": Job cancelled before completion");

        jobCancelled = true;
        return true;
    }

    private void executeTask(JobParameters jobParameters) {

        if(jobCancelled){
            return;
        }


        Log.d(TAG,Thread.currentThread().getName() + ": Job is running");
        boolean isSucceeded;
        if(notificationController.createNotification()){
            isSucceeded = false;
            Log.d(TAG,Thread.currentThread().getName() + ": Job has failed");
        }else{
            isSucceeded = true;
            Log.d(TAG,Thread.currentThread().getName() + ": Job has succeeded");
        }

        jobFinished(jobParameters, isSucceeded);
    }

}
