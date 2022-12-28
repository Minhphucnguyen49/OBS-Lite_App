
package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.sync.SyncController;
import com.hciws22.obslite.today.LectureRecViewAdapter;

public class TodayActivity extends AppCompatActivity {

    private RecyclerView modulesRecView;
    private LectureRecViewAdapter adapter;
    SyncController syncController = new SyncController(new SqLiteHelper(TodayActivity.this));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today);

        adapter = new LectureRecViewAdapter(this);

        modulesRecView = findViewById(R.id.modulesRecView_today);
        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setModules(syncController.getToDay());
    }
}


