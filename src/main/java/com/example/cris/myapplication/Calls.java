package com.example.cris.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Calls extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);

        Button blCallsButton = (Button) findViewById(R.id.calls_blacklist_numbers);
        blCallsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("misc", "started calls activity");
                Intent i = new Intent(Calls.this, CallsNumbersBlacklist.class);
                startActivity(i);
            }
        });

        CheckBox satView = findViewById(R.id.block_anonymous);
        satView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SQLiteDatabase mydatabase = openOrCreateDatabase("block_it.db", MODE_PRIVATE,null);
                if (buttonView.isChecked()) {
                    Log.i("checkbox", "toggle block");
                    mydatabase.execSQL("INSERT INTO CallsNumbersBlacklist VALUES('anonymous');");
                } else {
                    Log.i("checkbox", "toggle unblock");
                    mydatabase.execSQL("DELETE FROM CallsNumbersBlacklist WHERE number='anonymous';");
                }
                mydatabase.close();
            }
        });
    }
}
