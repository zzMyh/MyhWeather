package cn.myh.zz.myhweather.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

import db.City;
import db.County;
import db.Province;

public class DBManager {
    private final static String dbName = "city_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }


/**
 * 插入一条省份信息
 */

public void insertProvince(Province province){
    DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
    DaoSession daoSession = daoMaster.newSession();
    ProvinceDao userDao = daoSession.getProvinceDao();
    userDao.insert(province);
}

/**
 * 读取省份信息返回省List
 *
 */

public List<Province> queryProvince(){
    DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
    DaoSession daoSession = daoMaster.newSession();
    ProvinceDao provinceDao = daoSession.getProvinceDao();
    QueryBuilder<Province> provinceQueryBuilder = provinceDao.queryBuilder();
    List<Province> list = provinceQueryBuilder.list();
    return list;
}


/**
 *
 * 插入一条市信息
 */
public void insertCity(City city){
    DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
    DaoSession daoSession = daoMaster.newSession();
    CityDao cityDao = daoSession.getCityDao();
    cityDao.insert(city);
}


    /**
     * 读取市信息返回省List
     *
     */

    public List<City> queryCity(Long id){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CityDao provinceDao = daoSession.getCityDao();
        QueryBuilder<City> qb = provinceDao.queryBuilder();
        qb.where(CityDao.Properties.ProvinceId.eq(id)).orderAsc(CityDao.Properties.ProvinceId);
//        qb.where(UserDao.Properties.Age.gt(age)).orderAsc(UserDao.Properties.Age);
//        qb.where(CityDao.Properties.ProvinceId.gt(id)).orderAsc(CityDao.Properties.ProvinceId);
        List<City> list = qb.list();
        return list;
    }
/**
 * 插入一条区/县数据
 */
public void insertCounty(County county){
    DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
    DaoSession daoSession = daoMaster.newSession();
    CountyDao countyDao = daoSession.getCountyDao();
    countyDao.insert(county);
}


    /**
     * 读取省份信息返回省List
     *
     */

    public List<County> queryCounty(Long id){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CountyDao provinceDao = daoSession.getCountyDao();
        QueryBuilder<County> qb = provinceDao.queryBuilder();
        qb.where(CountyDao.Properties.CityId.eq(id+"")).orderAsc(CountyDao.Properties.CityId);
//        qb.where(CountyDao.Properties.CityId.gt(id)).orderAsc(CountyDao.Properties.CityId);
        List<County> list = qb.list();
//        List<County> list = provinceQueryBuilder.list();
        return list;
    }


/*
    *//**
     * 插入一条记录
     *
     * @param user
     *//*
    public void insertUser(User user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insert(user);
    }

    *//**
     * 插入用户集合
     *
     * @param users
     *//*
    public void insertUserList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insertInTx(users);
    }

    *//**
     * 删除一条记录
     *
     * @param user
     *//*
    public void deleteUser(User user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.delete(user);
    }

    *//**
     * 更新一条记录
     *
     * @param user
     *//*
    public void updateUser(User user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.update(user);
    }

    *//**
     * 查询用户列表
     *//*
    public List<User> queryUserList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        List<User> list = qb.list();
        return list;
    }

    *//**
     * 查询用户列表
     *//*
    public List<User> queryUserList(int age) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Age.gt(age)).orderAsc(UserDao.Properties.Age);
        List<User> list = qb.list();
        return list;
    }*/
}