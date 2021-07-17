package com.example.pokemongame;

import android.location.Location;

public class Pokemon {
    public  int image;
    public String name;
    public  String des;
    public double power;
    public boolean isCatch;
    public Location location;

    public Pokemon(int image, String name, String des, double power,
                   double lat,double log) {
        this.image = image;
        this.name = name;
        this.des = des;
        this.power = power;
        this.isCatch = false;
        location=new Location(name);
        this.location.setLatitude(lat);
        this.location.setLongitude(log);
    }
}
