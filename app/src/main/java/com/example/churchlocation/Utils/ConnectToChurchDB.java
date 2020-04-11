package com.example.churchlocation.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.churchlocation.Database.DatabaseHandler;
import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ConnectToChurchDB {
    private List<SearchChurchModel> searchChurchesList = new ArrayList<>();
    private SearchChurchModel searchChurchModelDB = new SearchChurchModel();


    private DatabaseHandler db;

    public void getChurches(Context context){
        FirebaseApp.initializeApp(context);

        db = new DatabaseHandler(context);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("churches");

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                searchChurchModelDB = dataSnapshot.getValue(SearchChurchModel.class);

                boolean check = false;

                List<SearchChurchModel> dTest = db.getAllContacts();
                searchChurchesList.addAll(dTest);
                for(int i=0; i<searchChurchesList.size();i++){
                    if(searchChurchModelDB.getChurchName().equals(searchChurchesList.get(i).getChurchName())){
                        check = true;
                        searchChurchesList.clear();
                    }
                }

                if(!check){
                    db.addContact(searchChurchModelDB);
                    searchChurchesList.clear();
                }


//                Log.d("Check ", String.valueOf(db.totalContact()));
//                Log.d("Name ", searchChurchModelDB.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("Names ", String.valueOf(db.totalContact()));

        List<SearchChurchModel> dTest = db.getAllContacts();
        for(SearchChurchModel dTestIt : dTest){
            Log.d("Names ", dTestIt.toString());
        }

    }
}
