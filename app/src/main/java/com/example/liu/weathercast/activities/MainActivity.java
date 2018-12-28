package com.example.liu.weathercast.activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.liu.weathercast.R;
import com.example.liu.weathercast.utils.SaveObject;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SaveObject saveObject=new SaveObject();
        //如果图标文件为空时，则存储图标
        if(saveObject.getObejectData(this,"icons")==null){
            saveObject.saveIcons(this);
        }
        //之前是否已经获取了数据，如果有记录则直接跳转到getWeatherInfo进行处理
        if(saveObject.getObejectData(this,"weather")!=null){
            Intent intent = new Intent(this,WeatherInfoActivity.class);
            startActivity(intent);
            finish();
        }
        //如果不为空则直接进行城市选择，然后跳转到getWeatherInfo界面进行处理
    }
}
