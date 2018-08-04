package com.example.cris.myapplication;

// let the monkey coding begin

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SmsWordsBlacklist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_words_blacklist);

        displayBlackListed();

        Button addBlNumber = (Button) findViewById(R.id.blacklist_sequence);
        addBlNumber.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // should add the number in the database if it wasn't added before, ofcorse
                Log.v("blacklisted_sms_numbers", "Detected attempt to add a blacklisted number!");
                TextView tw = (TextView) findViewById(R.id.sms_add_blacklisted_sequence);
                blacklistSequence(tw.getText().toString());
            }
        });
    }

    private void displayBlackListed() {
        LinearLayout myRoot = (LinearLayout) findViewById(R.id.layout_blacklisted_sms_sequences);
        myRoot.removeAllViews();
        LinearLayout a = new LinearLayout(this);
        a.setOrientation(LinearLayout.VERTICAL);

        final SQLiteDatabase mydatabase = openOrCreateDatabase("block_it.db", MODE_PRIVATE,null);
        Cursor resultSet =
                mydatabase.rawQuery("Select * from SmsSequencesBlacklist;",null);

        while (resultSet.moveToNext()) {
            final String number = resultSet.getString(0);
            TextView tw = new TextView(this.getApplicationContext());
            tw.setText(number);

            LinearLayout group = new LinearLayout(this);
            group.setOrientation(LinearLayout.HORIZONTAL);

            Button removeButton = new Button(this);
            removeButton.setText("Remove");
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydatabase.execSQL("DELETE FROM SmsSequencesBlacklist WHERE sequence ='" + number + "';");
                    displayBlackListed();
                }
            });

            group.addView(tw);
            group.addView(removeButton);

            a.addView(group);
        }
        myRoot.addView(a);
    }

    private void blacklistSequence(String sequence) {
        SQLiteDatabase mydatabase = openOrCreateDatabase("block_it.db",MODE_PRIVATE,null);
        Cursor resultSet =
                mydatabase.rawQuery("Select * from SmsSequencesBlacklist where sequence = '" +
                        sequence + "';",null);
        if (resultSet.moveToFirst() == false) {
            mydatabase.execSQL("INSERT INTO SmsSequencesBlacklist VALUES('" + sequence +"');");
            Log.v("blacklisting_process", "Sms sequence successfully blacklisted!");
            mydatabase.close();
            displayBlackListed();
        } else {
            Toast.makeText(this, "The sequence is already blacklisted",
                    Toast.LENGTH_LONG).show();
        }
    }
}
