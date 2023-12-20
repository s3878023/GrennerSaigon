package com.example.grennersaigon.Model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;

public class PinModel {
    private String siteName;
    private String siteDescription;
    private String siteAddress;
    private String siteOwner;
    private GeoPoint position;
    private ArrayList<String> siteMembers;
    private Date dateTime;
    private String siteReport;
    public PinModel() {
    }

    public PinModel(String siteName, String siteDescription, String siteAddress, String siteOwner, GeoPoint position, Date dateTime, ArrayList<String> siteMembers, String siteReport) {
        this.siteName = siteName;
        this.siteDescription = siteDescription;
        this.siteAddress = siteAddress;
        this.siteOwner = siteOwner;
        this.position = position;
        this.dateTime = dateTime;
        this.siteMembers = siteMembers;
        this.siteReport = siteReport;
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

    public String getSiteOwner() {
        return siteOwner;
    }

    public GeoPoint getPosition() {
        return position;
    }

    public ArrayList<String> getSiteMembers() {
        return siteMembers;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
