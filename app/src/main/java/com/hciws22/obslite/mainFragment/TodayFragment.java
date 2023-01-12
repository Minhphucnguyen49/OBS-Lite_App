package com.hciws22.obslite.mainFragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.hciws22.obslite.R;
import com.hciws22.obslite.WeekActivity;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.today.LectureRecViewAdapter;
import com.hciws22.obslite.today.AgendaController;
import com.hciws22.obslite.utils.SpacingItemDecorator;

public class TodayFragment extends Fragment {
    private Context mContext;
    private RecyclerView modulesRecView;
    private LectureRecViewAdapter adapter;
    AgendaController agendaController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        agendaController = new AgendaController(new SqLiteHelper(mContext));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.agenda, container, false);
        super.onViewCreated(view, savedInstanceState);

        adapter = new LectureRecViewAdapter(mContext);

        modulesRecView = view.findViewById(R.id.modulesRecView_today);
        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(mContext));

        TextView dateToday = view.findViewById(R.id.date_today);
        agendaController.showDate(dateToday);

        adapter.setModules(agendaController.getToDay());

        ChipGroup chipGroup = view.findViewById(R.id.chip_group);
        Chip weekChoice = view.findViewById(R.id.chip_2);
        Chip todayChoice = view.findViewById(R.id.chip_1);
        //Chip tommorrowChoice = view.findViewById(R.id.chip_3);

        chipWeek(weekChoice);
        chipToday(todayChoice);
        //Change to Week Screen
        Button weekBtn = view.findViewById(R.id.button_to_week);
        moveWeek(weekBtn);

        //Add space between cards
        addSpaceBetweenCards();

        return view;
    }

    private void chipWeek(Chip weekChoice) {
        String weekText = "WEEK";
        weekChoice.setText(weekText);
        weekChoice.setOnClickListener(v -> {
            //TODO: selectWeekView
            Fragment weekFragment = new WeekFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.chip_group, weekFragment).commit();

        });
    }

    private void chipToday(Chip todayChoice) {
        String todayText = "TODAY";
        todayChoice.setText(todayText);
        todayChoice.setOnClickListener(v -> {
            //TODO: selectWeekView
            Fragment todayFragment = new TodayFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.chip_group, todayFragment).commit();

        });
    }


    private void addSpaceBetweenCards() {
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(30);
        modulesRecView.addItemDecoration(itemDecorator);
    }


    public void moveWeek(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, WeekActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        });
    }
}