package com.hdsxtech.www.test_exercise.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 作者:丁文 on 2017/10/19.
 * copyright: www.tpri.org.cn
 */

public class Province extends RealmObject {

    /**
     * id : 1
     * name : 北京
     */
    @PrimaryKey
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
