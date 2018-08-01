package com.example.user.slapsell.pojo_model;

public class Users {
    String name;
    String email;
    Address address;
    String mob;
    String fcm_regid;

    public Users() {
        name="";
        email="";
        address=new Address();
        mob="";
        fcm_regid="";
    }

    public Users(String name, String email, Address address, String mob) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.mob = mob;
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

}
