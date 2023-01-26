package com.hciws22.obslite.fragments_mainactivity;

import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.hciws22.obslite.R;
import com.hciws22.obslite.fragments_agenda.TodayFragment;
import com.hciws22.obslite.fragments_agenda.TomorrowFragment;
import com.hciws22.obslite.fragments_agenda.WeekFragment;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.setting.Translation;
import com.hciws22.obslite.today.TodayController;

public class AgendaFragment extends Fragment {
    private Context mContext;
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

        View view = inflater.inflate(R.layout.agenda, container, false);
        super.onViewCreated(view, savedInstanceState);

        TextView todayTitle = view.findViewById(R.id.title_TODAY);
        todayTitle.setText(Translation.getTranslation( Translation.AGENDA, Translation.loadMode(mContext)));


        TextView dateToday = view.findViewById(R.id.date_today);

        todayController.showDate(dateToday, todayController.loadMode(mContext));

        ChipGroup chipGroup = view.findViewById(R.id.chip_group);

        Chip todayChoice = view.findViewById(R.id.chip_1);
        Chip tomorrowChoice = view.findViewById(R.id.chip_2);
        Chip weekChoice = view.findViewById(R.id.chip_3);

        setTodayChip(dateToday, todayChoice);
        setWeekChip(dateToday, weekChoice);
        setTomorrowChip(dateToday, tomorrowChoice);

        //set Today View as default
        chipGroup.setSelectionRequired(true);
        chipGroup.setSingleSelection(true);
        chipGroup.check(todayChoice.getId());
        if(todayChoice.isChecked()){
            Fragment todayFragment = new TodayFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.agenda_scrollable, todayFragment).commit();
        }

        //All chips are single selected
        chipGroup.setOnCheckedStateChangeListener( (group, id) -> {
            Chip chip = ((Chip) group.getChildAt(group.getCheckedChipId()));
            if (chip != null) {
                for (int i = 0; i < group.getChildCount(); ++i) {
                    group.getChildAt(i).setClickable(true);
                }
                chip.setClickable(false);
            }
        });
        todayController.applyAllChanges(todayChoice,tomorrowChoice,weekChoice,mContext);
        return view;
    }


    private void setWeekChip(TextView dateToday, Chip weekChoice) {
        String weekText = "WEEK";
        weekChoice.setText(weekText);
        showWeek(dateToday, weekChoice);
    }
    private void setTomorrowChip(TextView dateToday, Chip morgenChoice) {

        String weekText = "TOMORROW";
        morgenChoice.setText(weekText);
        showTomorrow(dateToday, morgenChoice);
    }
    private void setTodayChip(TextView dateToday, Chip todayChoice) {
        String todayText = "TODAY";
        todayChoice.setText(todayText);
        showToday(dateToday, todayChoice);

    }
    private void showToday(TextView dateToday, Chip selectedChip){
        selectedChip.setOnClickListener(v -> {
            //show todays date
            todayController.showDate(dateToday, todayController.loadMode(mContext));

            //TODO: selectTodayView
            Fragment todayFragment = new TodayFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.agenda_scrollable, todayFragment).commit();
        });
    }
    private void showWeek(TextView dateToday, Chip selectedChip) {
        selectedChip.setOnClickListener(v -> {
            //show todays date
            todayController.showDate(dateToday, todayController.loadMode(mContext));

            //TODO: selectWeekView
            Fragment weekFragment = new WeekFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.agenda_scrollable, weekFragment).commit();
        });
    }
    private void showTomorrow(TextView dateToday, Chip selectedChip){
        selectedChip.setOnClickListener(v -> {
            //add 1 Day to title
            todayController.showDateTomorrow(dateToday, todayController.loadMode(mContext));

            //TODO: selectTodayView
            Fragment tomorrowFragment = new TomorrowFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.agenda_scrollable, tomorrowFragment).commit();
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

        public void moveWeek(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, WeekActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        });

                /*
        chipGroup.removeAllViews();
        chipGroup.addView(weekChoice);
        chipGroup.addView(todayChoice);

//Change to Week Screen
//Button weekBtn = view.findViewById(R.id.button_to_week);
//moveWeek(weekBtn);

 */

