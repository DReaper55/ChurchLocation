package com.example.churchlocation.Model;

import android.location.Location;

public class ChurchLocation {
    String churchName, pastorName;
    double churchLat, churchLng;
    Location myLocation, churchLocation;

    public ChurchLocation() {
    }

    public ChurchLocation(String churchName, double churchLat, double churchLng) {
        this.churchName = churchName;
        this.churchLat = churchLat;
        this.churchLng = churchLng;
    }

    public ChurchLocation(String churchName, double churchLat, double churchLng, Location churchLocation) {
        this.churchName = churchName;
        this.churchLat = churchLat;
        this.churchLng = churchLng;
        this.churchLocation = churchLocation;
    }


    public ChurchLocation(Location churchLocation) {
        this.churchLocation = churchLocation;
    }

    public Location getChurchLocation() {
        return churchLocation;
    }

    public void setChurchLocation(Location churchLocation) {
        this.churchLocation = churchLocation;
    }

    public ChurchLocation(String churchName) {
        this.churchName = churchName;
    }

    public String getChurchName() {
        return churchName;
    }

    public void setChurchName(String churchName) {
        this.churchName = churchName;
    }

    public String getPastorName() {
        return pastorName;
    }

    public void setPastorName(String pastorName) {
        this.pastorName = pastorName;
    }

    public double getChurchLat() {
        return churchLat;
    }

    public void setChurchLat(double churchLat) {
        this.churchLat = churchLat;
    }

    public double getChurchLng() {
        return churchLng;
    }

    public void setChurchLng(double churchLng) {
        this.churchLng = churchLng;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }
}
