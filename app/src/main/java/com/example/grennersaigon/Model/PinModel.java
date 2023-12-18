package com.example.grennersaigon.Model;

import com.google.firebase.firestore.GeoPoint;

public class PinModel {
    private String siteName;
    private String siteDescription;
    private String siteAddress;
    private GeoPoint position;

    public PinModel() {
        // Default constructor required for Firestore
    }

    public PinModel(String siteName, String siteDescription, String siteAddress, GeoPoint position) {
        this.siteName = siteName;
        this.siteDescription = siteDescription;
        this.siteAddress = siteAddress;
        this.position = position;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public GeoPoint getPosition() {
        return position;
    }
}
