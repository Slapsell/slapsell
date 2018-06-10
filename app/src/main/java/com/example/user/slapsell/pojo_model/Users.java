package com.example.user.slapsell.pojo_model;

public class Users {
    String name;
    String email;
    Address address;
    Number mobile;
    int uploads;
    String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Number getMobile() {
        return mobile;
    }

    public void setMobile(Number mobile) {
        this.mobile = mobile;
    }

    public int getUploads() {
        return uploads;
    }

    public void setUploads(int uploads) {
        this.uploads = uploads;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
