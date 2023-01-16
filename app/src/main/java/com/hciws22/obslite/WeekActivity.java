package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hciws22.obslite.databinding.ActivityMainBinding;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.week.WeekController;

public class WeekActivity extends AppCompatActivity {

    private ListView modulesList;
    WeekController weekController = new WeekController(new SqLiteHelper(this));
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week);

        modulesList = findViewById(R.id.modulesListView);
/*
        ArrayAdapter<String> modulesAdapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_week_listview,
                R.id.expanded_list_item,
                weekController.getWeekString(weekController.getWeekArrayList())
        );

        TextView dateToday = findViewById(R.id.date_today_week);
        weekController.showDate(dateToday);
        //Change to Week Screen
        Button weekBtn = findViewById(R.id.button_to_today);

        weekBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(WeekActivity.this, MainActivity.class);
            startActivity(intent);
            this.overridePendingTransition(0, 0);

         Fragment todayFragment = new TodayFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_main, todayFragment).commit();
            finish();
            this.overridePendingTransition(0, 0);
            ft.setCustomAnimations(0,0);




        });

        modulesList.setAdapter(modulesAdapter);
        */

    }


}

