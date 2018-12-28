package com.example.liu.weathercast.utils;

import android.util.Log;

import com.example.liu.weathercast.entities.Advices;
import com.example.liu.weathercast.entities.DataPackage;
import com.example.liu.weathercast.entities.NowInfo;
import com.example.liu.weathercast.entities.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

//用于处理数据，工具类
public class ProcessingData {
    public static DataPackage analiseString(String data) {//解析获取的字符串

        List<Weather> lst = new ArrayList<Weather>();
        DataPackage dp = new DataPackage();
        Advices advices = new Advices();
        NowInfo nowInfo = new NowInfo();
        JSONObject result;
        try {
             result = new JSONObject(data);
            //获取数据的状态
            String state=result.getString("resultcode");
            if(state.equals("200")){
                JSONObject weatherResult = result.getJSONObject("result");
                JSONObject todayDtail = weatherResult.getJSONObject("sk");
                String weathers = weatherResult.getString("future");
                String todayAdvice = weatherResult.getString("today");
                //获取当天的一些细节数据
                nowInfo.setHuimidity(todayDtail.getString("humidity"));
                nowInfo.setTemp(todayDtail.getString("temp"));
                nowInfo.setTime(todayDtail.getString("time"));
                nowInfo.setWind_direction(todayDtail.getString("wind_direction"));
                nowInfo.setWind_strength(todayDtail.getString("wind_strength"));
                lst = setWeather(weathers);//获取未来天气的集合
                JSONObject todayJson = new JSONObject(todayAdvice);
                //获取当前天气建议
                advices.setCity(todayJson.getString("city"));
                if (todayJson.getString("comfort_index").equals("")) {
                    advices.setComfort("暂无");
                } else {
                    advices.setComfort(todayJson.getString("comfort_index"));
                }


                if (todayJson.getString("dressing_index").equals("")) {
                    advices.setDress("暂无");
                } else {
                    advices.setDress(todayJson.getString("dressing_index"));
                }


                if (todayJson.getString("dressing_advice").equals("")) {
                    advices.setDressAdvice("暂无");
                } else {
                    advices.setDressAdvice(todayJson.getString("dressing_advice"));
                }


                if (todayJson.getString("drying_index").equals("")) {
                    advices.setDryingIndex("暂无");
                } else {
                    advices.setDryingIndex(todayJson.getString("drying_index"));
                }

                if (todayJson.getString("uv_index").equals("")) {
                    advices.setUvIndex("暂无");
                }
                advices.setUvIndex(todayJson.getString("uv_index"));
                if (todayJson.getString("exercise_index").equals("")) {
                    advices.setExerciseIndex("暂无");
                } else {
                    advices.setExerciseIndex(todayJson.getString("exercise_index"));
                }

                if (todayJson.getString("wash_index").equals("")) {
                    advices.setWashIndex("暂无");
                } else {
                    advices.setWashIndex(todayJson.getString("wash_index"));
                }
            }else{
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dp.setAdvices(advices);
        dp.setLst(lst);
        dp.setNowInfo(nowInfo);
        return dp;
    }

    private static List<Weather> setWeather(String data) {
        List<Weather> weathers = new ArrayList<Weather>();
        try {
            JSONObject djsonObject = new JSONObject(data);

            for (int i = 0; i < 7; i++) {
                Weather weather = new Weather();
                JSONObject jsonObject = djsonObject.getJSONObject(transeDate(i));
                weather.setWeather(jsonObject.getString("weather"));
                weather.setDate(jsonObject.getString("date"));
                weather.setWeek(jsonObject.getString("week"));
                weather.setTemperature(jsonObject.getString("temperature"));
                weather.setWind(jsonObject.getString("wind"));
                weathers.add(weather);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weathers;
    }


    public static String transeDate(int i) {//根据日期来获取数据
        Date date = new Date();

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, i);//当前天数往后加一天
        date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String sdate = "day_" + sdf.format(date);
        return sdate;
    }
}
