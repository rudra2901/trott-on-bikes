package com.example.trottonbikes;

import android.os.Parcel;
import android.os.Parcelable;

public class Bike implements Parcelable {
    String id;
    String ownersName, ownerAddress;
    String desc;
    float rating;
    String imgUrl;
    String code;
    int booked;

    public Bike() {
        this.id = "bikeID";
        this.ownersName = "owner's Name";
        this.ownerAddress = "owner's Address";
        this.desc = "Lorem ipsum dolor si amet";
        this.rating = 4;
        this.imgUrl = "@mipmap/bikestockimage.png";
        this.code = "1234";
        this.booked = 0;
    }

    public Bike(String id,String ownersName, String ownerAddress, String desc, float rating, String imgUrl, String code, int booked) {
        this.id = id;
        this.ownersName = ownersName;
        this.ownerAddress = ownerAddress;
        this.desc = desc;
        this.rating = rating;
        this.imgUrl = imgUrl;
        this.code = code;
        this.booked = booked;
    }

    protected Bike(Parcel in) {
        id = in.readString();
        ownersName = in.readString();
        ownerAddress = in.readString();
        desc = in.readString();
        rating = in.readFloat();
        imgUrl = in.readString();
        code = in.readString();
        booked = in.readInt();
    }

    public static final Creator<Bike> CREATOR = new Creator<Bike>() {
        @Override
        public Bike createFromParcel(Parcel in) {
            return new Bike(in);
        }

        @Override
        public Bike[] newArray(int size) {
            return new Bike[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnersName() {
        return ownersName;
    }

    public void setOwnersName(String ownersName) {
        this.ownersName = ownersName;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = booked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ownersName);
        dest.writeString(ownerAddress);
        dest.writeString(desc);
        dest.writeFloat(rating);
        dest.writeString(imgUrl);
        dest.writeString(code);
        dest.writeInt(booked);
    }
}
