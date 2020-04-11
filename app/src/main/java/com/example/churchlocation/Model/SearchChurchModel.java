package com.example.churchlocation.Model;

import android.location.Location;

public class SearchChurchModel {
    private String churchName, pastorName, address, state, country, about, number, disciples, region;
    private double churchLat, churchLng;
    private Location churchLocation;

    public SearchChurchModel() {
    }

    public SearchChurchModel(String churchName, String region, String pastorName, String address, String state, String country, String about, String number, String disciples, double churchLat, double churchLng, Location churchLocation) {
        this.churchName = churchName;
        this.pastorName = pastorName;
        this.address = address;
        this.state = state;
        this.country = country;
        this.about = about;
        this.number = number;
        this.disciples = disciples;
        this.churchLat = churchLat;
        this.churchLng = churchLng;
        this.churchLocation = churchLocation;
        this.region = region;
    }

    public SearchChurchModel(String churchName, String pastorName, String address, String state, String country, String about, String number, String disciples, String region, double churchLat, double churchLng) {
        this.churchName = churchName;
        this.pastorName = pastorName;
        this.address = address;
        this.state = state;
        this.country = country;
        this.about = about;
        this.number = number;
        this.disciples = disciples;
        this.region = region;
        this.churchLat = churchLat;
        this.churchLng = churchLng;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDisciples() {
        return disciples;
    }

    public void setDisciples(String disciples) {
        this.disciples = disciples;
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

    public Location getChurchLocation() {
        return churchLocation;
    }

    public void setChurchLocation(Location churchLocation) {
        this.churchLocation = churchLocation;
    }

    @Override
    public String toString(){
        return  getAbout() + " \n" + getChurchName() + " \n" + getAddress();
    }
}
