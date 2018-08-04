package com.example.cris.myapplication;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

import static android.content.Context.MODE_PRIVATE;

public class IncomingCallReceiver extends BroadcastReceiver {
    private ITelephony telephonyService;
    private String blacklistednumber = "+458664455";

    private void updateHistory(String phoneNumber, Context ctx) {
        SQLiteDatabase mydatabase = ctx.openOrCreateDatabase("block_it.db", MODE_PRIVATE,null);
        Log.i("i_statement", "INSERT INTO History VALUES('" + phoneNumber + "', 'sms');");
        mydatabase.execSQL("INSERT INTO History VALUES('" + phoneNumber + "', 'call');");
        mydatabase.close();
    }

    private boolean isBlacklistedNumber(String phoneNumber, Context ctx) {
        Log.v("process_calls", "Is this shit working?");
        SQLiteDatabase mydatabase = ctx.openOrCreateDatabase("block_it.db", MODE_PRIVATE,null);
        Cursor resultSet =
                mydatabase.rawQuery("Select * from CallsNumbersBlacklist where number = '" +
                        phoneNumber + "';",null);

        if (resultSet.moveToFirst() == false) {
            Log.i("process_calls", "Sms sequence successfully blacklisted!");
            mydatabase.close();
            return false;
        }

        Log.v("process_calls", "Valid phone nomber not blacklisted " + phoneNumber);
        mydatabase.close();
        return true;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Log.i("phone_broadcast_received", "The phone broadcast receiver was stared");

            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(tm);
            Bundle bundle = intent.getExtras();
            String phoneNumber = bundle.getString("incoming_number");
            Log.i("phone_broadcast_received", "The phone number is " + phoneNumber);
            Log.e("INCOMING", " " + phoneNumber);

            if (phoneNumber == null) {
                Log.v("null_phone", "Suna cu numar ascuns bulangiu!");
                phoneNumber = "anonymous";
            }

            if (isBlacklistedNumber(phoneNumber, context)) {
                telephonyService.silenceRinger();
                telephonyService.endCall();
                Log.e("HANG UP", phoneNumber);
                updateHistory(phoneNumber, context);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}