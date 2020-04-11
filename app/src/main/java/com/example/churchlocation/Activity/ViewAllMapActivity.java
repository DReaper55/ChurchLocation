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

import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.R;
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
import java.util.Random;

        public class ViewAllMapActivity extends AppCompatActivity{
            SearchChurchModel searchChurchModel;
            ArrayList<SearchChurchModel> churchModelArrayList = new ArrayList<>();

            SupportMapFragment mapFragment;

            private BitmapDescriptor[] colors;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_view_all_map);

                getSupportActionBar().hide();

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
                getChurchModel();

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

            private SearchChurchModel getChurchModel() {
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

                        for (int i = 0; i < jsonArray.length(); i++) {
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