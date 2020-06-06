package com.example.churchlocation.Activity;

import androidx.annotation.NonNull;
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

import com.example.churchlocation.Database.DatabaseHandler;
import com.example.churchlocation.Model.ChurchDistance;
import com.example.churchlocation.Model.ChurchLocation;
import com.example.churchlocation.Model.SearchChurchModel;
import com.example.churchlocation.Utils.ConnectToChurchDB;
import com.example.churchlocation.Utils.LatLngInterpolator;
import com.example.churchlocation.Model.MarkerAnimation;
import com.example.churchlocation.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

//    update churchLocationList from db
    private ArrayList<Double> distanceList = new ArrayList<>();
    private ArrayList<ChurchDistance> ChurchDistanceList = new ArrayList<>();

    private CameraPosition mCameraPosition;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private DatabaseHandler db;
    private ConnectToChurchDB connect = new ConnectToChurchDB();
    private List<SearchChurchModel> listOfChurches;

    private View view;
    private Context ctx;

    private static int counter;

    private SupportMapFragment mapFragment;

    ChurchLocation churchLocation = new ChurchLocation();

    private AlertDialog.Builder alertBuilder;

    
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

//        Where first bit of code begins
        if(savedInstanceState != null){
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(root.getContext());

//      Where it ends

        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                view = getView();

                FloatingActionButton nearestChurch = getActivity().findViewById(R.id.findNearestChurch);
                nearestChurch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getShortestDistance();
                    }
                });


                getLocationPermission();

                updateLocationUI();

                getDeviceLocation();

                db = connect.getChurches(view.getContext());
                listOfChurches = db.getAllContacts();
            }
        });

       return root;
    }

    private void getDeviceLocation(){
//        final Marker mCurrLocationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
//                .icon(bitmapDescriptor(view.getContext(), R.drawable.map_current_local)));

        try{
            if(mLocationPermissionGranted){
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()){
                            mLastKnownLocation = task.getResult();
                            if(mLastKnownLocation != null){
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 12));

//                                LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
//                                MarkerAnimation.animateMarkerToGB(mCurrLocationMarker, latLng, new LatLngInterpolator.Spherical());

                                for(SearchChurchModel churchIterator : listOfChurches){
                                    Location startPoint = new Location(LocationManager.GPS_PROVIDER);
                                    startPoint.setLatitude(mLastKnownLocation.getLatitude());
                                    startPoint.setLongitude(mLastKnownLocation.getLongitude());

                                    Location endPoint = new Location(LocationManager.GPS_PROVIDER);
                                    endPoint.setLatitude(churchIterator.getChurchLat());
                                    endPoint.setLongitude(churchIterator.getChurchLng());

                                    double result = startPoint.distanceTo(endPoint);
                                    Log.println(Log.INFO, "Distances ", String.valueOf(result));

                                    ChurchDistanceList.add(new ChurchDistance(result, churchIterator.getChurchName(), churchIterator.getChurchLat(), churchIterator.getChurchLng()));

                                    distanceList.add(result);


                                    FloatingActionButton myLocation = view.findViewById(R.id.findMyLocation);
                                    myLocation.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 7));
                                        }
                                    });

                                }
                            }
                        } else {
                            Log.d("MapsActivity", "Current location is null. Using defaults.");
                            Log.e("MapsActivity", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 12));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        }catch(SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateLocationUI() {
        if(mMap == null){
            return;
        }
        try{
            if(mLocationPermissionGranted){
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch(SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }
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
                getShortestDistance();
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


    private void getShortestDistance(){
        String name = "";
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

    private static <T extends Comparable<T>> int findMinIndex(final List<T> xs) {
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

    private void getLocationPermission(){
        if(ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(mMap != null){
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

}
