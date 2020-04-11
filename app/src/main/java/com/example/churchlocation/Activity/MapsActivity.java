package com.example.churchlocation.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.churchlocation.Model.ChurchDistance;
import com.example.churchlocation.Model.ChurchLocation;
import com.example.churchlocation.Utils.LatLngInterpolator;
import com.example.churchlocation.Model.MarkerAnimation;
import com.example.churchlocation.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import static android.content.Context.LOCATION_SERVICE;

public class MapsActivity extends Fragment {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private ArrayList<ChurchLocation> churchLocationList = new ArrayList<>();
    private ArrayList<Double> distanceList = new ArrayList<>();
    private ArrayList<ChurchDistance> ChurchDistanceList = new ArrayList<>();

    private View view;
    private Context ctx;

    static int counter;

    SupportMapFragment mapFragment;

    ChurchLocation churchLocation = new ChurchLocation();

    AlertDialog.Builder alertBuilder;

    
    public MapsActivity() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        counter += 1;

        Log.d("Created", "Paused " + counter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.activity_maps, null);

       ctx = root.getContext();
        view = root.getRootView();

        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.clear();

                view = getView();

                FloatingActionButton nearestChurch = getActivity().findViewById(R.id.findNearestChurch);
                nearestChurch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "";
                        getShortestDistance(name);
                    }
                });

//                TODO: Update module
                locationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);
                locationListener = new LocationListener() {

                    @Override
                    public void onLocationChanged(final Location location) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .icon(bitmapDescriptor(view.getContext(), R.drawable.map_current_local)));

                        Marker mCurrLocationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .icon(bitmapDescriptor(view.getContext(), R.drawable.map_current_local)));

                                /*mMap.addCircle(circleOptions.center(new LatLng(location.getLatitude(), location.getLongitude()))
                                .radius(3000)
                                .fillColor(Color.BLUE)
                                .strokeWidth(3.6f));
*/
                                if(mCurrLocationMarker != null){
                                    mCurrLocationMarker.remove();

                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    MarkerAnimation.animateMarkerToGB(mCurrLocationMarker, latLng, new LatLngInterpolator.Spherical());

//                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                                }


// TODO: Change the location variable to the lastKnownLocation
                        for(ChurchLocation churchIterator : churchLocationList){
                            Location startPoint = new Location(LocationManager.GPS_PROVIDER);
                            startPoint.setLatitude(location.getLatitude());
                            startPoint.setLongitude(location.getLongitude());

                            Location endPoint = new Location(LocationManager.GPS_PROVIDER);
                            endPoint.setLatitude(churchIterator.getChurchLat());
                            endPoint.setLongitude(churchIterator.getChurchLng());

                            double result = startPoint.distanceTo(endPoint);
                            Log.println(Log.INFO, "Distances ", String.valueOf(result));

                            ChurchDistanceList.add(new ChurchDistance(result, churchIterator.getChurchName(), churchIterator.getChurchLat(), churchIterator.getChurchLng()));

//                    Log.println(Log.INFO, "Showw2", ChurchDistanceList.toString());

                            distanceList.add(result);

//                    Log.println(Log.INFO, "Distances2 ", String.valueOf(churchIterator.getChurchLocation()));

                    /*Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        if (addresses != null && addresses.size() > 0) {
                            Log.println(Log.INFO, "Address ", addresses.get(0).toString());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                            FloatingActionButton myLocation = view.findViewById(R.id.findMyLocation);
                            myLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 7));
                                }
                            });

                        }

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
                    if(ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(MapsActivity.this.getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    } else{

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    }

                }


            }
        });

       return root;
    }

    private BitmapDescriptor bitmapDescriptor(Context context, int vectorResID){
        Drawable drawable = ContextCompat.getDrawable(context, vectorResID);
        drawable.setBounds(0,0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getJsonObject();

        getChurchLocations();

        buttonFunctions();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(counter==1){
                    alertDialog();
                }
            }
        }, 5000);
    }

    private void buttonFunctions(){
        ImageView searchLocation = getView().findViewById(R.id.searchLocation);
        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog();
            }
        });

        TextView searchViewText = getView().findViewById(R.id.searchViewText);
        searchViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), SearchChurchesTool.class));
            }
        });
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

    private void alertDialog(){
        alertBuilder = new AlertDialog.Builder(ctx);
        alertBuilder.setTitle("Church Location");
        alertBuilder.setMessage("Find the nearest church?");
        alertBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = "";
                getShortestDistance(name);
            }
        });

        alertBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }


    private String getShortestDistance(String name){
        for(ChurchDistance churchDistanceIterator : ChurchDistanceList){
            if(churchDistanceIterator.getDistance() == findShortestDistance(distanceList)){
                name = churchDistanceIterator.getName();

                LatLng latLng = new LatLng(churchDistanceIterator.getChurchLat(), churchDistanceIterator.getChurchLng());
                mMap.addMarker(new MarkerOptions().position(latLng).title(name));

//                camera update for shortest church distance
                double churchDistance = churchDistanceIterator.getDistance();
                if(churchDistance < 7000){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                }

//                Log.println(Log.INFO, "Showw ", String.valueOf(churchDistanceIterator.getDistance()));
            }
        }
        return name;
    }

    private double findShortestDistance(ArrayList arrayList) {
        double distance = 0;

        for(int i = 0; i < arrayList.size(); i++){
            Collections.sort(arrayList);
        }
        int smallest = findMinIndex(arrayList);
//        Log.println(Log.INFO, "Short ", String.valueOf(ChurchDistanceList.get(smallest)));
        if(arrayList.get(0).equals(arrayList.get(smallest))){
            distance = (double) arrayList.get(0);
        }

        return distance;
    }

    public static <T extends Comparable<T>> int findMinIndex(final List<T> xs) {
        int minIndex;
        if (xs.isEmpty()) {
            minIndex = -1;
        } else {
            final ListIterator<T> itr = xs.listIterator();
            T min = itr.next(); // first element as the current minimum
            minIndex = itr.previousIndex();
            while (itr.hasNext()) {
                final T curr = itr.next();
                if (curr.compareTo(min) < 0) {
                    min = curr;
                    minIndex = itr.previousIndex();
                }
            }
        }
        return minIndex;
    }


//    TODO: Update module
        @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }

        }
    }

    public void getJsonObject(){
        String json;

        try {
            InputStream jObject = ctx.getAssets().open("some.json");
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


    private ChurchLocation getChurchLocations(){
        String json;

        try {
            InputStream jObject = ctx.getAssets().open("some.json");
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

                    churchLocation.setChurchName(jsonObject.getString("name"));

                    double latitude = churchLocation.getChurchLat();
                    double longitude = churchLocation.getChurchLng();
                    String churchName = churchLocation.getChurchName();

//                    churchLocation.setChurchLocation(new Location(churchLocation.getChurchLat(), churchLocation.getChurchLng()));

                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLatitude(churchLocation.getChurchLat());
                    location.setLongitude(churchLocation.getChurchLng());
//                    mMap.addMarker(new MarkerOp().position(new LatLng(churchLocation.getChurchLat(), churchLocation.getChurchLng())).title(churchLocation.getChurchName()));


                    churchLocation.setChurchLocation(location);

                    churchLocationList.add(new ChurchLocation(churchName, latitude, longitude, location));
//                    Location location = new Location("Point A");
//                    getDistances(getMyLocation(location), churchLocation);
                    Log.println(Log.INFO, "ChurchLocation ", String.valueOf(churchLocation.getChurchLocation()));
                }
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

}
