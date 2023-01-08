package com.hciws22.obslite;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.mainFragment.TodayFragment;
import com.hciws22.obslite.week.Week;
import com.hciws22.obslite.week.WeekController;

public class WeekFragment extends Fragment {
    private ListView modulesList;
    private Context mContext;
    WeekController weekController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        weekController = new WeekController(new SqLiteHelper(mContext));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.week, container, false);
        super.onViewCreated(view, savedInstanceState);

        modulesList = view.findViewById(R.id.modulesListView);

        ArrayAdapter<Week> modulesAdapter = new ArrayAdapter<>(
                mContext,
                R.layout.list_item_week,
                R.id.text_view,
                weekController.getWeekList()
        );

        //Change to Week Screen
        Button weekBtn = view.findViewById(R.id.button_to_today);
        weekBtn.setOnClickListener(view1 -> {
            Fragment todayFragment = new TodayFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_main, todayFragment).commit();
        });

        modulesList.setAdapter(modulesAdapter);
        return view;
    }
}