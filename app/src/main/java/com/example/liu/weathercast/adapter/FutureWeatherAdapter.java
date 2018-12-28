package com.example.liu.weathercast.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liu.weathercast.R;
import com.example.liu.weathercast.activities.WeatherInfoActivity;
import com.example.liu.weathercast.utils.IconsProcessing;

import java.util.List;
import java.util.Map;

public class FutureWeatherAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<Map<String, String>> lstData;
    private Context context;

    public FutureWeatherAdapter(Context context, List<Map<String, String>> lst) {
        layoutInflater = LayoutInflater.from(context);
        lstData = lst;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public Object getItem(int position) {
        return lstData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.future_weather_item, parent, false);
            viewHolder.weekText = convertView.findViewById(R.id.week);
            viewHolder.dateText = convertView.findViewById(R.id.date);
            viewHolder.tempText = convertView.findViewById(R.id.temper);
            viewHolder.weaterText = convertView.findViewById(R.id.weather);
            viewHolder.windText = convertView.findViewById(R.id.wind);
            viewHolder.icon1 = convertView.findViewById(R.id.icon1);
            viewHolder.icon2 = convertView.findViewById(R.id.icon2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.dateText.setText("日期:" + lstData.get(position).get("日期"));
        viewHolder.weekText.setText("星期:" + lstData.get(position).get("星期"));
        viewHolder.windText.setText("风  :" + lstData.get(position).get("风"));
        viewHolder.weaterText.setText("天气:" + lstData.get(position).get("天气"));
        viewHolder.tempText.setText("温度:" + lstData.get(position).get("温度"));
        //动态的添加icons
        IconsProcessing iconsProcessing = new IconsProcessing(context);
        int i = iconsProcessing.judgeWeather(lstData.get(position).get("天气"));
        viewHolder.icon1.setImageResource(i);
        viewHolder.icon2.setImageResource(i);
        return convertView;
    }

    private class ViewHolder {
        TextView windText;
        TextView tempText;
        TextView dateText;
        TextView weaterText;
        TextView weekText;
        ImageView icon1;
        ImageView icon2;
    }
}
