package com.hciws22.obslite.mainFragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hciws22.obslite.R;
import com.hciws22.obslite.WeekFragment;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.today.LectureRecViewAdapter;
import com.hciws22.obslite.today.TodayController;
import com.hciws22.obslite.utils.SpacingItemDecorator;

public class TodayFragment extends Fragment {
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

        View view = inflater.inflate(R.layout.today, container, false);
        super.onViewCreated(view, savedInstanceState);

        adapter = new LectureRecViewAdapter(mContext);

        modulesRecView = (RecyclerView) view.findViewById(R.id.modulesRecView_today);
        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(mContext));

        TextView dateToday = view.findViewById(R.id.date_today);
        todayController.showDate(dateToday);

        //Change to Week Screen
        Button weekBtn = view.findViewById(R.id.button_to_week);
        weekBtn.setOnClickListener(view1 -> {
            Fragment weekFragment = new WeekFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_main, weekFragment).commit();
        });


        adapter.setModules(todayController.getToDay());

        //Add space between cards
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(30);
        modulesRecView.addItemDecoration(itemDecorator);

        return view;
    }
}