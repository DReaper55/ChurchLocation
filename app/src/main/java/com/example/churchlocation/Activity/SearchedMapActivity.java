package com.example.churchlocation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SearchedMapActivity extends AppCompatActivity {
    SearchChurchModel searchChurchModel;
    ArrayList<SearchChurchModel> churchModelArrayList = new ArrayList<>();

    TextView name_of_church, church_leader_name, church_members_number,
            church_address, church_country_and_state, church_lat_long, church_details;

    ImageView call_church_leader, church_leader_dm;

    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_map);

        getSupportActionBar().hide();

        name_of_church = findViewById(R.id.name_of_church);
        church_leader_name = findViewById(R.id.church_leader_name);
        church_members_number = findViewById(R.id.church_members_number);
        church_address = findViewById(R.id.church_address);
        church_country_and_state = findViewById(R.id.church_country);
        church_lat_long = findViewById(R.id.church_lat_long);
        church_details = findViewById(R.id.church_details);

        call_church_leader = findViewById(R.id.call_church_leader);

        setUp();
    }

    private void setUp(){
        final String intent = getIntent().getExtras().getString("shower");
        getChurchModel();

//        Log.d("Stuffss", intent);

        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.search_map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for(final SearchChurchModel sm : churchModelArrayList){
                    if(sm.getChurchName().equals(intent)){
                        // rename every textView

                        name_of_church.setText(sm.getChurchName());
                        church_leader_name.setText(sm.getPastorName());
                        church_members_number.setText(sm.getDisciples());
                        church_address.setText(sm.getAddress());

                        String churchCountry = sm.getCountry() + ", " + sm.getState();
                        church_country_and_state.setText(churchCountry);

                        String churchLatandLong = sm.getChurchLat() + ", " + sm.getChurchLng();
                        church_lat_long.setText(churchLatandLong);

//                        church_details.setText(sm.getAbout());

                        googleMap.addMarker(new MarkerOptions().position(new LatLng(sm.getChurchLat(), sm.getChurchLng())));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sm.getChurchLat(), sm.getChurchLng()), 18));


                        call_church_leader.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + sm.getNumber()));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (view.getContext().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(SearchedMapActivity.this,
                                                new String[]{Manifest.permission.CALL_PHONE}, 1);

                                    } else {
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
                    }

                }
            }
        });

    }

    private SearchChurchModel getChurchModel(){
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

        return searchChurchModel;
    }
}
