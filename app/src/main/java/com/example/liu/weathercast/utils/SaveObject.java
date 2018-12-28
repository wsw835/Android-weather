package com.example.liu.weathercast.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.ContextThemeWrapper;

import com.example.liu.weathercast.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 利用sharepreferenec保存在网上获取下来的对象
 */
public class SaveObject {
    //将对象保存到sharePreference中
    public boolean saveObject(Context context,Object obj,String key){

        SharedPreferences share= PreferenceManager.getDefaultSharedPreferences(context);
        if(obj==null){//如果对象为空，则删除对应的键名
           SharedPreferences.Editor editor= share.edit().remove(key);
            return editor.commit();
        }

        //将对象放入对象输出流中，然后转为字节数组
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=null;
        try {
            oos=new ObjectOutputStream(bos);
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
        //对byte数组进行base64编码，转为base64字符串
        String objectStringBase64 =new String(Base64.encode(bos.toByteArray(),Base64.DEFAULT));
        try {
            bos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor=share.edit();
        editor.putString(key,objectStringBase64);
        return editor.commit();
    }
    //从sharepreference中获取对应的字符串，并转为对象
    public Object getObejectData(Context context,String key){
        SharedPreferences share=PreferenceManager.getDefaultSharedPreferences(context);
       String stringObjectBase64= share.getString(key,"");
       //没有获取到数据时
       if(stringObjectBase64==null||stringObjectBase64.equals("")){
            return null;
       }
       //将获取的字符串解码成字节数组
      byte[] bytesArray= Base64.decode(stringObjectBase64.getBytes(),Base64.DEFAULT);
        ByteArrayInputStream bais=new ByteArrayInputStream(bytesArray);
        ObjectInputStream ois=null;
        try {
            //将字节数组转化为对象
          ois=new ObjectInputStream(bais);
          Object obj=ois.readObject();
          ois.close();
          bais.close();
          return  obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveIcons(Context context){
        Map<String,Integer> map=new HashMap<String ,Integer>();
        map.put("云",R.mipmap.cloudy);
        map.put("雾",R.mipmap.foggy);
        map.put("晴",R.mipmap.sunny);
        map.put("雪",R.mipmap.snow);
        map.put("风",R.mipmap.storm);
        map.put("雨",R.mipmap.rain);
        map.put("雷",R.mipmap.thunder_shower);
       this.saveObject(context,map,"icons");
    }

}
