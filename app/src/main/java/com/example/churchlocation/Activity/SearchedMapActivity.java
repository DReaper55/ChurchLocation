package com.example.churchlocation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.churchlocation.Database.ChurchesDB;
import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.R;
import com.example.churchlocation.Utils.ConnectToChurchDB;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class SearchedMapActivity extends AppCompatActivity {
    List<SearchChurchModel> churchModelArrayList;

    private ChurchesDB db;
    private ConnectToChurchDB connect = new ConnectToChurchDB();

    TextView name_of_church, church_leader_name, church_members_number,
            church_address, church_country_and_state, church_lat_long, church_details;

    ImageView call_church_leader, church_leader_dm;

    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_map);

        getSupportActionBar().hide();

        findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        name_of_church = findViewById(R.id.name_of_church);
        church_leader_name = findViewById(R.id.church_leader_name);
        church_members_number = findViewById(R.id.church_members_number);
        church_address = findViewById(R.id.church_address);
        church_country_and_state = findViewById(R.id.church_country);
        church_lat_long = findViewById(R.id.church_lat_long);
        church_details = findViewById(R.id.church_details);

        call_church_leader = findViewById(R.id.call_church_leader);

        db = connect.getChurches(this);
        churchModelArrayList = db.getAllContacts();

        setUp();
    }

    private void setUp(){
        final String intent = getIntent().getExtras().getString("shower");

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

                        church_details.setText(sm.getAbout());

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

}
