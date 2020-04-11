package com.example.churchlocation.Model;

public class ChurchDistance {
    double distance;
    String name;
    double churchLat, churchLng;

    public ChurchDistance(double distance, String name, double churchLat, double churchLng) {
        this.distance = distance;
        this.name = name;
        this.churchLat = churchLat;
        this.churchLng = churchLng;
    }

    public ChurchDistance(String name , double distance) {
        this.distance = distance;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
