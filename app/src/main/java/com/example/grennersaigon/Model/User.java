package com.example.grennersaigon.Model;
// Information.java

import java.util.ArrayList;

public class User {

    private String name;
    private String age;
    private String address;
    private String userId;
    private ArrayList<String> ownSite;
    private ArrayList<String> joinSite;


    // Default constructor required for Firestore
    public User() {}

    public User(String name, String age, String address, String userId, ArrayList<String> ownSite, ArrayList<String> joinSite) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.userId = userId;
        this.ownSite = ownSite;
        this.joinSite = joinSite;
    }

    // Getter methods (required for Firestore)
    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<String> getOwnSite() {
        return ownSite;
    }
    public ArrayList<String> getJoinSiteSite() {
        return joinSite;
    }
    public void setOwnSite(ArrayList<String> ownSite) {
        this.ownSite = ownSite;
    }

    public void setJoinSite(ArrayList<String> joinSite) {
        this.joinSite = joinSite;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }
}
