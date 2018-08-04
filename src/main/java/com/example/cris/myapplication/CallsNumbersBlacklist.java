package com.example.cris.myapplication;

import android.annotation.SuppressLint;
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

public class CallsNumbersBlacklist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls_numbers_blacklist);
        displayBlackListed();

        Button addBlNumber = (Button) findViewById(R.id.add_new_calls_blacklist_item);
        addBlNumber.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                // should add the number in the database if it wasn't added before, ofcorse
                Log.v("blacklisted_calls_number", "Detected attempt to add a blacklisted number!");
                TextView tw = (TextView) findViewById(R.id.new_blacklisted_calls_number);
                blacklistNumber(Integer.parseInt(tw.getText().toString()) );
            }
        });
    }

    private void displayBlackListed() {
        LinearLayout myRoot = (LinearLayout) findViewById(R.id.layout_blacklisted_calls_numbers);
        myRoot.removeAllViews();
        LinearLayout a = new LinearLayout(this);
        a.setOrientation(LinearLayout.VERTICAL);

        final SQLiteDatabase mydatabase = openOrCreateDatabase("block_it.db", MODE_PRIVATE,null);
        Cursor resultSet =
                mydatabase.rawQuery("Select * from CallsNumbersBlacklist;",null);

        while (resultSet.moveToNext()) {
            final String number = resultSet.getString(0);

            LinearLayout group = new LinearLayout(this);
            group.setOrientation(LinearLayout.HORIZONTAL);
            TextView tw = new TextView(this.getApplicationContext());
            tw.setText(number);

            Button removeButton = new Button(this);
            removeButton.setText("Remove");
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydatabase.execSQL("DELETE FROM CallsNumbersBlacklist WHERE number ='" + number + "';");
                    displayBlackListed();
                }
            });

            group.addView(tw);
            group.addView(removeButton);

            a.addView(group);
        }
        myRoot.addView(a);
    }

    private void blacklistNumber(int number) {
        SQLiteDatabase mydatabase = openOrCreateDatabase("block_it.db",MODE_PRIVATE,null);
        Cursor resultSet =
                mydatabase.rawQuery("Select * from CallsNumbersBlacklist where number = '" +
                        number + "';",null);
        if (resultSet.moveToFirst() == false) {
            mydatabase.execSQL("INSERT INTO CallsNumbersBlacklist VALUES('" + number +"');");
            Log.v("blacklisting_process", "Sms number successfuly blacklisted!");
            mydatabase.close();
            displayBlackListed();
        } else {
            Toast.makeText(this, "The number is already blacklisted",
                    Toast.LENGTH_LONG).show();
        }
    }
}
