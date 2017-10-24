package com.hdsxtech.www.test_exercise.api;


import com.hdsxtech.www.test_exercise.bean.City;
import com.hdsxtech.www.test_exercise.bean.Country;
import com.hdsxtech.www.test_exercise.bean.Province;
import com.hdsxtech.www.test_exercise.bean.Weather;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 作者:丁文 on 2017/10/19.
 * copyright: www.tpri.org.cn
 */

public interface ApiService {

    /**
     * 获取省级列表
     */
    @GET("china/")
    Observable<List<Province>> getProvince();
    /**
     * 获取市级列表
     */
    @GET("china/{id}")
    Observable<List<City>> getCity(@Path("id") int id);
    /**
     * 获取区县列表
     */
    @GET("china/{id}/{id}")
    Observable<List<Country>> getCountry(@Path("id") int id_province,@Path("id") int id_city);
    /**
     * 获取天气状况
     */
    @GET("city")
    Observable<Weather> getWeather(@Field("cityid") String cityId,
                                   @Field("key") String key);

}
