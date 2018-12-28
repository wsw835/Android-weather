package com.example.liu.weathercast.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liu.weathercast.R;
import com.example.liu.weathercast.adapter.FutureWeatherAdapter;
import com.example.liu.weathercast.adapter.ListAdapter;
import com.example.liu.weathercast.entities.Advices;
import com.example.liu.weathercast.entities.DataPackage;
import com.example.liu.weathercast.entities.ListItem;
import com.example.liu.weathercast.entities.NowInfo;
import com.example.liu.weathercast.entities.Weather;
import com.example.liu.weathercast.utils.HttpUtil;
import com.example.liu.weathercast.utils.IconsProcessing;
import com.example.liu.weathercast.utils.ProcessingData;
import com.example.liu.weathercast.utils.SaveObject;
import com.example.liu.weathercast.view.WeatherTrendView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class WeatherInfoActivity extends AppCompatActivity {


    //定义界面的组件
    private TextView futureWeatherTitle, cityView, dateView, refreshTimeView, temperView, windDirView, windPower, humiddityView;
    //获取两个界面的列表    //未来天气列表                   //温馨提示列表
    private ListView futureWeatherContent, weatherHints;
    private ScrollView scrollView;
    private ListAdapter myAdapter;//自定义的适配器
    private List<ListItem> dataList;//列表中的数据
    public SwipeRefreshLayout swipeRefreshLayout;//下拉刷新组件
    public DrawerLayout drawerLayout;//左滑选择城市布局
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {//将工具栏设置为透明
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather_info);
        SaveObject saveObject = new SaveObject();
        DataPackage weatherData = (DataPackage) saveObject.getObejectData(this, "weather");
        final String city;
        findElement();
        setDraw();
        if (weatherData == null) {//如果天气信息没有缓存的话，则直接获取天气信息
            city = getIntent().getStringExtra("city");

            scrollView.setVisibility(View.INVISIBLE);//当没有获取数据是，隐藏
            getWeather(city);

        } else {
            city = weatherData.getAdvices().getCity();
            showWeatherInfo(weatherData);
        }

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getWeather(city);
            }
        });
    }

    //获取界面组件
    public void findElement() {
        futureWeatherTitle = findViewById(R.id.future_weather_title);

        cityView = findViewById(R.id.city);
        swipeRefreshLayout = findViewById(R.id.refreshWeather);
        dateView = findViewById(R.id.date);
        refreshTimeView = findViewById(R.id.refreshTime);
        temperView = findViewById(R.id.temperView);
        windDirView = findViewById(R.id.windDirView);
        windPower = findViewById(R.id.windPower);
        humiddityView = findViewById(R.id.humiddityView);
        futureWeatherContent = findViewById(R.id.future_weather_content);
        weatherHints = findViewById(R.id.weatherHints);
        scrollView = findViewById(R.id.weather_layout);
        drawerLayout = findViewById(R.id.drawlayout_menu);
        frameLayout = findViewById(R.id.body);
        linearLayout = findViewById(R.id.weathertrend);

    }

    public void getWeather(final String city) {//根据城市信息获取天气信息

        //先将city改成urlencode
        String ecodeCity = null;
        try {
            ecodeCity = URLEncoder.encode(city, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //对于
        String url = " http://v.juhe.cn/weather/index?cityname=" + ecodeCity + "&dtype=&format=&key=9a052a297f2b64dff2c83382d9738264";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                final String cityData=data;

                final DataPackage dataPackage = ProcessingData.analiseString(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {//控制界面组件必须要在主线程中执行
                        if (dataPackage != null) {
                            SaveObject saveObject = new SaveObject();
                            saveObject.saveObject(WeatherInfoActivity.this, dataPackage, "weather");
                            showWeatherInfo(dataPackage);
                        } else {
                            Toast.makeText(WeatherInfoActivity.this, "获取数据失败", Toast.LENGTH_LONG).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        });
    }

    //在界面上展示天气信息
    private void showWeatherInfo(DataPackage dataPackage) {

       //展示现在天气的信息
        NowInfo nowInfo = dataPackage.getNowInfo();
        Advices advices = dataPackage.getAdvices();
        List<Weather> CurrentList = dataPackage.getLst();
        cityView.setText(advices.getCity());
        refreshTimeView.setText("今天：" + nowInfo.getTime());
        windPower.setText("风力:" + nowInfo.getWind_strength());
        windDirView.setText("风向:" + nowInfo.getWind_direction());
        temperView.setText("温度:" + nowInfo.getTemp() + "C");
        humiddityView.setText("湿度:" + nowInfo.getHuimidity());

        dateView.setText(CurrentList.get(0).getDate() + CurrentList.get(0).getWeek());
        setData(advices);
        myAdapter = new ListAdapter(this, dataList);
        weatherHints.setAdapter(myAdapter);

        //展示未来天气信息
        List<Weather> FutureList = dataPackage.getLst();

        futureWeatherTitle.setText(dataPackage.getAdvices().getCity() + "未来七天的天气");
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        for (Weather weather : FutureList) {
            Map<String, String> map = new HashMap<String, String>();

            map.put("星期", weather.getWeek());
            map.put("风", weather.getWind());
            map.put("日期", weather.getDate());
            map.put("天气", weather.getWeather());
            map.put("温度", weather.getTemperature());
            dataList.add(map);

        }
        FutureWeatherAdapter futureWeatherAdapter = new FutureWeatherAdapter(this, dataList);
        futureWeatherContent.setAdapter(futureWeatherAdapter);


        DisplayMetrics display = new DisplayMetrics();
        //将当前窗口的一些信息放在DisplayMetrics类中，
        this.getWindowManager().getDefaultDisplay().getMetrics(display);

        //获取缩放比例 480 * 1280 尺寸为 4.7英寸 为2.0
        float scaledDensity = display.scaledDensity;
        //删除之前添加的趋势图
       int m= linearLayout.getChildCount();

       if(m!=0){//如果组件中没有控件时，则不删除
           linearLayout.removeView(linearLayout.getChildAt(m-1));
       }
        //动态的添加趋势图
        WeatherTrendView weatherTrendView = new WeatherTrendView(WeatherInfoActivity.this, dataPackage);
        linearLayout.addView(weatherTrendView);

        //获取自定义组件的参数
        ViewGroup.LayoutParams layoutParams = weatherTrendView.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height=(int)(400*scaledDensity);
        }

    }



    public void setData(Advices advices) {//用于填充列表数据
        dataList = new ArrayList<ListItem>();
        ListItem lst1 = new ListItem();
        lst1.setTitle("穿衣情况");
        lst1.setContent(advices.getDress());
        ListItem lst2 = new ListItem();
        lst2.setTitle("穿衣建议");
        lst2.setContent(advices.getDressAdvice());
        ListItem lst3 = new ListItem();
        lst3.setTitle("舒适指数");
        lst3.setContent(advices.getComfort());
        ListItem lst4 = new ListItem();
        lst4.setTitle("洗车指数");
        lst4.setContent(advices.getWashIndex());
        ListItem lst5 = new ListItem();
        lst5.setTitle("锻炼指数");
        lst5.setContent(advices.getExerciseIndex());
        ListItem lst6 = new ListItem();
        lst6.setTitle("干燥指数");
        lst6.setContent(advices.getDryingIndex());
        ListItem lst7 = new ListItem();
        lst7.setTitle("照射指数");
        lst7.setContent(advices.getUvIndex());
        dataList.add(lst1);
        dataList.add(lst2);
        dataList.add(lst3);
        dataList.add(lst4);
        dataList.add(lst5);
        dataList.add(lst6);
        dataList.add(lst7);
    }

    //设置抽屉事件
    private void setDraw() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //设置拉出布局的宽度
                frameLayout.setX(slideOffset);//*drawerView.getWidth()

                /*Log.e(TAG, "onDrawerSlide: " + "滑动时执行");
                Log.e(TAG, "onDrawerSlide偏移量: " + slideOffset);
                Log.e(TAG, "onDrawerSlide偏移的宽度: " + drawerView.getWidth());*/

            }

            //当抽屉打开时执行该方法
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            //当抽屉关闭时执行该方法
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            //当抽屉状态改变时执行该方法
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

}
