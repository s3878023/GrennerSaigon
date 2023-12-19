//package com.example.grennersaigon.Model;
//
//import com.google.firebase.firestore.GeoPoint;
//
//public class PinModel {
//    private String siteName;
//    private String siteDescription;
//    private String siteAddress;
//    private GeoPoint position;
//
//
//    public PinModel() {
//        // Default constructor required for Firestore
//    }
//
//    public PinModel(String siteName, String siteDescription, String siteAddress, GeoPoint position) {
//        this.siteName = siteName;
//        this.siteDescription = siteDescription;
//        this.siteAddress = siteAddress;
//        this.position = position;
//    }
//
//    public String getSiteName() {
//        return siteName;
//    }
//
//    public String getSiteDescription() {
//        return siteDescription;
//    }
//
//    public String getSiteAddress() {
//        return siteAddress;
//    }
//
//    public GeoPoint getPosition() {
//        return position;
//    }
//}
package com.example.grennersaigon.Model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Collection;

public class PinModel {
    private String siteName;
    private String siteDescription;
    private String siteAddress;
    private String siteOwner; // New field
    private GeoPoint position;
    private ArrayList<String> siteMember;

    public PinModel() {
        // Default constructor required for Firestore
    }

    public PinModel(String siteName, String siteDescription, String siteAddress, String siteOwner, GeoPoint position) {
        this.siteName = siteName;
        this.siteDescription = siteDescription;
        this.siteAddress = siteAddress;
        this.siteOwner = siteOwner;
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

    public String getSiteOwner() {
        return siteOwner;
    }

    public GeoPoint getPosition() {
        return position;
    }

    public ArrayList<String> getSiteMembers() {
        return null;
    }
}
