
package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moveToDo(findViewById(R.id.move));
        moveToday(findViewById(R.id.move_today));
        moveSettings(findViewById(R.id.move_settings));
    }

    public void moveToDo(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TodoActivity.class);
            startActivity(intent);
        });
    }

    public void moveToday(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TodayActivity.class);
            startActivity(intent);
        });
    }

    public void moveSettings(View moveBtn) {
        moveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }



}





