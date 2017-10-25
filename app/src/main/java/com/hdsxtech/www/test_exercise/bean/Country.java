package com.hdsxtech.www.test_exercise.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 作者:丁文 on 2017/10/19.
 * copyright: www.tpri.org.cn
 */

public class Country extends RealmObject {
    /**
     * id : 1
     * name : 北京
     * weather_id : CN101010100
     */
    private int cityId;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    private int id;
    private String name;
    @PrimaryKey
    private String weather_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(String weather_id) {
        this.weather_id = weather_id;
    }

}
