package com.example.user.slapsell.pojo_model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Products implements Parcelable {
    String name;
    String type;
    String description;
    int price;
    String owner;
    String date;
    String p_id;
    String location;
    public Products(){

    }
    public Products(String name, String type, String description,int price, String owner, String date,String Location) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
        this.owner = owner;
        this.date = date;
        this.location=Location;
    }
    public Products(Parcel parcel)
    {
        name=parcel.readString();
        type=parcel.readString();
        description=parcel.readString();
        price=parcel.readInt();
        owner=parcel.readString();
        date=parcel.readString();
        p_id=parcel.readString();
        location=parcel.readString();

    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(description);
        parcel.writeInt(price);
        parcel.writeString(owner);
        parcel.writeString(date);
        parcel.writeString(p_id);
        parcel.writeString(location);

    }
}
