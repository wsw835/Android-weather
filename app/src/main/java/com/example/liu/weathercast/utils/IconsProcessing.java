package com.example.liu.weathercast.utils;

import android.content.Context;
import android.util.Log;

import com.example.liu.weathercast.R;

import java.util.Map;

//判断天气，并返回图标
public class IconsProcessing {
    private Context context;
    public IconsProcessing(Context context){
        this.context=context;
    }
    public int judgeWeather(String weather){
        SaveObject saveObject=new SaveObject();
        int i=0;
        Log.d("判断","运行"+weather.contains("风"));
       Map<String ,Integer> map= (Map<String,Integer>)saveObject.getObejectData(context,"icons");

        if(weather.contains("晴")){
            i=map.get("晴");
        }else if(weather.contains("雨")){
           i= map.get("雨");
        }else if(weather.contains("雾")){
           i= map.get("雾");
        }else if(weather.contains("风")){

           i= map.get("风");

        }else if(weather.contains("雷")){
          i= map.get("雷");
        }else if(weather.contains("云")){
            i=map.get("云");
        }else if(weather.contains("雪")){
           i= map.get("雪");
        }else {
            i= R.mipmap.unknown;
        }

        return i;
    }
}

