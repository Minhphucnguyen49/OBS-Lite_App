package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WeekActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week);
        moveToday(findViewById(R.id.button_to_today));
    }

    public void moveToday(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WeekActivity.this, TodayActivity.class);
            startActivity(intent);
        });
    }
}