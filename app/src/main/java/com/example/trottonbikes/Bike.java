package com.example.trottonbikes;

public class Bike {
    String ownersName, ownerAddress;
    float rating;
    String imgUrl;

    public Bike(String ownersName, String ownerAddress, float rating, String imgUrl) {
        this.ownersName = ownersName;
        this.ownerAddress = ownerAddress;
        this.rating = rating;
        this.imgUrl = imgUrl;
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
}
