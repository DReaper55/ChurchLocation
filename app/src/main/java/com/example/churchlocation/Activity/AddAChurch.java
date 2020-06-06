package com.example.churchlocation.Activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.churchlocation.Model.MyLocation;
import com.example.churchlocation.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AddAChurch extends AppCompatActivity {
    AlertDialog.Builder alertBuilder;
    AlertDialog dialog;

    EditText enterRegion;
    EditText pastorName;
    EditText address;
    EditText pastorNum;
    Button shareLocation;

    LocationManager locationManager;
    LocationListener locationListener;

    ArrayList<MyLocation> myLocationArrayList = new ArrayList<>();
    ArrayList<MyLocation> myLocations = new ArrayList<>();
    MyLocation myLocation = new MyLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achurch);

        fetchMyLocation();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAChurch.this.popUpDialog();
            }
        });
    }


    private void popUpDialog() {
        alertBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.send_location, null);

        enterRegion = view.findViewById(R.id.regionName);
        shareLocation = view.findViewById(R.id.shareRegionLocation);
        pastorName = view.findViewById(R.id.pastorName);
        address = view.findViewById(R.id.address);
        pastorNum = view.findViewById(R.id.phoneNumber);

        alertBuilder.setView(view);
        dialog = alertBuilder.create();

        dialog.show();

        shareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    if (!enterRegion.getText().toString().isEmpty()
                            && !pastorName.getText().toString().isEmpty()
                            && !pastorNum.getText().toString().isEmpty()) {
                        AddAChurch.this.shareLocation();
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    private void fetchMyLocation() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLocation.setmLatitude(location.getLatitude());
                myLocation.setmLongitude(location.getLongitude());

                myLocationArrayList.add(myLocation);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

    }

    private void shareLocation() {
        if (myLocationArrayList.size() > 1) {
            myLocations.add(myLocationArrayList.get(0));

            Log.println(Log.INFO, "New ", myLocations.toString());
            myLocationArrayList.clear();
        }

        for (MyLocation myLocationIterator : myLocations) {
            for (int i = 0; i < 2; i++) {
                StringBuilder dataString = new StringBuilder();

                String region = enterRegion.getText().toString();
                String pName = pastorName.getText().toString();
                String phoneNum = pastorNum.getText().toString();
                String churchAddress = address.getText().toString();

                dataString.append(" Region: " + region + "\n");
                dataString.append(" Leader name: " + pName + "\n");
                dataString.append(" Leader number: " + phoneNum + "\n");

                if (address != null) {
                    dataString.append(" Church address: " + churchAddress + "\n");
                }

                dataString.append(" Latitude: " + myLocationIterator.getmLatitude() + "\n");
                dataString.append(" Longitude: " + myLocationIterator.getmLongitude() + "\n");

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("message/rfc822");
                share.putExtra(Intent.EXTRA_SUBJECT, "Church Location");
                share.putExtra(Intent.EXTRA_EMAIL, new String[]{"sharechurch61@gmail.com"});
                share.putExtra(Intent.EXTRA_TEXT, dataString.toString());

                Log.println(Log.INFO, "Location ", String.valueOf(myLocationIterator));

                try {
                    startActivity(Intent.createChooser(share, "Send mail..."));
                    Toast.makeText(this, "Thanks for the feedback ", Toast.LENGTH_LONG).show();
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Install an email client", Toast.LENGTH_LONG).show();
                }
            }

        }

        myLocationArrayList.clear();
        myLocations.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }

        }
    }

}
