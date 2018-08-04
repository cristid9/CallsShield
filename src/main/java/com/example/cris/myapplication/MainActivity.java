package com.example.cris.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the database
//        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase mydatabase = openOrCreateDatabase("block_it.db", MODE_PRIVATE,null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS SmsNumbersBlacklist(number VARCHAR)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS SmsSequencesBlacklist(sequence VARCHAR)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS CallsNumbersBlacklist(number VARCHAR)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS BlockAnonymous(block BOOLEAN)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS History(number VARCHAR, type VARCHAR)");


        Button smsButton = (Button) findViewById(R.id.sms);
        smsButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Sms.class);
                startActivity(i);
            }
        });

        Button callsButton = (Button) findViewById(R.id.calls);
        callsButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Calls.class);
                startActivity(i);
            }
        });

        Button historyButton = (Button) findViewById(R.id.history);
        historyButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, History.class);
                startActivity(i);
            }
        });
    }
}
