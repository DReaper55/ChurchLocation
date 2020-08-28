package com.example.churchlocation.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.Utils.ChurchDBConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class ChurchesDB extends SQLiteOpenHelper {
    private Context context;

    public ChurchesDB(@Nullable Context context) {
        super(context, ChurchDBConstants.DB_NAME, null, ChurchDBConstants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + ChurchDBConstants.TABLE_NAME + "("
                + ChurchDBConstants.KEY_ID + " INTEGER PRIMARY KEY,"
                + ChurchDBConstants.KEY_CHURCH_NAME + " TEXT,"
                + ChurchDBConstants.KEY_PASTOR_NAME + " TEXT,"
                + ChurchDBConstants.KEY_ADDRESS + " TEXT,"
                + ChurchDBConstants.KEY_STATE + " TEXT,"
                + ChurchDBConstants.KEY_COUNTRY + " TEXT,"
                + ChurchDBConstants.KEY_ABOUT + " TEXT,"
                + ChurchDBConstants.KEY_LEADER_NUMBER + " TEXT,"
                + ChurchDBConstants.KEY_REGION + " TEXT,"
                + ChurchDBConstants.KEY_CHURCH_LAT + " TEXT,"
                + ChurchDBConstants.KEY_CHURCH_LONG + " TEXT,"
                + ChurchDBConstants.KEY_CHURCH_LOCATION + " TEXT);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ChurchDBConstants.TABLE_NAME);
        onCreate(db);
    }

    public void addContact(SearchChurchModel searchChurchModelDB){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ChurchDBConstants.KEY_CHURCH_NAME, searchChurchModelDB.getChurchName());
        values.put(ChurchDBConstants.KEY_PASTOR_NAME, searchChurchModelDB.getPastorName());
        values.put(ChurchDBConstants.KEY_ADDRESS, searchChurchModelDB.getAddress());
        values.put(ChurchDBConstants.KEY_STATE, searchChurchModelDB.getState());
        values.put(ChurchDBConstants.KEY_COUNTRY, searchChurchModelDB.getCountry());
        values.put(ChurchDBConstants.KEY_ABOUT, searchChurchModelDB.getAbout());
        values.put(ChurchDBConstants.KEY_LEADER_NUMBER, searchChurchModelDB.getNumber());
        values.put(ChurchDBConstants.KEY_REGION, searchChurchModelDB.getRegion());
        values.put(ChurchDBConstants.KEY_CHURCH_LAT, searchChurchModelDB.getChurchLat());
        values.put(ChurchDBConstants.KEY_CHURCH_LONG, searchChurchModelDB.getChurchLng());
        values.put(ChurchDBConstants.KEY_CHURCH_LOCATION, String.valueOf(searchChurchModelDB.getChurchLocation()));

        db.insert(ChurchDBConstants.TABLE_NAME, null, values);
    }

    public Cursor getContact(String name){
//        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
//        builder.setTables(ChurchDBConstants.TABLE_NAME);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ChurchDBConstants.TABLE_NAME, new String[]{ChurchDBConstants.KEY_ID, ChurchDBConstants.KEY_CHURCH_NAME, ChurchDBConstants.KEY_PASTOR_NAME, ChurchDBConstants.KEY_ADDRESS,
                        ChurchDBConstants.KEY_STATE, ChurchDBConstants.KEY_COUNTRY, ChurchDBConstants.KEY_ABOUT,
                        ChurchDBConstants.KEY_LEADER_NUMBER, ChurchDBConstants.KEY_REGION, ChurchDBConstants.KEY_CHURCH_LAT,
                        ChurchDBConstants.KEY_CHURCH_LONG},
                ChurchDBConstants.KEY_CHURCH_NAME + "=? ", new String[]{name+ "*"}, null, null, null, null
        );

        /*if(cursor == null){
            return  null;
        } else if(!cursor.moveToFirst()){
            cursor.close();
            return null;
        }*/

        return cursor;
    }

    public List<SearchChurchModel> getAllContacts(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<SearchChurchModel> dummyTestList = new ArrayList<>();

        Cursor cursor = db.query(ChurchDBConstants.TABLE_NAME, new String[]{ChurchDBConstants.KEY_ID, ChurchDBConstants.KEY_CHURCH_NAME, ChurchDBConstants.KEY_PASTOR_NAME, ChurchDBConstants.KEY_ADDRESS,
                        ChurchDBConstants.KEY_STATE, ChurchDBConstants.KEY_COUNTRY, ChurchDBConstants.KEY_ABOUT,
                        ChurchDBConstants.KEY_LEADER_NUMBER, ChurchDBConstants.KEY_REGION, ChurchDBConstants.KEY_CHURCH_LAT,
                        ChurchDBConstants.KEY_CHURCH_LONG},
                null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                SearchChurchModel searchChurchModelDB = new SearchChurchModel();
                searchChurchModelDB.setChurchName(cursor.getString(cursor.getColumnIndex(ChurchDBConstants.KEY_CHURCH_NAME)));
                searchChurchModelDB.setPastorName(cursor.getString(cursor.getColumnIndex(ChurchDBConstants.KEY_PASTOR_NAME)));
                searchChurchModelDB.setAddress(cursor.getString(cursor.getColumnIndex(ChurchDBConstants.KEY_ADDRESS)));
                searchChurchModelDB.setState(cursor.getString(cursor.getColumnIndex(ChurchDBConstants.KEY_STATE)));
                searchChurchModelDB.setCountry(cursor.getString(cursor.getColumnIndex(ChurchDBConstants.KEY_COUNTRY)));
                searchChurchModelDB.setAbout(cursor.getString(cursor.getColumnIndex(ChurchDBConstants.KEY_ABOUT)));
                searchChurchModelDB.setNumber(cursor.getString(cursor.getColumnIndex(ChurchDBConstants.KEY_LEADER_NUMBER)));
                searchChurchModelDB.setRegion(cursor.getString(cursor.getColumnIndex(ChurchDBConstants.KEY_REGION)));
                searchChurchModelDB.setChurchLat(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ChurchDBConstants.KEY_CHURCH_LAT))));
                searchChurchModelDB.setChurchLng(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ChurchDBConstants.KEY_CHURCH_LONG))));

                dummyTestList.add(searchChurchModelDB);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return dummyTestList;
    }

    public void deleteContact(String name){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ChurchDBConstants.TABLE_NAME, ChurchDBConstants.KEY_CHURCH_NAME + "=?", new String[]{name});

        db.close();
    }

    public int totalContact(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ChurchDBConstants.TABLE_NAME, null);

        return cursor.getCount();
    }


}
