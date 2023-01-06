package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hciws22.obslite.week.Week;

import java.util.ArrayList;

public class WeekActivity extends AppCompatActivity {

    private ListView modulesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week);

        modulesList = findViewById(R.id.modulesListView);

        ArrayList<Week> modules = new ArrayList<>();

        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));

        ArrayAdapter<Week> modulesAdapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_week,
                R.id.text_view,
                modules
        );
        modulesList.setAdapter(modulesAdapter);

        moveToday(findViewById(R.id.button_to_today));
    }

    public void moveToday(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WeekActivity.this, TodayActivity.class);
            startActivity(intent);
        });
    }
}