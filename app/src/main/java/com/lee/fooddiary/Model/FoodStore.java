package com.lee.fooddiary.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class FoodStore implements Parcelable {

    String name;
    String date;
    float rate;
    int moneyMin;
    int moneyMax;
    String address;
    double latitude;
    double longtiude;
    List<String> imageUrls = new ArrayList<>();

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtiude() {
        return longtiude;
    }

    public void setLongtiude(double longtiude) {
        this.longtiude = longtiude;
    }


    public int getMoneyMin() {
        return moneyMin;
    }

    public void setMoneyMin(int moneyMin) {
        this.moneyMin = moneyMin;
    }

    public int getMoneyMax() {
        return moneyMax;
    }

    public void setMoneyMax(int moneyMax) {
        this.moneyMax = moneyMax;
    }


    public FoodStore() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.date);
        dest.writeFloat(this.rate);
        dest.writeInt(this.moneyMin);
        dest.writeInt(this.moneyMax);
        dest.writeString(this.address);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longtiude);
        dest.writeStringList(this.imageUrls);
    }

    protected FoodStore(Parcel in) {
        this.name = in.readString();
        this.date = in.readString();
        this.rate = in.readFloat();
        this.moneyMin = in.readInt();
        this.moneyMax = in.readInt();
        this.address = in.readString();
        this.latitude = in.readDouble();
        this.longtiude = in.readDouble();
        this.imageUrls = in.createStringArrayList();
    }

    public static final Parcelable.Creator<FoodStore> CREATOR = new Parcelable.Creator<FoodStore>() {
        @Override
        public FoodStore createFromParcel(Parcel source) {
            return new FoodStore(source);
        }

        @Override
        public FoodStore[] newArray(int size) {
            return new FoodStore[size];
        }
    };
}
