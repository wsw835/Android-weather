package com.example.liu.weathercast.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import android.support.annotation.Nullable;

import android.util.AttributeSet;

import android.view.View;
import android.widget.Toast;


import com.example.liu.weathercast.activities.WeatherInfoActivity;
import com.example.liu.weathercast.entities.DataPackage;
import com.example.liu.weathercast.entities.Weather;
import com.example.liu.weathercast.utils.IconsProcessing;
import com.example.liu.weathercast.utils.SaveObject;

import java.util.List;

public class WeatherTrendView extends View {//绘制天气变化趋势图
    private Context context;
    //坐标点画笔，文字画笔，线画笔
    private Paint pointPaint, textPaint, textPaint2, linePaint,pointPaint2,textPaint3;
    //文字的高度
    private float textHeight;

    //一度对应的像素
    private int scale = 20;
    //点坐标的半径
    private int radiu = 5;
    //横坐标之间的间隔
    private int xSpace = 60;
    //温度文字与图标垂直之间的间隔
    private int ySpace=5;
    //保存温度值
    private int[] x = new int[7];
    //保存星期信息
    private String[] weekDay = new String[7];

    private  int[] icons=new int[7];
    //天气数据集合
    private List<Weather> weatherList;
    //最低最高温度
    private int[] minTemp;
    private int[] maxTemp;
    private int MinTemp;
    private  int MaxTemp;
    public DataPackage weatherTrendData;
    public WeatherTrendView(Context context,DataPackage dataPackage) {
        super(context);
        weatherTrendData=dataPackage;
        this.context=context;
        init();
        setWillNotDraw(false);
    }

    public WeatherTrendView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);



    }

    public void init() {//画笔等参数
        //坐标点画笔设置
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.WHITE);
        pointPaint2 = new Paint();
        pointPaint2.setAntiAlias(true);
        pointPaint2.setColor(Color.YELLOW);
        //连接线画笔设置
        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        //连线的宽度
        linePaint.setStrokeWidth(3);
        //连线为实线
        linePaint.setStyle(Paint.Style.FILL);

        //文本画笔

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(25);
        //文字的大小
        textPaint2 = new Paint();
        textPaint2.setAntiAlias(true);
        textPaint2.setColor(Color.RED);
        textPaint2.setTextSize(20);
        textPaint3=new Paint();
        textPaint3.setAntiAlias(true);
        textPaint3.setColor(Color.YELLOW);
        textPaint3.setTextSize(18);
        //计算文字的高度
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        //文字底部坐标减去顶部坐标
        textHeight = fontMetrics.bottom - fontMetrics.top;


        if (weatherTrendData != null) {
            weatherList = weatherTrendData.getLst();
            getTemp();
            //初始化星期数据
            for (int i = 0; i < weatherList.size(); i++) {
                weekDay[i] = weatherList.get(i).getWeek();
            }
            //获取对应天气的图标
            IconsProcessing iconsProcessing=new IconsProcessing(context);
            for(int i=0;i<weatherList.size();i++){
                icons[i]=iconsProcessing.judgeWeather(weatherList.get(i).getWeather());
            }

        }else{
            Toast.makeText(context,"数据为空",Toast.LENGTH_LONG).show();
        }

    }


    public void getTemp() {//获取天气
        minTemp = new int[7];
        maxTemp = new int[7];
        for (int i = 0; i < weatherList.size(); i++) {
            Weather weather = new Weather();
            weather = weatherList.get(i);
            String temp = weather.getTemperature();
            String[] strs = temp.split("~");
            minTemp[i] = Integer.parseInt(strs[0].split("℃")[0]);
            maxTemp[i] = Integer.parseInt(strs[1].split("℃")[0]);
        }
        MinTemp=minTemp[0];
        MaxTemp=maxTemp[0];
        for(int i=1;i<minTemp.length;i++){
            if(MinTemp>minTemp[i]){
                MinTemp=minTemp[i];
            }
            if(MaxTemp<maxTemp[i]){
                MaxTemp=maxTemp[i];
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {//在界面中进行绘制

        super.onDraw(canvas);

       int width = this.getWidth();//获取控件的宽度
        xSpace = width / x.length;//两个点之间水平间距
        for (int i = 0; i < x.length; i++) {//初始化各个点x轴坐标，起始点偏移40px
            x[i] = 40 + i * xSpace;

        }
        for (int i = 0; i < weekDay.length; i++) {
            // 写出每个星期的文字
            canvas.drawText(weekDay[i], x[i] - 20, 20, textPaint);
            //写出每天对应的天气
            canvas.drawText(weatherList.get(i).getWeather(), x[i] - 20, 20 + textHeight, textPaint3);
        }
        textPaint.setTextSize(18);
        textPaint.setColor(Color.WHITE);
        //获取最高温度最低温度中间值
        int mindTem=(MaxTemp+MinTemp)/2;
        //获取控件的y轴中线，中线为中间温度
        int centerHeight=this.getHeight()/2;
        //绘制最高温度折线
        for(int i=0;i<weatherList.size();i++){
            //每天的最高温度与中间温度的差值，负在上正的在下
            float point=(-(maxTemp[i]-mindTem))*scale;
            canvas.drawCircle(x[i],centerHeight+point,radiu,pointPaint);
            //绘制该点对应的文字
            canvas.drawText(maxTemp[i]+"℃",x[i]-12,centerHeight+point-textHeight/2-ySpace,textPaint2);

            //跟据id来绘制图片
           Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),icons[i]);

            //获取图片的宽高
           int bHeight=bitmap.getHeight();
           int bWidth=bitmap.getWidth();
           //定义先要的宽高
            int newHeight=90;
            int newWidth=90;
           //计算需要压缩的比例
            float scaleWidth=((float)newWidth)/bWidth;
            float scaleHeight=((float)newHeight)/bHeight;
            //获取想要缩放的matrix
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth,scaleHeight);
            //获取新的bitmap
            bitmap=Bitmap.createBitmap(bitmap,0,0,bWidth,bHeight,matrix,true);
            //在视图上绘制图片
            canvas.drawBitmap(bitmap,x[i]-bitmap.getWidth()/2,centerHeight+point-textHeight-2*ySpace-bitmap.getHeight(),null);
            //当该点不是最后一个点时，绘制该点与下一个点之间的连线
            if(i!=weatherList.size()-1){
                int nextTopTem=maxTemp[i+1];
                float pointNext=(-(nextTopTem-mindTem))*scale;
                canvas.drawLine(x[i],centerHeight+point,x[i+1],centerHeight+pointNext,linePaint);
            }

        }
        //绘制最低温度折线
        linePaint.setColor(Color.YELLOW);
        for(int i=0;i<weatherList.size();i++){
            //该温度相对于中线的偏移量
            float point=(-(minTemp[i]-mindTem))*scale;
            canvas.drawText(minTemp[i]+"℃",x[i]-12,centerHeight+point+textHeight+ySpace,textPaint2);
            canvas.drawCircle(x[i],centerHeight+point,radiu,pointPaint2);
            //跟据id来绘制图片


            if(i !=weatherList.size()-1){
                //不是最后一个点，则绘制该点到下一个点之间的连线
                float pointNext=(-(minTemp[i+1]-mindTem))*scale;

                canvas.drawLine(x[i],centerHeight+point,x[i+1],centerHeight+pointNext,linePaint);
            }

        }
    }


}
