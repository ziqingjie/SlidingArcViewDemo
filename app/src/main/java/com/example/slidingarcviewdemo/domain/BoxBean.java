package com.example.slidingarcviewdemo.domain;

import java.util.ArrayList;

/**
 * @author 小訾
 * @projectName SlidingArcViewDemo
 * @packageName com.example.slidingarcviewdemo.domain
 * @description:
 * @date :2022/2/7
 */
public class BoxBean {
    private String name;
    private String url;
    private ArrayList<Products> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }
}
