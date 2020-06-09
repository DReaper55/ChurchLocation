package com.example.churchlocation.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.Utils.UserDBUtil;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class SavedUserDB extends SQLiteOpenHelper {
    private Context context;

    public SavedUserDB(@Nullable Context context) {
        super(context, UserDBUtil.DB_NAME, null, UserDBUtil.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + UserDBUtil.TABLE_NAME + "("
                + UserDBUtil.KEY_ID + " INTEGER PRIMARY KEY,"
                + UserDBUtil.KEY_EMAIL + " TEXT,"
                + UserDBUtil.KEY_CHURCH + " TEXT,"
                + UserDBUtil.KEY_COUNTRY + " TEXT,"
                + UserDBUtil.KEY_UID + " TEXT);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + UserDBUtil.TABLE_NAME);
        onCreate(db);
    }

    public void addUser(UserObject userObject){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserDBUtil.KEY_EMAIL, userObject.getEmail());
        values.put(UserDBUtil.KEY_UID, userObject.getId());
        values.put(UserDBUtil.KEY_CHURCH, userObject.getChurch());
        values.put(UserDBUtil.KEY_COUNTRY, userObject.getLeaderCountry());

        db.insert(UserDBUtil.TABLE_NAME, null, values);
    }

    public UserObject getUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        UserObject userObjectDB = new UserObject();

        ArrayList<UserObject> dummyTestList = new ArrayList<>();

        Cursor cursor = db.query(UserDBUtil.TABLE_NAME,
                new String[]{UserDBUtil.KEY_ID, UserDBUtil.KEY_EMAIL, UserDBUtil.KEY_CHURCH, UserDBUtil.KEY_COUNTRY,
                        UserDBUtil.KEY_UID,},
                null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                userObjectDB.setEmail(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_EMAIL)));
                userObjectDB.setChurch(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_CHURCH)));
                userObjectDB.setLeaderCountry(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_COUNTRY)));
                userObjectDB.setId(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_UID)));

//                dummyTestList.add(userObjectDB);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return userObjectDB;
    }


    public void deleteUsers(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + UserDBUtil.TABLE_NAME);

        db.close();
    }

    public int totalUsers(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + UserDBUtil.TABLE_NAME, null);

        return cursor.getCount();
    }
}
