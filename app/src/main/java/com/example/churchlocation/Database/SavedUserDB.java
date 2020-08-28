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
                + UserDBUtil.KEY_STATE + " TEXT,"
                + UserDBUtil.KEY_FULLNAME + " TEXT,"
                + UserDBUtil.KEY_USERNAME + " TEXT,"
                + UserDBUtil.KEY_DISPLAY_PIC + " TEXT,"
                + UserDBUtil.KEY_BIO_DATA + " TEXT,"
                + UserDBUtil.KEY_DOBIRTH + " TEXT,"
                + UserDBUtil.KEY_DOBAPTISM + " TEXT,"
                + UserDBUtil.KEY_HOBBY + " TEXT,"
                + UserDBUtil.KEY_TITLE + " TEXT,"
                + UserDBUtil.KEY_TITLE_VERIFIED + " TEXT,"
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
        values.put(UserDBUtil.KEY_STATE, userObject.getState());
        values.put(UserDBUtil.KEY_COUNTRY, userObject.getLeaderCountry());
        values.put(UserDBUtil.KEY_FULLNAME, userObject.getFullname());
        values.put(UserDBUtil.KEY_USERNAME, userObject.getUsername());
        values.put(UserDBUtil.KEY_DISPLAY_PIC, userObject.getDisplayPic());
        values.put(UserDBUtil.KEY_BIO_DATA, userObject.getBio());
        values.put(UserDBUtil.KEY_DOBIRTH, userObject.getDateOfBirth());
        values.put(UserDBUtil.KEY_DOBAPTISM, userObject.getDateOfBaptism());
        values.put(UserDBUtil.KEY_HOBBY, userObject.getHobby());
        values.put(UserDBUtil.KEY_TITLE, userObject.getTitle());
        values.put(UserDBUtil.KEY_TITLE_VERIFIED, userObject.isTitleVerification());

        db.insert(UserDBUtil.TABLE_NAME, null, values);
    }

    public UserObject getUser(String uid){
        SQLiteDatabase db = this.getReadableDatabase();
        UserObject userObjectDB = new UserObject();

        ArrayList<UserObject> dummyTestList = new ArrayList<>();

        Cursor cursor = db.query(UserDBUtil.TABLE_NAME,
                new String[]{UserDBUtil.KEY_ID, UserDBUtil.KEY_EMAIL, UserDBUtil.KEY_CHURCH, UserDBUtil.KEY_COUNTRY,
                        UserDBUtil.KEY_UID, UserDBUtil.KEY_STATE, UserDBUtil.KEY_FULLNAME, UserDBUtil.KEY_USERNAME,
                        UserDBUtil.KEY_DISPLAY_PIC, UserDBUtil.KEY_BIO_DATA, UserDBUtil.KEY_DOBIRTH, UserDBUtil.KEY_DOBAPTISM,
                        UserDBUtil.KEY_HOBBY, UserDBUtil.KEY_TITLE,UserDBUtil.KEY_TITLE_VERIFIED,},
                UserDBUtil.KEY_UID + "=? ", new String[]{uid}, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                userObjectDB.setEmail(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_EMAIL)));
                userObjectDB.setChurch(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_CHURCH)));
                userObjectDB.setLeaderCountry(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_COUNTRY)));
                userObjectDB.setId(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_UID)));
                userObjectDB.setState(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_STATE)));
                userObjectDB.setFullname(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_FULLNAME)));
                userObjectDB.setUsername(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_USERNAME)));
                userObjectDB.setDisplayPic(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_DISPLAY_PIC)));
                userObjectDB.setBio(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_BIO_DATA)));
                userObjectDB.setDateOfBirth(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_DOBIRTH)));
                userObjectDB.setDateOfBaptism(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_DOBAPTISM)));
                userObjectDB.setHobby(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_HOBBY)));
                userObjectDB.setTitle(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_TITLE)));
                userObjectDB.setTitleVerification(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_TITLE_VERIFIED)));

//                dummyTestList.add(userObjectDB);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return userObjectDB;
    }


    public ArrayList<UserObject> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        UserObject userObjectDB = new UserObject();

        ArrayList<UserObject> dummyTestList = new ArrayList<>();

        Cursor cursor = db.query(UserDBUtil.TABLE_NAME,
                new String[]{UserDBUtil.KEY_ID, UserDBUtil.KEY_EMAIL, UserDBUtil.KEY_CHURCH, UserDBUtil.KEY_COUNTRY,
                        UserDBUtil.KEY_UID, UserDBUtil.KEY_STATE, UserDBUtil.KEY_FULLNAME, UserDBUtil.KEY_USERNAME,
                        UserDBUtil.KEY_DISPLAY_PIC, UserDBUtil.KEY_BIO_DATA, UserDBUtil.KEY_DOBIRTH, UserDBUtil.KEY_DOBAPTISM,
                        UserDBUtil.KEY_HOBBY, UserDBUtil.KEY_TITLE, UserDBUtil.KEY_TITLE_VERIFIED,},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                userObjectDB.setEmail(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_EMAIL)));
                userObjectDB.setChurch(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_CHURCH)));
                userObjectDB.setLeaderCountry(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_COUNTRY)));
                userObjectDB.setId(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_UID)));
                userObjectDB.setState(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_STATE)));
                userObjectDB.setFullname(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_FULLNAME)));
                userObjectDB.setUsername(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_USERNAME)));
                userObjectDB.setDisplayPic(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_DISPLAY_PIC)));
                userObjectDB.setBio(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_BIO_DATA)));
                userObjectDB.setDateOfBirth(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_DOBIRTH)));
                userObjectDB.setDateOfBaptism(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_DOBAPTISM)));
                userObjectDB.setHobby(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_HOBBY)));
                userObjectDB.setTitle(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_TITLE)));
                userObjectDB.setTitleVerification(cursor.getString(cursor.getColumnIndex(UserDBUtil.KEY_TITLE_VERIFIED)));

                dummyTestList.add(userObjectDB);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return dummyTestList;
    }


    public void deleteUser(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

//        db.execSQL("DELETE FROM " + UserDBUtil.TABLE_NAME);

        db.delete(UserDBUtil.TABLE_NAME, UserDBUtil.KEY_UID + "=?", new String[]{uid});

        db.close();
    }

    public int totalUsers(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + UserDBUtil.TABLE_NAME, null);

        return cursor.getCount();
    }
}
