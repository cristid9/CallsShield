package com.example.cris.myapplication;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class SmsListener extends BroadcastReceiver {

    private boolean isBlacklistedNumber(String phoneNumber, Context ctx) {
        SQLiteDatabase mydatabase = ctx.openOrCreateDatabase("block_it.db", MODE_PRIVATE,null);
        Cursor resultSet =
                mydatabase.rawQuery("Select * from SmsNumbersBlacklist where number = '" +
                        phoneNumber + "';",null);
        if (resultSet.moveToFirst() == false) {
            Log.v("blacklisting_process", "Sms sequence successfully blacklisted!");
            mydatabase.close();
            return false;
        }

        Log.v("blacklisting_process", "Valid phone nomber not blacklisted " + phoneNumber);
        mydatabase.close();
        return true;
    }

    private boolean isBlacklistedText(String smsBody, Context ctx) {
        SQLiteDatabase mydatabase = ctx.openOrCreateDatabase("block_it.db", MODE_PRIVATE,null);
        Cursor resultSet =
                mydatabase.rawQuery("Select * from SmsSequencesBlacklist where sequence = '" +
                        smsBody + "';",null);
        if (resultSet.moveToFirst() == false) {
            Log.v("blacklisting_process", "Attempt to blacklist sms with body failed!==>" + smsBody);
            mydatabase.close();
            return false;
        }

        Log.v("blacklisting_process", "Attempt to blacklist sms with body failed!==>" + smsBody);
        mydatabase.close();
        return true;
    }

    private void updateHistory(String phoneNumber, Context ctx) {
        SQLiteDatabase mydatabase = ctx.openOrCreateDatabase("block_it.db", MODE_PRIVATE,null);
        Log.i("i_statement", "INSERT INTO History VALUES('" + phoneNumber + "', 'sms');");
        mydatabase.execSQL("INSERT INTO History VALUES('" + phoneNumber + "', 'sms');");
        mydatabase.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.v("custom_sms", "Here I am: " + intent.getAction());
        if (Telephony.Sms.Intents.SMS_DELIVER_ACTION.equals(intent.getAction())) {
            for (final SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                Log.v("custom_sms", "==========================================");
                Log.v("custom_sms", "new text message" + messageBody);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("custom_sms", "Ba da chestia asta de ce plm nu merge?");
                        ContentValues values = new ContentValues();
                        values.put(Telephony.Sms.ADDRESS, smsMessage.getDisplayOriginatingAddress());
                        values.put(Telephony.Sms.BODY, smsMessage.getMessageBody());

                        if (!isBlacklistedNumber(smsMessage.getDisplayOriginatingAddress(), context)
                                && !isBlacklistedText(smsMessage.getMessageBody(), context)) {
                            context.getApplicationContext().getContentResolver().insert(Telephony.Sms.Sent.CONTENT_URI, values);
                            Log.i("misc", "Why isn't the toast displaying?!");
                            Toast.makeText(context, "Sms successfully filtered!", Toast.LENGTH_LONG).show();
                        } else {

                            updateHistory(smsMessage.getDisplayOriginatingAddress(), context);
                            // should update the database with blocked things
                            Toast.makeText(context, "Unwanted sms successfully blocked!", Toast.LENGTH_LONG).show();
                        }
                        Log.v("custom_sms", "Gata bossule l-am sters pe nemernic");
                    }
                }, 1000);
            }
        }
    }
}