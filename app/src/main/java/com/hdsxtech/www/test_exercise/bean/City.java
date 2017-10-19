package com.hdsxtech.www.test_exercise.bean;

import io.realm.RealmObject;

/**
 * 作者:丁文 on 2017/10/19.
 * copyright: www.tpri.org.cn
 */

public class City extends RealmObject {

    /**
     * id : 311
     * name : 兰州
     */

    private int id;
    private String name;

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
}
