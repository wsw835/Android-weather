package com.example.liu.weathercast.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liu.weathercast.R;

import com.example.liu.weathercast.activities.MainActivity;
import com.example.liu.weathercast.activities.WeatherInfoActivity;
import com.example.liu.weathercast.entities.City;
import com.example.liu.weathercast.entities.County;
import com.example.liu.weathercast.entities.Province;
import com.example.liu.weathercast.utils.AddressParse;
import com.example.liu.weathercast.utils.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class QueryCitiesFragment extends Fragment {
    private static String tag = QueryCitiesFragment.class.getSimpleName();
    //判断查询的等级
    private final int LEVEL_PROVINCE = 1;
    private final int LEVEL_CITY = 2;
    private final int LEVEL_COUNTY = 3;
    private ProgressDialog progressDialog;
    //保存当前查询等级
    private int current_level;
    //标题
    private TextView tv_title;

    //返回上一层的按钮
    private Button button;

    //listview的数据源
    private List<String> lstdata = new ArrayList<String>();

    //省的信息
    private List<Province> provinces;

    //市的信息
    private List<City> cities;

    //县的信息
    private List<County> counties;

    //城市的列表
    private ListView city_list;

    //listView填充器
    private ArrayAdapter<String> adapter;
    //选中的省
    private Province province;
    private City city;
    private County county;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {//初始化视图，并将视图填充
        //将视图填充
        View view = inflater.inflate(R.layout.city_choose_layout, container, false);
        button = view.findViewById(R.id.back_button);
        city_list = view.findViewById(R.id.city_list);
        tv_title = view.findViewById(R.id.title_text);
        //获取数据源，以及布局位置，和布局样式
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lstdata);
        //将listview填充
        city_list.setAdapter(adapter);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {//在活动加载完了以后为，其设置点击事件
        super.onActivityCreated(savedInstanceState);
        city_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (current_level == LEVEL_PROVINCE) {//如果当前访问的是省级时
                    province = provinces.get(position);
                    getCities();
                } else if (current_level == LEVEL_CITY) {
                    city = cities.get(position);
                    getCounty();
                } else if (current_level == LEVEL_COUNTY) {
                    county = counties.get(position);

                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherInfoActivity.class);
                        intent.putExtra("city", county.getCountyName());
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof WeatherInfoActivity){//如果是在getweatherInfo选择城市的话，直接进行更新界面信息
                        WeatherInfoActivity getWeatherInfo=(WeatherInfoActivity) getActivity();

                        getWeatherInfo.drawerLayout.closeDrawers();

                        getWeatherInfo.swipeRefreshLayout.setRefreshing(true);//在更新时，显示更新样式
                        getWeatherInfo.getWeather(county.getCountyName());

                    }

                }
            }
        });
        //设置返回事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_level == LEVEL_CITY) {
                    getProvinces();
                } else if (current_level == LEVEL_COUNTY) {
                    getCities();
                }
            }
        });
        getProvinces();
    }


    private void getProvinces() {//获取城市信息
        button.setVisibility(View.GONE);
        tv_title.setText("中国");
        //首先在数据库中查找数据,查找出全部的省的信息
        provinces = DataSupport.findAll(Province.class);
        if (provinces.size() > 0) {//如果数据中有数据
            lstdata.clear();
            //将所有的省放入lstdata中
            for (Province p : provinces) {
                lstdata.add(p.getProvinceName());
            }
            //让listview填充知道数据已经改变
            adapter.notifyDataSetChanged();
            //将选中状态致为0
            city_list.setSelection(0);
            //设置当前访问等级
            current_level = LEVEL_PROVINCE;
        } else {//如果数据库中没有数据的话，从网上获取数据
            String address = "http://guolin.tech/api/china";
            queryDate(address, "province");
        }

    }
    private void getCities() {//获取城市的信息
        button.setVisibility(View.VISIBLE);
        tv_title.setText(province.getProvinceName());
        //根据省的id获取所有城市的名字
        cities = DataSupport.where("provinceid=?", String.valueOf(province.getId())).find(City.class);
        if (cities.size() > 0) {//如果在其中有数据的话
            //将之前放入的数据清除
            lstdata.clear();
            for (City c : cities) {
                lstdata.add(c.getCityName());
            }
            adapter.notifyDataSetChanged();
            city_list.setSelection(0);
            current_level = LEVEL_CITY;
        } else {

            String address = "http://guolin.tech/api/china/" + province.getProvinceCode();
            queryDate(address, "city");
        }

    }

    private void getCounty() {//获取县的名字
        button.setVisibility(View.VISIBLE);
        tv_title.setText(city.getCityName());
        //根剧市名获取县的名字
        counties = DataSupport.where("cityId=?", String.valueOf(city.getId())).find(County.class);
        if (counties.size() > 0) {
            lstdata.clear();
            for (County c : counties) {
                lstdata.add(c.getCountyName());
            }
            adapter.notifyDataSetChanged();
            city_list.setSelection(0);
            current_level = LEVEL_COUNTY;
        } else {
            String address = "http://guolin.tech/api/china/" + province.getProvinceCode() + "/" + city.getCityCode();
            queryDate(address, "county");
        }

    }

    private void queryDate(String address, final String type) {
        showProgressDialog();
        //从网上获取信息,此方法是在异步线程中获取数据，要想传递消息或数据必须要在主线程中
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //当获取失败时，返回主线程中通知用户消息
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "网络链接失败", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //判断是否访问完了数据
                Boolean callMethod = false;
                if ("province".equals(type)) {//如果要获取的是省的话
                    String provinces = response.body().string();
                    //将省的信息查询出来并放入数据库
                    callMethod = AddressParse.handleProvinceResponse(provinces);
                } else if ("city".equals(type)) {
                    String cities = response.body().string();
                    callMethod = AddressParse.handleCityResponse(cities, province.getId());
                } else if ("county".equals(type)) {
                    String counties = response.body().string();
                    callMethod = AddressParse.handleCountyResponse(counties, city.getId());
                }

                if (callMethod) {//如果数据访问成功了,则在主线程中更新数据
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                getProvinces();
                            } else if ("city".equals(type)) {
                                getCities();
                            } else if ("county".equals(type)) {
                                getCounty();
                            }
                        }
                    });

                }
            }
        });

    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

