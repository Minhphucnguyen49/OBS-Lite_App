
package com.hciws22.obslite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hciws22.obslite.db.SqLiteHelper;
import com.hciws22.obslite.sync.SyncController;

public class MainActivity extends AppCompatActivity {

    SyncController syncController = new SyncController(new SqLiteHelper(MainActivity.this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        syncController.init(findViewById(R.id.send));
        move(findViewById(R.id.move));
    }
    public void move(View moveBtn) {
        moveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                startActivity(intent);
            }
        });
    }

}





