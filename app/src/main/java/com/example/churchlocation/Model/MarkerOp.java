package com.example.churchlocation.Model;

import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerOp {
    private MarkerOptions markerOptions;

    public MarkerOp() {
    }

    public MarkerOp(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }
}


