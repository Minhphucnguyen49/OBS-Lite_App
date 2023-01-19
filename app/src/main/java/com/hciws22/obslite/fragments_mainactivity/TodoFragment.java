package com.hciws22.obslite.fragments_mainactivity;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.hciws22.obslite.R;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.fragments_todo.ToDoCurrentFragment;
import com.hciws22.obslite.fragments_todo.TodoExamsFragment;
import com.hciws22.obslite.fragments_todo.TodoAllWeekFragment;
import com.hciws22.obslite.todo.TodoController;


public class TodoFragment extends Fragment {
    private Context mContext;
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

        View view = inflater.inflate(R.layout.todo, container, false);
        super.onViewCreated(view, savedInstanceState);

        ChipGroup chipGroup = view.findViewById(R.id.chip_group);

        Chip currentWeek = view.findViewById(R.id.chip_1);
        Chip allWeek = view.findViewById(R.id.chip_2);
        Chip examsChoice = view.findViewById(R.id.chip_3);

        setCurrentWeek(currentWeek);
        setAllWeek(allWeek);
        setExams(examsChoice);

        //set Today View as default
        chipGroup.setSelectionRequired(true);
        chipGroup.setSingleSelection(true);
        chipGroup.check(currentWeek.getId());
        if (currentWeek.isChecked()) {
            Fragment toDoThisWeekFragment = new ToDoCurrentFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.todo_scrollable, toDoThisWeekFragment).commit();
        }

        //All chips are single selected
        chipGroup.setOnCheckedStateChangeListener((group, id) -> {
            Chip chip = ((Chip) group.getChildAt(group.getCheckedChipId()));
            if (chip != null) {
                for (int i = 0; i < group.getChildCount(); ++i) {
                    group.getChildAt(i).setClickable(true);
                }
                chip.setClickable(false);
            }
        });
        todoController.applyAllChanges(currentWeek,allWeek,examsChoice,mContext);
        return view;
    }



    private void setAllWeek(Chip allWeek) {
        String nexWeekText = "ALL"; //will be overwritten
        allWeek.setText(nexWeekText);
        showAllWeek(allWeek);
    }

    private void setExams(Chip examsChip) {
        String examsText = "EXAMS"; //will be overwritten
        examsChip.setText(examsText);
        showExams(examsChip);
    }

    private void setCurrentWeek(Chip currentWeek) {
        String thisWeekText = "CURRENT"; //will be overwritten
        currentWeek.setText(thisWeekText);
        showCurrentWeek(currentWeek);
    }

    private void showCurrentWeek(Chip selectedChip) {
        selectedChip.setOnClickListener(v -> {
            //TODO: selectTodayView
            Fragment thisWeekFragment = new ToDoCurrentFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.todo_scrollable, thisWeekFragment).commit();
        });
    }

    private void showAllWeek(Chip selectedChip) {
        selectedChip.setOnClickListener(v -> {
            //TODO: selectWeekView
            Fragment nextWeekFragment = new TodoAllWeekFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.todo_scrollable, nextWeekFragment).commit();
        });
    }

    private void showExams(Chip selectedChip) {
        selectedChip.setOnClickListener(v -> {
            //TODO: selectTodayView
            Fragment examsFragment = new TodoExamsFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.todo_scrollable, examsFragment).commit();
        });
    }
}
