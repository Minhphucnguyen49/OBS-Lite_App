
package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.sync.SyncController;
import com.hciws22.obslite.todo.ModuleRecViewAdapter;


public class TodoActivity extends AppCompatActivity {

    private RecyclerView modulesRecView;
    private ModuleRecViewAdapter adapter;
    SyncController syncController = new SyncController(new SqLiteHelper(TodoActivity.this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo);

        adapter = new ModuleRecViewAdapter(this);
        modulesRecView = findViewById(R.id.modulesRecView);
        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setModules(syncController.getToDo());

    }
}

