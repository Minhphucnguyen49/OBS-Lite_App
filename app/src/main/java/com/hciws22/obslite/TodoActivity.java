
package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import java.util.ArrayList;


public class TodoActivity extends AppCompatActivity {

    private RecyclerView modulesRecView;
    private ModuleRecViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo);

        adapter = new ModuleRecViewAdapter(this);
        modulesRecView = findViewById(R.id.modulesRecView);

        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(this));

        /*
        ArrayList<TodoView> moduleList = new ArrayList<>();

        moduleList.add( new Module("HCI #4", ModuleType.Practical, "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));

        moduleList.add( new Module("BS #5", ModuleType.Practical, "20%",
                "Mon 20.11.2022", "10:15-11:45","D15/01.07."));
        moduleList.add( new Module("TI #6", ModuleType.Practical, "20%",
                "Mon 20.11.2022", "08:30-10:00","D15/01.07."));
        moduleList.add( new Module("GDV #4", ModuleType.Practical, "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));
        moduleList.add( new Module("PG2 #4", ModuleType.Practical, "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));
        moduleList.add( new Module("DB #5", ModuleType.Practical, "20%",
                "Mon 20.11.2022", "08:30-10:00","D15/01.07."));
        moduleList.add( new Module("TI #7", ModuleType.Practical, "20%",
                "Mon 20.11.2022", "14:15-15:45","D15/01.07."));
        moduleList.add( new Module("MPS #5", ModuleType.Practical, "20%",
                "Mon 20.11.2022", "10:15-11:45","D15/01.07."));

        adapter.setModules(moduleList);

         */
    }
}



