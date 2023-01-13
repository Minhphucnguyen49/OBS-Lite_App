package com.hciws22.obslite.fragmentstodo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hciws22.obslite.R;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.todo.ModuleRecViewAdapter;
import com.hciws22.obslite.todo.TodoController;
import com.hciws22.obslite.utils.SpacingItemDecorator;

public class TodoExamsFragment extends Fragment {
    private Context mContext;
    private RecyclerView modulesRecView;
    private ModuleRecViewAdapter adapter;
    TodoController todoController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        todoController = new TodoController(new SqLiteHelper(mContext));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choice_todo_all, container, false);
        super.onViewCreated(view, savedInstanceState);

        adapter = new ModuleRecViewAdapter(mContext);

        modulesRecView = (RecyclerView) view.findViewById(R.id.modulesRecView);
        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(mContext));


        todoController.getExtraInfo();
        adapter.setModules(todoController.getExams());
        todoController.getExtraInfo();

        //Add space between cards
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(30);
        modulesRecView.addItemDecoration(itemDecorator);
        // Inflate the layout for this fragment
        return view;

    }
}
