package com.example.trottonbikes;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Ride {
    String rideID, userId;
    float amount;
    long timeStart, timeEnd;

    public Ride(String rideID, String userId, float amount) {
        this.rideID = rideID;
        this.userId = userId;
        this.amount = amount;
    }

    public String getRideID() {
        return rideID;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }
}
