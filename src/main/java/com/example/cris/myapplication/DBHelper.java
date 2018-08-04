package com.example.cris.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "block_it.db";
    public static final String TABLE_SMS_NUMBERS_BLACKLIST = "SmsNumbersBlacklist";
    public static final String TABLE_SMS_SEQUENCE_BLACKLIST = "SmsSequencesBlacklist";
    public static final String TABLE_CALLS_NUMBERS_BLACKLIST = "SmsSequencesBlacklist";
    public static final String TABLE_CALLS_BLOCK_ANONYMOUS = "SmsSequencesBlacklist";
    public static final String TABLE_HISTORY = "History";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS SmsNumbersBlacklist(number VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SmsSequencesBlacklist(sequence VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS CallsNumbersBlacklist(number VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS BlockAnonymous(block BOOLEAN)");
        db.execSQL("CREATE TABLE IF NOT EXISTS History(number VARCHAR, type VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS SmsNumbersBlacklist");
        db.execSQL("DROP TABLE IF EXISTS SmsSequencesBlacklist");
        db.execSQL("DROP TABLE IF EXISTS CallsNumbersBlacklist");
        db.execSQL("DROP TABLE IF EXISTS BlockAnonymous");
        db.execSQL("DROP TABLE IF EXISTS History");
        onCreate(db);
    }

    public boolean insertContact (String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.insert("contacts", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id=" + id + "", null );
        return res;
    }

//    public int numberOfRows(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
//        return numRows;
//    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
//            res.moveToNext();
//        }
        return array_list;
    }
}