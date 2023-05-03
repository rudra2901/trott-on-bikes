package com.example.trottonbikes;

import java.util.ArrayList;

public class Users {
    String userid , name , address;
    ArrayList<String> bikesOwned;

    public ArrayList<String> getBikesOwned() {
        return bikesOwned;
    }

    public void setBikesOwned(ArrayList<String> bikesOwned) {
        this.bikesOwned = bikesOwned;
    }

    public Users(String userid, String name, String address , ArrayList<String> bikesOwned) {
        this.userid = userid;
        this.name = name;
        this.address = address;
        this.bikesOwned = bikesOwned;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
