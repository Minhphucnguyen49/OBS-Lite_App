
package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hciws22.obslite.databinding.ActivityMainBinding;
import com.hciws22.obslite.jobs.AutoSyncService;
import com.hciws22.obslite.mainFragment.AgendaFragment;
import com.hciws22.obslite.mainFragment.SettingsFragment;
import com.hciws22.obslite.mainFragment.TodayFragment;
import com.hciws22.obslite.mainFragment.TodoFragment;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AgendaFragment()); //default screen when app starts

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.agenda);

        /**
         *  Auto Synchronisation
         */
        setUpScheduler();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.todo:
                    replaceFragment(new TodoFragment());
                    break;
                case R.id.agenda:
                    replaceFragment(new AgendaFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;
        });
    }

    public int setUpScheduler(){
        ComponentName componentName = new ComponentName(this, AutoSyncService.class);
        JobInfo jobInfo = new JobInfo.Builder(123, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(1000 * 60 * 60)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        return scheduler.schedule(jobInfo);
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        //fragmentTransaction.addToBackStack(null); //for back arrow to go back one fragment, but navbar indicator isnt changing
        fragmentTransaction.commit();

    }

}
/*
    public void moveToDo(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TodoActivity.class);
            startActivity(intent);
        });
    }

    public void moveToday(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TodayActivity.class);
            startActivity(intent);
        });
    }

    public void moveSettings(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

 */




