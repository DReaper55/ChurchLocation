package com.example.churchlocation.Utils;

import android.content.Context;
import android.util.Log;

import com.example.churchlocation.Database.ChurchesDB;
import com.example.churchlocation.Database.LinksDB;
import com.example.churchlocation.Model.SearchChurchModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ConnectToChurchDB {
    private SearchChurchModel searchChurchModelDB = new SearchChurchModel();

    private ChurchesDB db;
    private LinksDB linksDB;

    public ChurchesDB getChurches(Context context){
        FirebaseApp.initializeApp(context);

        db = new ChurchesDB(context);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("churches");

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                searchChurchModelDB = dataSnapshot.getValue(SearchChurchModel.class);

                boolean check = false;

                List<SearchChurchModel> dTest = db.getAllContacts();
                for(int i=0; i<dTest.size();i++){
                    if(searchChurchModelDB.getChurchName().equals(dTest.get(i).getChurchName())){
                        check = true;
                        dTest.clear();
                    }
                }

                if(!check){
                    db.addContact(searchChurchModelDB);
                    dTest.clear();
                }


                Log.d("Check ", String.valueOf(db.totalContact()));
                Log.d("Name ", searchChurchModelDB.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                searchChurchModelDB = dataSnapshot.getValue(SearchChurchModel.class);

                List<SearchChurchModel> dTest = db.getAllContacts();
                for(int i=0; i<dTest.size();i++){
                    if(searchChurchModelDB.getChurchName().equals(dTest.get(i).getChurchName())){
                        db.deleteContact(searchChurchModelDB.getChurchName());
                        db.addContact(searchChurchModelDB);
                        dTest.clear();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                searchChurchModelDB = dataSnapshot.getValue(SearchChurchModel.class);

                List<SearchChurchModel> dTest = db.getAllContacts();
                for(int i=0; i<dTest.size();i++){
                    if(searchChurchModelDB.getChurchName().equals(dTest.get(i).getChurchName())){
                        db.deleteContact(searchChurchModelDB.getChurchName());
                        dTest.clear();
                    }
                }
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

        return db;
    }

    public LinksDB getLinks(Context context){
        FirebaseApp.initializeApp(context);

        linksDB = new LinksDB(context);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference db = firebaseDatabase.getReference("links");
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                s = dataSnapshot.getValue(String.class);

                boolean check = false;

                List<String> dTest = linksDB.getAllContacts();
                for(int i=0; i<dTest.size();i++){
                    if(s.equals(dTest.get(i))){
                        check = true;
                        dTest.clear();
                    }
                }

                if(!check){
                    linksDB.addContact(s);
                    dTest.clear();
                }

                Log.d("Linksss", s);
                Log.d("AnotherC1", String.valueOf(linksDB.totalContact()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                s = dataSnapshot.getValue(String.class);

                List<String> dTest = linksDB.getAllContacts();
                for(int i=0; i<dTest.size();i++){
                    if(s.equals(dTest.get(i))){
                        linksDB.deleteContact(s);
                        linksDB.addContact(s);
                        dTest.clear();
                    }
                }
                Log.d("AnotherC2", String.valueOf(linksDB.totalContact()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String link  = dataSnapshot.getValue(String.class);

                List<String> dTest = linksDB.getAllContacts();
                for(int i=0; i<dTest.size();i++){
                    if(link.equals(dTest.get(i))){
                        linksDB.deleteContact(link);
                        dTest.clear();
                    }
                }
                Log.d("AnotherC3", String.valueOf(linksDB.totalContact()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("Linkss", String.valueOf(linksDB.totalContact()));

        return linksDB;
    }
}
