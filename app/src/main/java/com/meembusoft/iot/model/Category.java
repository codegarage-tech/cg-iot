package com.meembusoft.iot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class Category {

    private String category_id = "";
    private String category_name = "";
    private String category_thumbnail = "";
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_thumbnail() {
        return category_thumbnail;
    }

    public void setCategory_thumbnail(String category_thumbnail) {
        this.category_thumbnail = category_thumbnail;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "{" +
                "category_id='" + category_id + '\'' +
                ", category_name='" + category_name + '\'' +
                ", category_thumbnail='" + category_thumbnail + '\'' +
                ", products=" + products +
                '}';
    }
}