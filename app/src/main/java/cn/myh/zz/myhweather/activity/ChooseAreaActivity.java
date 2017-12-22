package cn.myh.zz.myhweather.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.myh.zz.myhweather.R;
import cn.myh.zz.myhweather.Utils.WeatherUtils;
import cn.myh.zz.myhweather.dao.DBManager;
import db.City;
import db.County;
import db.Province;
import okhttp3.Call;
import okhttp3.Response;

public class ChooseAreaActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ListView lvList;


    //省，市，县对应的级别
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    //当前选中的级别
    private int currentLevel;

    //数据源
    List<String> dataList = new ArrayList<>();

    //省数据源
    List<Province> provinces;
    //市 数据源
    List<City> citys;
    //区/县
    List<County> countys;

    //点击省份Item的条目
    Province selectProvince;
    //点击市的Item
    City selectCity;
    //点击的县Item
    County selectCounty;

    private ArrayAdapter<String> adapter;
    private DBManager manager;
    private WeatherUtils weatherUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);

        manager = DBManager.getInstance(this);
        weatherUtils = new WeatherUtils();
        initView();
        initData();
        initListener();

    }

    private void initData() {
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        lvList.setAdapter(adapter);
        queryProvinces();


    }

    private void initListener() {
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel==LEVEL_PROVINCE){//当前级别是省
                    selectProvince = provinces.get(position);
                    queryCity();
                }else if (currentLevel==LEVEL_CITY){
                    selectCity = citys.get(position);
                    queryCounty();
                }else if (currentLevel==LEVEL_COUNTY){
                    selectCounty = countys.get(position);

                }
            }
        });

    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryProvinces() {
        //从本地获取
        provinces = weatherUtils.loadProvinces(manager);
        if (provinces.size()>0){
            dataList.clear();
            for (Province province:provinces){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
                lvList.setSelection(0);
            tvTitle.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else {
            //如果本地无数据请求网络数据
            queryFromServer(null, "provice");
        }

    }

    private void queryCity(){
        //从本地获取
        Long id = selectProvince.getId();
        citys = weatherUtils.loadCity(manager,id);
        if (citys.size()>0){
            dataList.clear();
            for (City city:citys){
                dataList.add(city.getCityName());
            }
            //刷新界面
            adapter.notifyDataSetChanged();
            //标题
            tvTitle.setText(selectProvince.getProvinceName());
            //界面移动到最上方
            lvList.setSelection(0);
            //重新设置Level
            currentLevel = LEVEL_CITY;
        }else {
            //从网络获取
            queryFromServer(selectProvince.getProvinceCode(),"city");
        }

    }

    /**
     *请求区/县数据
     */
    private void queryCounty() {
        countys = weatherUtils.loadCounty(manager,selectCity.getId());
        if (countys.size()>0){
            //先清空datas
            dataList.clear();
            for (County county:countys){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            tvTitle.setText(selectCity.getCityName());
            lvList.setSelection(0);
            currentLevel = LEVEL_COUNTY;

        }else {
            //请求网络数据
            queryFromServer(selectCity.getCityCode(),"county");
        }
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.title_text);
        lvList = (ListView) findViewById(R.id.list_view);
    }

    /**
     * 从网路获取数据
     * @param code
     * @param type
     */
    private void queryFromServer(String code, final String type) {
        String address;
        if (!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code +".xml";
        }else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        Log.d("ChooseAreaActivity","请求的url是：+"+address);
        showProgress();
        OkHttpUtils.get().url(address).build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {

                //加载失败
                closeProgressDialog();
                Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response, int id) {
                boolean bResult = false;
                if ("provice".equals(type)){
                    Log.d("ChooseAreaAct","返回的结果是：+"+(String) response);
                    bResult = weatherUtils.handleProvinces(manager, (String) response);
                }else if ("city".equals(type)){
                    bResult = weatherUtils.handleCity(manager, (String) response,selectProvince.getId());
                }else if ("county".equals(type)){
                    bResult = weatherUtils.handleCounty(manager, (String) response,selectCity.getId());
                }
                if (bResult){
                    closeProgressDialog();
                    if ("provice".equals(type)){
                        queryProvinces();
                    }else if ("city".equals(type)){
                        queryCity();
                    }else if ("county".equals(type)){
                        queryCounty();
                    }
                }

            }
        });
    }
    private ProgressDialog progressDialog;

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
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

    @Override
    public void onBackPressed() {
        if (currentLevel==LEVEL_COUNTY){
            queryCity();

        }else if (currentLevel==LEVEL_CITY){
            queryProvinces();
        }else {
            finish();
        }
    }
}
