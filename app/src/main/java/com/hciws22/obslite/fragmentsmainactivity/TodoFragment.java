package com.hciws22.obslite.fragmentsmainactivity;

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
import com.hciws22.obslite.fragmentstodo.ToDoThisWeekFragment;
import com.hciws22.obslite.fragmentstodo.TodoExamsFragment;
import com.hciws22.obslite.fragmentstodo.TodoNextWeekFragment;
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
        Chip nextWeek = view.findViewById(R.id.chip_2);
        Chip thisWeek = view.findViewById(R.id.chip_1);
        Chip examsChoice = view.findViewById(R.id.chip_3);

        setTodoThisWeekChip(thisWeek);
        setNextWeekChip(nextWeek);
        setExamsChip(examsChoice);

        //set Today View as default
        chipGroup.setSelectionRequired(true);
        chipGroup.setSingleSelection(true);
        chipGroup.check(thisWeek.getId());
        if (thisWeek.isChecked()) {
            Fragment toDoThisWeekFragment = new ToDoThisWeekFragment();
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
        return view;
    }


    private void setNextWeekChip(Chip nextWeekChip) {
        String nexWeekText = "NEXT WEEK";
        nextWeekChip.setText(nexWeekText);
        showNextWeek(nextWeekChip);
    }

    private void setExamsChip(Chip examsChip) {
        String examsText = "EXAMS";
        examsChip.setText(examsText);
        showExams(examsChip);
    }

    private void setTodoThisWeekChip(Chip thisWeekChoice) {
        String thisWeekText = "THIS WEEK";
        thisWeekChoice.setText(thisWeekText);
        showTodoThisWeek(thisWeekChoice);
    }

    private void showTodoThisWeek(Chip selectedChip) {
        selectedChip.setOnClickListener(v -> {
            //TODO: selectTodayView
            Fragment thisWeekFragment = new ToDoThisWeekFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.todo_scrollable, thisWeekFragment).commit();
        });
    }

    private void showNextWeek(Chip selectedChip) {
        selectedChip.setOnClickListener(v -> {
            //TODO: selectWeekView
            Fragment nextWeekFragment = new TodoNextWeekFragment();
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
