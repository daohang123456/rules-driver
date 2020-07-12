package org.syq.bean;

import com.google.gson.Gson;

/**
 * Created by cage on 2020-07-12
 */

public class RuleEnvironment<V> {

    private String name;
    private V value;

    public RuleEnvironment(String name) {
        this.name = name;
    }

    public RuleEnvironment(String name, V value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
