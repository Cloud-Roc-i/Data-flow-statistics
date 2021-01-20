package com.study.domain;

public class CategoryClickCount {
    private String name;
    private long value;

    public void setValue(long value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }
}
