package com.gori.acmeexplorer.api;

public class Geolocation {
    private String name;
    private float lat;
    private float lon;
    private String country;

    public Geolocation() {
    }

    public Geolocation(String name, float lat, float lon, String country) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
