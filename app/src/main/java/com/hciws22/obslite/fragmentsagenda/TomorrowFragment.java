package com.hciws22.obslite.fragmentsagenda;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hciws22.obslite.R;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.today.LectureRecViewAdapter;
import com.hciws22.obslite.today.TodayController;
import com.hciws22.obslite.utils.SpacingItemDecorator;

public class TomorrowFragment extends Fragment {
    private Context mContext;
    private RecyclerView modulesRecView;
    private LectureRecViewAdapter adapter;
    TodayController todayController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        todayController = new TodayController(new SqLiteHelper(mContext));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choice_today, container, false);
        super.onViewCreated(view, savedInstanceState);

        adapter = new LectureRecViewAdapter(mContext);

        modulesRecView = view.findViewById(R.id.modulesRecView_today);
        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(mContext));

        exceptionsHandling();
        adapter.setModules(todayController.getTomorrow());

        //Add space between cards
        addSpaceBetweenCards();

        return view;
    }

    private void exceptionsHandling() {
        if (todayController.getTomorrow().isEmpty()){
            Toast.makeText(mContext, "You are free tomorrow Yoo-hoo!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSpaceBetweenCards() {
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(30);
        modulesRecView.addItemDecoration(itemDecorator);
    }

}