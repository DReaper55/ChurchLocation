package com.example.churchlocation.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.Utils.UserDBUtil;

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

        db.insert(UserDBUtil.TABLE_NAME, null, values);
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
