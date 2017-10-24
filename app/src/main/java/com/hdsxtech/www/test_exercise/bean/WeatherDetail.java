package com.hdsxtech.www.test_exercise.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 作者:丁文 on 2017/10/20.
 * copyright: www.tpri.org.cn
 */

public class WeatherDetail extends RealmObject {
    @PrimaryKey
    private String cityId;
    private long  saveTime;
    private String weather;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
