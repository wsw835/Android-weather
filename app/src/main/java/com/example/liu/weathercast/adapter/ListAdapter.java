package com.example.liu.weathercast.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.liu.weathercast.R;
import com.example.liu.weathercast.entities.ListItem;

import java.util.List;


public class ListAdapter extends BaseAdapter {
    private LayoutInflater listInflater;
    private List<ListItem> dataList;

    //这里需要上下文来获取填充器来加载item的布局
    public ListAdapter(Context context, List<ListItem> listData) {
      listInflater=  LayoutInflater.from(context);
      dataList=listData;
    }

    //数据的长度
    @Override
    public int getCount() {
        return dataList.size();
    }
    //对应位置的数据
    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }
    //对应位置的id
    @Override
    public long getItemId(int position) {
        return position;
    }
    //获取一个用来展示数据集中指定位置的数据的视图。可以通过代码或者inflate一个XML文件来获得这个View对象。
    //这里关键是创建一个ViewHolder（视图持有者）
    //这样做的话就可以省的每次都要用findViewById的资源了
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder=null;
        if(convertView==null){//如果convertView 没有被用过时，就加载布局
            convertView=listInflater.inflate(R.layout.item_layout,parent,false);
            holder=new ViewHolder();
            //将找到的视图传给视图持有者
            holder.titleTv=(TextView)convertView.findViewById(R.id.reminderTitle);
            holder.contentTv=(TextView)convertView.findViewById(R.id.reminder);
            //将持有者放入convertView
            convertView.setTag(holder);
        }else{//convertView已经被复用了，说明convertView中已经设置过tag了，即holde
            //直接就可以获取holder
           holder=(ViewHolder) convertView.getTag();
        }
        ListItem item=dataList.get(position);
        holder.titleTv.setText(item.getTitle());
        holder.contentTv.setText(item.getContent());


        return convertView;
    }
    //这里定义的ViewHolder只对应某个特定的adapter,不同的listView对应的控件不同
    private class ViewHolder{
        TextView titleTv;
        TextView contentTv;
    }
}
