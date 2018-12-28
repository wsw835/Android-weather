package com.example.liu.weathercast.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.liu.weathercast.R;
public class WeatherTrend  extends Fragment{
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.weather_trend,container,false);
       view.findViewById(R.id.trend_title);
       return super.onCreateView(inflater, container, savedInstanceState);
    }
}
