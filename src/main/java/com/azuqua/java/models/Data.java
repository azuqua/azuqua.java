package com.azuqua.java.models;

/**
 * Created by sasidhar on 12/11/16.
 */
public class Data {
    private String key;
    private String defaultValue;
    private String group;
    private String collection;
    private String type;

    public Data() {
    }

    public Data(String key, String defaultValue, String group, String collection, String type) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.group = group;
        this.collection = collection;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
