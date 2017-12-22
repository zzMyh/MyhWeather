package cn.myh.zz.myhweather.Utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import cn.myh.zz.myhweather.dao.DBManager;
import db.City;
import db.County;
import db.Province;

/**
 * Created by lenovo on 2017/12/21.
 */

public class WeatherUtils {

    public synchronized static boolean handleProvinces(DBManager manager,String result){


        /**
         * 解析处理服务器返回的省级数据
         */
        if (!TextUtils.isEmpty(result)){
            String[] allProvinces = result.split(",");
            if (allProvinces!=null&&allProvinces.length>0){
                for (String p:allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceName(array[1]);
                    province.setProvinceCode(array[0]);
                    Log.d("WeatherUtils","解析的实体对象是："+province.toString());
                    //将解析后的省份表插入到数据库中
                    manager.insertProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     *读取省份数据
     */
public List<Province> loadProvinces(DBManager manager){

    return manager.queryProvince();
}
    /**
     * 解析处理服务器返回的市级数据
     */

    public synchronized static boolean handleCity(DBManager manager,String result,Long pid){

        if (!TextUtils.isEmpty(result)){
            String[] citys = result.split(",");
            for (String c:citys){
                String[] arrays = c.split("\\|");
                City city = new City();
                city.setCityName(arrays[1]);
                city.setCityCode(arrays[0]);
                city.setProvinceId(pid+"");
                manager.insertCity(city);
            }
            return true;
        }
        return false;
    }

    /**
     *读取市数据
     */
    public List<City> loadCity(DBManager manager,Long id){

        return manager.queryCity(id);
    }


    /**
     * 解析处理区/县数据
     */

    public synchronized static boolean handleCounty(DBManager manager,String result,long cid){

        if (!TextUtils.isEmpty(result)){
            String[] countys = result.split(",");
            if (countys!=null&&countys.length>0){
                for (String c:countys){
                    String[] arrays = c.split("\\|");
                    County county = new County();
                    county.setCountyName(arrays[1]);
                    county.setCountyCode(arrays[0]);
                    county.setCityId((int)cid+"");
                    manager.insertCounty(county);
                }
                return true;
            }

        }
        return false;
    }

    /**
     *读取区县数据
     */
    public List<County> loadCounty(DBManager manager,Long id){

        return manager.queryCounty(id);
    }
}
