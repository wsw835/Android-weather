package com.example.liu.weathercast.entities;

import java.io.Serializable;

public class NowInfo implements Serializable
{
    private String wind_strength;

    public String getWind_strength() {
        return wind_strength;
    }

    public void setWind_strength(String wind_strength) {
        this.wind_strength = wind_strength;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(String wind_direction) {
        this.wind_direction = wind_direction;
    }

    public String getHuimidity() {
        return huimidity;
    }

    public void setHuimidity(String huimidity) {
        this.huimidity = huimidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String temp;
    private String wind_direction ;
    private  String huimidity ;
    private  String time;
}
