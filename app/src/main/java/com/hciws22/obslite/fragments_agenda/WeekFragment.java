package com.hciws22.obslite.fragments_agenda;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.hciws22.obslite.R;
import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.setting.Translation;
import com.hciws22.obslite.week.WeekController;
import com.hciws22.obslite.week.WeekListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class WeekFragment extends Fragment {
    private ExpandableListView expandableListView;
    private ExpandableListAdapter adapter;
    List<String> expandableListTitle;
    //HashMap<String, List<Week>> expandableListDetail;
    LinkedHashMap<String, List<String>> expandableListDetail;
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
        View view = inflater.inflate(R.layout.choice_week, container, false);
        super.onViewCreated(view, savedInstanceState);

        expandableListView = view.findViewById(R.id.expandableListView);
        //expandableListDetail = weekController.getData();
        expandableListDetail = weekController.getDataString(mContext);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());

        adapter = new WeekListAdapter(mContext, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnGroupExpandListener(groupPosition -> {

            if (adapter.getChildrenCount(groupPosition) == 0){
                String freeMessage = Translation.getTranslation( Translation.POP_UP_NO_APPOINTMENTS, Translation.loadMode(mContext));
                String day = Translation.getTranslation(Translation.valueOf(expandableListTitle.get(groupPosition).toUpperCase(Locale.ROOT)), Translation.loadMode(mContext));
                Toast.makeText(mContext,
                        freeMessage + day +" Yoo-hoo!!!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(groupPosition -> {
            //Toast.makeText(mContext,expandableListTitle.get(groupPosition) + " List Collapsed.",Toast.LENGTH_SHORT).show();
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Toast.makeText(
                    mContext,
                    expandableListTitle.get(groupPosition)
                            + " -> "
                            + expandableListDetail
                            .get(expandableListTitle.get(groupPosition))
                            .get(childPosition), Toast.LENGTH_SHORT
            ).show();
            return false;
        });

        return view;
    }
}
/*
        //Change to Week Screen
        Button weekBtn = view.findViewById(R.id.button_to_today);
        weekBtn.setOnClickListener(view1 -> {
            Fragment todayFragment = new TodayFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_main, todayFragment).commit();
        });
 */