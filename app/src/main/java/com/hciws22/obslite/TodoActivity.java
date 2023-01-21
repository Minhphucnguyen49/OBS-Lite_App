
package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.todo.ModuleRecViewAdapter;
import com.hciws22.obslite.todo.TodoController;
import com.hciws22.obslite.utils.SpacingItemDecorator;


public class TodoActivity extends AppCompatActivity {

    private RecyclerView modulesRecView;
    private ModuleRecViewAdapter adapter;
    private ModuleRecViewAdapter adapter_slider;
    TodoController todoController = new TodoController(new SqLiteHelper(TodoActivity.this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo);

        adapter = new ModuleRecViewAdapter(this);
        adapter_slider = new ModuleRecViewAdapter( this, new SqLiteHelper(TodoActivity.this) );
        modulesRecView = findViewById(R.id.modulesRecView);
        //modulesRecView.setAdapter(adapter);

        modulesRecView.setAdapter(adapter_slider);
        modulesRecView.setLayoutManager(new LinearLayoutManager(this));

        //adapter_slider.insertExtraInfo();
        //adapter.setModules(todoController.getToDo());
        adapter_slider.setModules(adapter_slider.getToDo(),adapter_slider.getExtraInfo());

        //Add space between cards
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(30);
        modulesRecView.addItemDecoration(itemDecorator);

    }
}


