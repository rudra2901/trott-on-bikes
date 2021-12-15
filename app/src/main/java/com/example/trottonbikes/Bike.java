package com.example.trottonbikes;

public class Bike {
    String ownersName, ownerAddress;
    double rating;
    String imgUrl;

    public Bike() {
        this.ownersName = "owner's Name";
        this.ownerAddress = "owner's Address";
        this.rating = 4.0;
        this.imgUrl = "@mipmap/bikestockimage.png";
    }

    public Bike(String ownersName, String ownerAddress, double rating, String imgUrl) {
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
