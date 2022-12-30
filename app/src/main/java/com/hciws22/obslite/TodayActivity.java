
package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.today.LectureRecViewAdapter;
import com.hciws22.obslite.today.TodayController;
import com.hciws22.obslite.utils.SpacingItemDecorator;

public class TodayActivity extends AppCompatActivity {

    private RecyclerView modulesRecView;
    private LectureRecViewAdapter adapter;
    TodayController todayController = new TodayController(new SqLiteHelper(TodayActivity.this));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today);

        adapter = new LectureRecViewAdapter(this);

        modulesRecView = findViewById(R.id.modulesRecView_today);
        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setModules(todayController.getToDay());

        //Add space between cards
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(30);
        modulesRecView.addItemDecoration(itemDecorator);

    }
}


