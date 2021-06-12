package com.meembusoft.iot.model;

import org.parceler.Parcel;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
@Parcel
public class ProductType {

    private String product_type_id = "";
    private String product_type_name = "";

    public ProductType() {
    }

    public String getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(String product_type_id) {
        this.product_type_id = product_type_id;
    }

    public String getProduct_type_name() {
        return product_type_name;
    }

    public void setProduct_type_name(String product_type_name) {
        this.product_type_name = product_type_name;
    }

    @Override
    public String toString() {
        return "{" +
                "product_type_id='" + product_type_id + '\'' +
                ", product_type_name='" + product_type_name + '\'' +
                '}';
    }
}