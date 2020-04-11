package com.example.churchlocation.Activity;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageButton;

import com.example.churchlocation.Adapter.SearchChurchesAdapter;
import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SearchChurchesTool extends AppCompatActivity {
    SearchChurchModel searchChurchModel;
    ArrayList<SearchChurchModel> churchModelArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_churches_tool);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getChurchModel();

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

    }

    private void getChurchModel(){
        String json;

        try {
            InputStream jObject = getAssets().open("some.json");
            int size = jObject.available();
            byte[] buffer = new byte[size];
            jObject.read(buffer);
            jObject.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                json = new String(buffer, StandardCharsets.UTF_8);
                JSONArray jsonArray = new JSONArray(json);

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    searchChurchModel = new SearchChurchModel();

                    searchChurchModel.setChurchLat(Double.parseDouble(jsonObject.getString("latitude")));
                    searchChurchModel.setChurchLng(Double.parseDouble(jsonObject.getString("longitude")));

                    searchChurchModel.setChurchName(jsonObject.getString("name"));

                    searchChurchModel.setAbout(jsonObject.getString("about"));
                    searchChurchModel.setState(jsonObject.getString("state"));
                    searchChurchModel.setRegion(jsonObject.getString("region"));
                    searchChurchModel.setPastorName(jsonObject.getString("leader"));
                    searchChurchModel.setNumber(jsonObject.getString("number"));
                    searchChurchModel.setAddress(jsonObject.getString("address"));
                    searchChurchModel.setDisciples(jsonObject.getString("disciples"));
                    searchChurchModel.setCountry(jsonObject.getString("country"));

                    double latitude = searchChurchModel.getChurchLat();
                    double longitude = searchChurchModel.getChurchLng();
                    String churchName = searchChurchModel.getChurchName();
                    String aboutChurch = searchChurchModel.getAbout();
                    String churchState = searchChurchModel.getState();
                    String churchRegion = searchChurchModel.getRegion();
                    String churchLeader = searchChurchModel.getPastorName();
                    String churchLeaderNumber = searchChurchModel.getNumber();
                    String churchAddress = searchChurchModel.getAddress();
                    String churchDisciples = searchChurchModel.getDisciples();
                    String churchCountry = searchChurchModel.getCountry();

//                    searchChurchModel.setChurchLocation(new Location(searchChurchModel.getChurchLat(), searchChurchModel.getChurchLng()));

                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLatitude(searchChurchModel.getChurchLat());
                    location.setLongitude(searchChurchModel.getChurchLng());
//                    mMap.addMarker(new MarkerOp().position(new LatLng(searchChurchModel.getChurchLat(), searchChurchModel.getChurchLng())).title(searchChurchModel.getChurchName()));


                    searchChurchModel.setChurchLocation(location);

                    churchModelArrayList
                            .add(new SearchChurchModel(churchName, churchRegion, churchLeader, churchAddress, churchState,
                                    churchCountry, aboutChurch, churchLeaderNumber, churchDisciples, latitude, longitude, location));
//                    Location location = new Location("Point A");
//                    getDistances(getMyLocation(location), searchChurchModel);

//                    Log.println(Log.INFO, "SearchChurchModel ", searchChurchModel.toString());

                }
//                SearchChurchModel location = new SearchChurchModel();
//                location = churchModelArrayList.getClass();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
