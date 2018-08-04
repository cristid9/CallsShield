package com.example.cris.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        updateHistory();
        // reupdate the history of blocked calls every 5 seconds
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateHistory();
            }
        }, 5000);
    }

    // HOW DO I BLOCK INCOMING CALLS?

    private void updateHistory() {
        LinearLayout myRoot = (LinearLayout) findViewById(R.id.history);
        myRoot.removeAllViews();
        LinearLayout a = new LinearLayout(this);
        a.setOrientation(LinearLayout.VERTICAL);

        SQLiteDatabase mydatabase = openOrCreateDatabase("block_it.db", MODE_PRIVATE,null);
        Cursor resultSet =
                mydatabase.rawQuery("Select * from History;",null);
        while (resultSet.moveToNext()) {
            String number = resultSet.getString(0);
            String type = resultSet.getString(1);
            TextView tw = new TextView(this.getApplicationContext());
            tw.setText(number + " ==> " + type);
            a.addView(tw);
        }
        myRoot.addView(a);
    }
}
