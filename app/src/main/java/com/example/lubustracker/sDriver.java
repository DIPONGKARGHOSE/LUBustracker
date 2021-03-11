package com.example.lubustracker;

public class sDriver{
    private  String Name;
    private  double Longitude;
    private  double Latitude;

    public sDriver(String name, double longitude, double latitude) {
        Name = name;
        Longitude = longitude;
        Latitude = latitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
