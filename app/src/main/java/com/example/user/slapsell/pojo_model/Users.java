package com.example.user.slapsell.pojo_model;

public class Users {
    String name;
    String email;
    Address address;
    String mob;
    int uploads;
    String image;

    public Users() {
        name=null;
        email=null;
        address=new Address();
        mob=null;
        uploads=0;
        image=null;
    }

    public Users(String name, String email, Address address, String mob, int uploads, String image) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.mob = mob;
        this.uploads = uploads;
        this.image = image;
    }

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

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
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
