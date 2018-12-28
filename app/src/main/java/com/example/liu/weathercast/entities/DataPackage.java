package com.example.liu.weathercast.entities;

import java.io.Serializable;
import java.util.List;

public class DataPackage implements Serializable{
    private Advices advices;
    private List<Weather> lst;
    private NowInfo nowInfo;

    public NowInfo getNowInfo() {
        return nowInfo;
    }

    public void setNowInfo(NowInfo nowInfo) {
        this.nowInfo = nowInfo;
    }

    public Advices getAdvices() {
        return advices;
    }

    public void setAdvices(Advices advices) {
        this.advices = advices;
    }

    public List<Weather> getLst() {
        return lst;
    }

    public void setLst(List<Weather> lst) {
        this.lst = lst;
    }


    @Override
    public String toString() {
        return ("风力："+lst.get(0).getWind()+"气温："+lst.get(0).getTemperature()+"日期:"+lst.get(0).getDate()+"星期："+lst.get(0).getWeek()+"天气"+lst.get(0).getWeather()
                +"城市："+advices.getCity()+"舒适度："+advices.getComfort()+"穿衣指数："+advices.getDress()+"穿衣建议："+advices.getDressAdvice()+"干燥指数："+advices.getDryingIndex()+"运动指数："+advices.getExerciseIndex()+"紫外线："+advices.getUvIndex()+"洗车指数："
                +advices.getWashIndex());
    }
}
