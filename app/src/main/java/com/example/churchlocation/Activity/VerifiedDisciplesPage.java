package com.example.churchlocation.Activity;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.churchlocation.Adapter.SearchUserRequestsAdapter;
import com.example.churchlocation.Database.VerifiedDisciplesDB;
import com.example.churchlocation.Model.UserObject;
import com.example.churchlocation.R;

import java.util.ArrayList;

public class VerifiedDisciplesPage extends Fragment {
    private Context context;
    private View view;
    private ArrayList<UserObject> userObjects;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public VerifiedDisciplesPage() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = inflater.getContext();
        view = container;
        return inflater.inflate(R.layout.fragment_verified_disciples, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        VerifiedDisciplesDB verifiedDisciplesDB = new VerifiedDisciplesDB(context);

        recyclerView = view.findViewById(R.id.mVerifiedUsers);

        if(verifiedDisciplesDB.totalUsers() >= 1){
            userObjects = verifiedDisciplesDB.getAllUsers();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new SearchUserRequestsAdapter(context, userObjects, 2, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }
}
