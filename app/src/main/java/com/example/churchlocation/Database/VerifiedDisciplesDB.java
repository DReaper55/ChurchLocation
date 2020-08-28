package com.example.churchlocation.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.Utils.VerifiedUsersUtils;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class VerifiedDisciplesDB extends SQLiteOpenHelper {
    private Context context;

    public VerifiedDisciplesDB(@Nullable Context context) {
        super(context, VerifiedUsersUtils.DB_VERIFIED_USERS, null, VerifiedUsersUtils.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + VerifiedUsersUtils.TABLE_NAME + "("
                + VerifiedUsersUtils.KEY_ID + " INTEGER PRIMARY KEY,"
                + VerifiedUsersUtils.KEY_FULLNAME + " TEXT,"
                + VerifiedUsersUtils.KEY_USERNAME + " TEXT,"
                + VerifiedUsersUtils.KEY_DISPLAY_PIC + " TEXT,"
                + VerifiedUsersUtils.KEY_UID + " TEXT);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + VerifiedUsersUtils.TABLE_NAME);
        onCreate(db);
    }

    public void addUser(UserObject userObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VerifiedUsersUtils.KEY_FULLNAME, userObject.getFullname());
        values.put(VerifiedUsersUtils.KEY_UID, userObject.getId());
        values.put(VerifiedUsersUtils.KEY_USERNAME, userObject.getUsername());
        values.put(VerifiedUsersUtils.KEY_DISPLAY_PIC, userObject.getDisplayPic());

        db.insert(VerifiedUsersUtils.TABLE_NAME, null, values);
    }

    public ArrayList<UserObject> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        UserObject userObjectDB = new UserObject();

        ArrayList<UserObject> dummyTestList = new ArrayList<>();

        Cursor cursor = db.query(VerifiedUsersUtils.TABLE_NAME,
                new String[]{VerifiedUsersUtils.KEY_ID, VerifiedUsersUtils.KEY_FULLNAME, VerifiedUsersUtils.KEY_USERNAME,
                        VerifiedUsersUtils.KEY_DISPLAY_PIC,
                        VerifiedUsersUtils.KEY_UID,},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                userObjectDB.setFullname(cursor.getString(cursor.getColumnIndex(VerifiedUsersUtils.KEY_FULLNAME)));
                userObjectDB.setUsername(cursor.getString(cursor.getColumnIndex(VerifiedUsersUtils.KEY_USERNAME)));
                userObjectDB.setDisplayPic(cursor.getString(cursor.getColumnIndex(VerifiedUsersUtils.KEY_DISPLAY_PIC)));
                userObjectDB.setId(cursor.getString(cursor.getColumnIndex(VerifiedUsersUtils.KEY_UID)));

                dummyTestList.add(userObjectDB);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return dummyTestList;
    }

    public UserObject getUser(String uid) {
//        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
//        builder.setTables(VerifiedUsersUtils.TABLE_NAME);

        UserObject userObjectDB = new UserObject();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(VerifiedUsersUtils.TABLE_NAME, new String[]{VerifiedUsersUtils.KEY_ID, VerifiedUsersUtils.KEY_FULLNAME, VerifiedUsersUtils.KEY_USERNAME,
                        VerifiedUsersUtils.KEY_DISPLAY_PIC,
                        VerifiedUsersUtils.KEY_UID,},
                VerifiedUsersUtils.KEY_UID + "=? ", new String[]{uid}, null, null, null, null
        );

        /*if(cursor == null){
            return  null;
        } else if(!cursor.moveToFirst()){
            cursor.close();
            return null;
        }*/

        if (cursor.moveToFirst()) {
            userObjectDB.setEmail(cursor.getString(cursor.getColumnIndex(VerifiedUsersUtils.KEY_FULLNAME)));
            userObjectDB.setChurch(cursor.getString(cursor.getColumnIndex(VerifiedUsersUtils.KEY_USERNAME)));
            userObjectDB.setChurch(cursor.getString(cursor.getColumnIndex(VerifiedUsersUtils.KEY_DISPLAY_PIC)));
            userObjectDB.setId(cursor.getString(cursor.getColumnIndex(VerifiedUsersUtils.KEY_UID)));
        }

        return userObjectDB;
    }

    public void deleteUser(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

//        db.execSQL("DELETE FROM " + VerifiedUsersUtils.TABLE_NAME);

        db.delete(VerifiedUsersUtils.TABLE_NAME, VerifiedUsersUtils.KEY_UID + "=?", new String[]{uid});

        db.close();
    }

    public int totalUsers() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + VerifiedUsersUtils.TABLE_NAME, null);

        return cursor.getCount();
    }
}
