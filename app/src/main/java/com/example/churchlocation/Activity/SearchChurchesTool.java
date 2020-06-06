package com.example.churchlocation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Filterable;
import android.widget.ImageButton;

import com.example.churchlocation.Adapter.SearchChurchesAdapter;
import com.example.churchlocation.Database.DatabaseHandler;
import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.R;
import com.example.churchlocation.Utils.ConnectToChurchDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearchChurchesTool extends AppCompatActivity {
    private DatabaseHandler db;
    private ConnectToChurchDB connect = new ConnectToChurchDB();

    private Filterable filterable;

    List<SearchChurchModel> churchModelArrayList;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_churches_tool);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = connect.getChurches(this);
        churchModelArrayList = db.getAllContacts();

        ImageButton previousActivity = findViewById(R.id.previousActivity);
        previousActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cardView = findViewById(R.id.allLocationSearch);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchChurchesTool.this, ViewAllMapActivity.class));
            }
        });


        recyclerView = findViewById(R.id.search_church_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SearchChurchesAdapter(getApplicationContext(), churchModelArrayList);
        recyclerView.setAdapter(adapter);


        SearchView searchView = findViewById(R.id.search_churches);
//        searchView = (SearchView) searchItem.getActionView();

        // change the keyboard search button
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                // to get the filter methods
                filterable = (Filterable) adapter;
                filterable.getFilter().filter(s);

                return false;
            }
        });

    }
}
