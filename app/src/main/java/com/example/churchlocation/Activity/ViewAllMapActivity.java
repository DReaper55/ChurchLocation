package com.example.churchlocation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.churchlocation.Database.DatabaseHandler;
import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.R;
import com.example.churchlocation.Utils.ConnectToChurchDB;
import com.example.churchlocation.Utils.ConstantsRandom;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import java.util.List;
import java.util.Random;

        public class ViewAllMapActivity extends AppCompatActivity{
            private DatabaseHandler db;
            private ConnectToChurchDB connect = new ConnectToChurchDB();

            List<SearchChurchModel> churchModelArrayList;

            SupportMapFragment mapFragment;

            private BitmapDescriptor[] colors;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_view_all_map);

                getSupportActionBar().hide();

                db = connect.getChurches(this);
                churchModelArrayList = db.getAllContacts();

                findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                setUp();

                colors = new BitmapDescriptor[]{
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
                };

            }

            private void setUp() {

                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.viewAllMapsFragment);
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        for (SearchChurchModel sm : churchModelArrayList) {
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sm.getChurchLat(), sm.getChurchLng()), 18));

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.title(sm.getChurchName());
                            markerOptions.position(new LatLng(sm.getChurchLat(), sm.getChurchLng()));
                            markerOptions.icon(colors[ConstantsRandom.Constants.random(colors.length, 0)]);
                            markerOptions.snippet("click for more info");

                            googleMap.addMarker(markerOptions);
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sm.getChurchLat(), sm.getChurchLng()), 3));
                        }

                        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                for(int i = 0; i < churchModelArrayList.size(); i++){
                                    if(churchModelArrayList.get(i).getChurchName().equals(marker.getTitle())){
                                        ChurchDetailsBottomSheet bottomSheet = new ChurchDetailsBottomSheet();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("church_title", marker.getTitle());
                                        bottomSheet.setArguments(bundle);
                                        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

                                    }
                                }
                            }
                        });
                    }
                });
            }

        }