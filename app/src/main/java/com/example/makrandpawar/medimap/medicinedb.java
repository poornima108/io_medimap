package com.example.makrandpawar.medimap;

public class medicinedb {

    String name;
    String address;
    String image;
    int value;

    public medicinedb(){}
    public medicinedb(String name, String address, String image, int value) {
        this.name = name;
        this.address = address;
        this.image = image;
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
