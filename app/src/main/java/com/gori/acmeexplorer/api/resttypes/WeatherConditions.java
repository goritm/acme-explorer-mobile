package com.gori.acmeexplorer.api.resttypes;

public class WeatherConditions {
    private float temp;
    private float feels_like;
    private float temp_max;
    private float temp_min;
    private int pressure;
    private int humidity;

    public WeatherConditions() {
    }

    public WeatherConditions(float temp, float feels_like, float temp_max, float temp_min, int pressure, int humidity) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(float feels_like) {
        this.feels_like = feels_like;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
