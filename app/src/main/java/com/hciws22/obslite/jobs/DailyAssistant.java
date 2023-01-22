package com.hciws22.obslite.jobs;



import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;

import com.hciws22.obslite.notification.NotificationController;


public class DailyAssistant extends JobService {

    private static final String TAG = "DailyAssistantService";
    private final NotificationController notificationController;
    private static Context context;

    public DailyAssistant() {
        this.notificationController =  new NotificationController(context);
    }

    public static void setContext(Context m_context) {
        context = m_context;
    }


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        executeTask(jobParameters);
        return true;
    }

    private void executeTask(JobParameters jobParameters) {


        Log.d(TAG,Thread.currentThread().getName() + ": Job is running");
        boolean isSucceeded;
        if(notificationController.displayDailyAppointments()){
            isSucceeded = true;
            Log.d(TAG,Thread.currentThread().getName() + ": Job has failed");
        }else{
            isSucceeded = false;
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
