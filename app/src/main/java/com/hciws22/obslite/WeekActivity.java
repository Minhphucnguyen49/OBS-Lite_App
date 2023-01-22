package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
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

    }


}

