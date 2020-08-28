package com.example.churchlocation.Activity;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.churchlocation.Adapter.SearchUserRequestsAdapter;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiscipleRequestPage extends Fragment {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String uid = firebaseAuth.getUid();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<UserObject> userObjectList = new ArrayList<>();

    private View view;
    private Context ctx;

    public DiscipleRequestPage() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = container;
        ctx = inflater.getContext();

        return inflater.inflate(R.layout.fragment_disciple_request_page, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DatabaseReference db = firebaseDatabase.getReference("users/" + uid + "/church");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String church = dataSnapshot.getValue().toString();

                    DatabaseReference db = firebaseDatabase.getReference("discipleVerification/" + church);
                    db.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists()){
                                UserObject userObject = dataSnapshot.getValue(UserObject.class);
                                Log.d("LoginTAG", userObject.getId());

                                userObjectList.add(userObject);
                                recyclerView = view.findViewById(R.id.discipleRequestRecycler);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(ctx));

                                adapter = new SearchUserRequestsAdapter(ctx, userObjectList, 0, recyclerView);
                                recyclerView.setAdapter(adapter);
                            }
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
