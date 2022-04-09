package com.example.trottonbikes;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Booking {
    String bookingID, user;
    int bookingTime;
    long time;

    public Booking(String bookingID, String user, int bookingTime, long time) {
        this.bookingID = bookingID;
        this.user = user;
        this.bookingTime = bookingTime;
        this.time = time;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(int bookingTime) {
        this.bookingTime = bookingTime;
    }
}
