package com.example.snaphelpertest;

/**
 * @Description: 测试item类，两个成员：id和name
 * @Author: Linda
 * @Time: 2020/5/7 21:14.
 */
public class TestBean {
    
    private int id;
    private String name;

    public TestBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

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
