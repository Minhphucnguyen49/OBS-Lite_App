package com.hciws22.obslite.mainFragment;

import androidx.annotation.NonNull;
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

import java.util.List;

public class AgendaFragment extends Fragment {
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

        TextView dateToday = view.findViewById(R.id.date_today);
        agendaController.showDate(dateToday);

        ChipGroup chipGroup = view.findViewById(R.id.chip_group);
        Chip weekChoice = view.findViewById(R.id.chip_2);
        Chip todayChoice = view.findViewById(R.id.chip_1);
        //Chip tommorrowChoice = view.findViewById(R.id.chip_3);

        setTodayChip(todayChoice);
        setWeekChip(weekChoice);

        //Change to Week Screen
        Button weekBtn = view.findViewById(R.id.button_to_week);
        moveWeek(weekBtn);

        return view;
    }

    private void setWeekChip(Chip weekChoice) {
        String weekText = "WEEK";
        weekChoice.setText(weekText);
        weekChoice.setOnClickListener(v -> {
            //TODO: selectWeekView
            Fragment weekFragment = new WeekFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.agenda_scrollable, weekFragment).commit();

        });
    }

    private void setTodayChip(Chip todayChoice) {
        String todayText = "TODAY";
        todayChoice.setText(todayText);
        todayChoice.setOnClickListener(v -> {
            //TODO: selectWeekView
            Fragment todayFragment = new TodayFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.agenda_scrollable, todayFragment).commit();

        });
    }

    public void moveWeek(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, WeekActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        });
    }
}
/*
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (group.findViewById(R.id.chip_2) != null){
                setWeekChip(group.findViewById(R.id.chip_2));
            }
            if (group.findViewById(R.id.chip_1) != null){
                setTodayChip(group.findViewById(R.id.chip_1));
            }
        });
 */