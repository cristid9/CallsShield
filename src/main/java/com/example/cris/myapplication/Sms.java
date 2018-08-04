package com.example.cris.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Sms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        Button smsBlacklistedNumbers = (Button) findViewById(R.id.sms_blacklist_numbers);
        smsBlacklistedNumbers.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Sms.this, SmsNumbersBlacklist.class);
                startActivity(i);
            }
        });

        Button smsBlacklistedWords = (Button) findViewById(R.id.sms_blacklisted_words);
        smsBlacklistedWords.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Sms.this, SmsWordsBlacklist.class);
                startActivity(i);
            }
        });
    }
}
