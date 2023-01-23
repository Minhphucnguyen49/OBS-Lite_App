package com.hciws22.obslite.fragments_agenda;

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
import com.hciws22.obslite.setting.Translation;
import com.hciws22.obslite.today.LectureRecViewAdapter;
import com.hciws22.obslite.today.TodayController;
import com.hciws22.obslite.utils.SpacingItemDecorator;

import java.util.Locale;

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

        View view = inflater.inflate(R.layout.choice_today, container, false);
        super.onViewCreated(view, savedInstanceState);

        adapter = new LectureRecViewAdapter(mContext);

        modulesRecView = view.findViewById(R.id.modulesRecView_today);
        modulesRecView.setAdapter(adapter);
        modulesRecView.setLayoutManager(new LinearLayoutManager(mContext));
        exceptionsHandling();
        adapter.setModules(todayController.getToDay());

        //Add space between cards
        addSpaceBetweenCards();

        return view;
    }

    private void exceptionsHandling() {
        if (todayController.getToDay().isEmpty()){
            String freeMessage = Translation.getTranslation( Translation.POP_UP_NO_APPOINTMENTS_TODAY, Translation.loadMode(mContext));

            Toast.makeText(mContext, freeMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void addSpaceBetweenCards() {
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(30);
        modulesRecView.addItemDecoration(itemDecorator);
    }

}