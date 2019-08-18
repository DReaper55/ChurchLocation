package com.example.churchlocation.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.churchlocation.Model.ChurchLocation;
import com.example.churchlocation.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private ArrayList<ChurchLocation> churchLocationList = new ArrayList<>();
    private ArrayList<String> churchLongitude = new ArrayList<>();
    private ArrayList<Double> distanceList = new ArrayList<>();

    ChurchLocation churchLocation = new ChurchLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getJsonObject();

        getChurchLocations();

        dummyTest();
    }


    private void dummyTest(){
        double distance;
        Location locationA = new Location("Point A");
        locationA.setLatitude(4.955037);
        locationA.setLongitude(8.345457);

        Location locationB = new Location("Point B");
        locationB.setLatitude(6.624966);
        locationB.setLongitude(3.548801);

// distance = locationA.distanceTo(locationB);   // in meters
        distance = locationA.distanceTo(locationB)/1000;
        Log.println(Log.INFO, "Dummy ", String.valueOf(distance));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                for(ChurchLocation churchIterator : churchLocationList){

//                    getMyLocation(location);

//                    Location pointA = new Location(LocationManager.GPS_PROVIDER);
//                    Location myLocation = getMyLocation(pointA);

                    Location startPoint = new Location(LocationManager.GPS_PROVIDER);
                    startPoint.setLatitude(location.getLatitude());
                    startPoint.setLongitude(location.getLongitude());

                    Location endPoint = new Location(LocationManager.GPS_PROVIDER);
                    endPoint.setLatitude(churchIterator.getChurchLat());
                    endPoint.setLongitude(churchIterator.getChurchLng());

                    double result = startPoint.distanceTo(endPoint);
                    Log.println(Log.INFO, "Distances ", String.valueOf(result));
                    Log.println(Log.INFO, "Distances2 ", location.toString());

                    distanceList.add(result);
//                    Log.println(Log.INFO, "Distances2 ", churchIterator.getMyLocation() + " " + churchIterator.getChurchLocation());
//                    Log.println(Log.INFO, "Distances3 ", String.valueOf(churchIterator.getChurchLat()));
//                    Log.println(Log.INFO, "Distances4 ", location.toString());

//                    getDistances(location, churchLocation);

//                    Log.println(Log.INFO, "ChurchDistances", distances.toString());
                }
                getShortestDistance();

//                Log.println(Log.INFO, "Churches", String.valueOf(churchLocation.getChurchLat()));
            }



            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(Build.VERSION.SDK_INT < 23){
            return;
        } else {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else{

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }
    }

    private void getShortestDistance() {
        for(int i =0; i < distanceList.size(); i++){
            Collections.sort(distanceList);
            Log.println(Log.INFO, "Shortest2 ", String.valueOf(distanceList.get(i)));
        }

    }


        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }

        }
    }

    public void getJsonObject(){
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

//                    churchLocationList.add(name.getString("name"));
//                    Log.println(Log.INFO, "Names ", name.getString("name"));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Location getMyLocation(Location location){
        Log.println(Log.INFO, "Location: ", location.toString());
        mMap.clear();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses != null && addresses.size() > 0) {
                Log.println(Log.INFO, "Address ", addresses.get(0).toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        churchLocation.setMyLocation(location);

        return location;
    }

    private ChurchLocation getChurchLocations(){
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

                    churchLocation = new ChurchLocation();

                    churchLocation.setChurchLat(Double.parseDouble(jsonObject.getString("latitude")));
                    churchLocation.setChurchLng(Double.parseDouble(jsonObject.getString("longitude")));

                    double latitude = churchLocation.getChurchLat();
                    double longitude = churchLocation.getChurchLng();

//                    churchLocation.setChurchLocation(new Location(churchLocation.getChurchLat(), churchLocation.getChurchLng()));

                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLatitude(churchLocation.getChurchLat());
                    location.setLongitude(churchLocation.getChurchLng());

                    churchLocation.setChurchLocation(location);

                    churchLocationList.add(new ChurchLocation(latitude, longitude, location));
//                    Location location = new Location("Point A");
//                    getDistances(getMyLocation(location), churchLocation);
                }
                Log.println(Log.INFO, "ChurchLocation ", churchLocationList.toString());
//                ChurchLocation location = new ChurchLocation();
//                location = churchLocationList.getClass();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return churchLocation;
    }

    private void getDistances(){
        for(int i = 0; i < churchLocationList.size(); i++){
            Location pointA = new Location(LocationManager.GPS_PROVIDER);
            Location myLocation = getMyLocation(pointA);

            Location startPoint = new Location(LocationManager.GPS_PROVIDER);
            startPoint.setLatitude(myLocation.getLatitude());
            startPoint.setLongitude(myLocation.getLongitude());

            churchLocation = getChurchLocations();

            Location endPoint = new Location(LocationManager.GPS_PROVIDER);
            endPoint.setLatitude(churchLocation.getChurchLat());
            endPoint.setLongitude(churchLocation.getChurchLng());

            String result = String.valueOf(startPoint.distanceTo(endPoint));
            Log.println(Log.INFO, "Distances ", result);
        }


    }

}
