package com.example.churchlocation.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.churchlocation.Utils.LinksUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class LinksDB extends SQLiteOpenHelper {
    private Context context;

    public LinksDB(@Nullable Context context) {
        super(context, LinksUtil.DB_NAME, null, LinksUtil.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + LinksUtil.TABLE_NAME + "("
                + LinksUtil.KEY_ID + " INTEGER PRIMARY KEY,"
                + LinksUtil.KEY_LINK_NAME + " TEXT);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LinksUtil.TABLE_NAME);
        onCreate(db);
    }

    public void addContact(String link){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LinksUtil.KEY_LINK_NAME, link);

        db.insert(LinksUtil.TABLE_NAME, null, values);
    }

    public List<String> getAllContacts(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> dummyTestList = new ArrayList<>();

        Cursor cursor = db.query(LinksUtil.TABLE_NAME, new String[]{LinksUtil.KEY_ID, LinksUtil.KEY_LINK_NAME},
                null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String link = cursor.getString(cursor.getColumnIndex(LinksUtil.KEY_LINK_NAME));

                dummyTestList.add(link);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return dummyTestList;
    }

    public void deleteContact(String name){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(LinksUtil.TABLE_NAME, LinksUtil.KEY_LINK_NAME + "=?", new String[]{name});

        db.close();
    }

    public int totalContact(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + LinksUtil.TABLE_NAME, null);

        return cursor.getCount();
    }

}
