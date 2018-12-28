package com.example.liu.weathercast.entities;

import java.io.Serializable;

public class Advices implements Serializable {
    private String dress;//穿衣情况
    private String dressAdvice;//穿衣建议
    private String comfort;//舒适指数
    private String washIndex;//洗车指数
    private String exerciseIndex;//锻炼建议
    private String dryingIndex;//干燥指数
    private String uvIndex;//紫外线指数
    private String city;//城市


    public String getDress() {
        return dress;
    }

    public void setDress(String dress) {
        this.dress = dress;
    }

    public String getDressAdvice() {
        return dressAdvice;
    }

    public void setDressAdvice(String dressAdvice) {
        this.dressAdvice = dressAdvice;
    }

    public String getComfort() {
        return comfort;
    }

    public void setComfort(String comfort) {
        this.comfort = comfort;
    }

    public String getWashIndex() {
        return washIndex;
    }

    public void setWashIndex(String washIndex) {
        this.washIndex = washIndex;
    }

    public String getExerciseIndex() {
        return exerciseIndex;
    }

    public void setExerciseIndex(String exerciseIndex) {
        this.exerciseIndex = exerciseIndex;
    }

    public String getDryingIndex() {
        return dryingIndex;
    }

    public void setDryingIndex(String dryingIndex) {
        this.dryingIndex = dryingIndex;
    }

    public String getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(String uvIndex) {
        this.uvIndex = uvIndex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}