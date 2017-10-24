package com.hdsxtech.www.test_exercise.utils;

import com.hdsxtech.www.test_exercise.api.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 作者:丁文 on 2017/10/19.
 * copyright: www.tpri.org.cn
 */

public class ApiUtils {
    public static final String bath_url = "http://guolin.tech/api/";
    private static ApiService apiService;
    public static ApiService getApiService(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15 * 1000L, TimeUnit.MILLISECONDS)//15
                .readTimeout(20 * 1000L, TimeUnit.MILLISECONDS)//20
                .writeTimeout(30 * 1000L, TimeUnit.MILLISECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(bath_url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        return apiService;
    }
    public synchronized static ApiService getInstance(){
        if (apiService == null){
            getApiService();
        }
        return apiService;
    }
}
