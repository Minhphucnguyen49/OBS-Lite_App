package com.hciws22.obslite.fragments_todo;

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

    private ModuleRecViewAdapter adapter_slider;
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
        adapter_slider = new ModuleRecViewAdapter( mContext, new SqLiteHelper(mContext) );

        modulesRecView = view.findViewById(R.id.modulesRecView);
        modulesRecView.setAdapter(adapter_slider);
        modulesRecView.setLayoutManager(new LinearLayoutManager(mContext));

        adapter_slider.setModules(adapter_slider.getExams(),adapter_slider.getExtraInfo());

        //Add space between cards
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(30);
        modulesRecView.addItemDecoration(itemDecorator);
        // Inflate the layout for this fragment
        return view;

    }
    @Override
    public void onResume(){
        super.onResume();

    }
}


