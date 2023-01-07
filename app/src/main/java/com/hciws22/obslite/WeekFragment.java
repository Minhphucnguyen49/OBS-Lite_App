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
import com.hciws22.obslite.today.TodayController;
import com.hciws22.obslite.week.Week;

import java.util.ArrayList;

public class WeekFragment extends Fragment {
    private ListView modulesList;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.week, container, false);
        super.onViewCreated(view, savedInstanceState);

        modulesList = view.findViewById(R.id.modulesListView);

        ArrayList<Week> modules = new ArrayList<>();

        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));
        modules.add(new Week("HCI",  "P", "C:12/04.01", "02.12.23", "12:30 - 15:00"));

        ArrayAdapter<Week> modulesAdapter = new ArrayAdapter<>(
                mContext,
                R.layout.list_item_week,
                R.id.text_view,
                modules
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