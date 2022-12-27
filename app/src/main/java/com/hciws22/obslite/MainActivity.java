/*
package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.sync.SyncController;

public class MainActivity extends AppCompatActivity {

    SyncController syncController = new SyncController(new SqLiteHelper(MainActivity.this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        syncController.init(findViewById(R.id.send));

    }
}

 */

package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.sync.SyncController;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView modulesRecView;
    private ModuleRecViewAdapter adapter;
    SyncController syncController = new SyncController(new SqLiteHelper(MainActivity.this));

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

