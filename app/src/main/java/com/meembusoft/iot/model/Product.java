package com.meembusoft.iot.model;

import com.meembusoft.addtocart.model.CartListener;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
@Parcel
public class Product {

    private String product_id = "";
    private String product_qr_code = "";
    private String product_name = "";
    private ProductType product_type;
    private List<Image> product_images = new ArrayList<>();
    private double product_price = 0;
    private String product_description = "";
    private double product_discount_percentage = 0;
    private List<Color> product_colors = new ArrayList<>();
    private List<Size> product_sizes = new ArrayList<>();
    private List<Connection> product_connections = new ArrayList<>();
    private List<Subscription> product_subscriptions = new ArrayList<>();
    private int is_favorite = 0;
    private int item_quantity = 0;

    /*This is for local usage*/
    private boolean isSelected = false;

    public Product() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_qr_code() {
        return product_qr_code;
    }

    public void setProduct_qr_code(String product_qr_code) {
        this.product_qr_code = product_qr_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public ProductType getProduct_type() {
        return product_type;
    }

    public void setProduct_type(ProductType product_type) {
        this.product_type = product_type;
    }

    public List<Image> getProduct_images() {
        return product_images;
    }

    public void setProduct_images(List<Image> product_images) {
        this.product_images = product_images;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public double getProduct_discount_percentage() {
        return product_discount_percentage;
    }

    public void setProduct_discount_percentage(double product_discount_percentage) {
        this.product_discount_percentage = product_discount_percentage;
    }

    public List<Color> getProduct_colors() {
        return product_colors;
    }

    public void setProduct_colors(List<Color> product_colors) {
        this.product_colors = product_colors;
    }

    public List<Size> getProduct_sizes() {
        return product_sizes;
    }

    public void setProduct_sizes(List<Size> product_sizes) {
        this.product_sizes = product_sizes;
    }

    public List<Connection> getProduct_connections() {
        return product_connections;
    }

    public void setProduct_connections(List<Connection> product_connections) {
        this.product_connections = product_connections;
    }

    public List<Subscription> getProduct_subscriptions() {
        return product_subscriptions;
    }

    public void setProduct_subscriptions(List<Subscription> product_subscriptions) {
        this.product_subscriptions = product_subscriptions;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    public int getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(int item_quantity) {
        this.item_quantity = item_quantity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "{" +
                "product_id='" + product_id + '\'' +
                ", product_qr_code='" + product_qr_code + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_type='" + product_type + '\'' +
                ", product_images=" + product_images +
                ", product_price=" + product_price +
                ", product_description='" + product_description + '\'' +
                ", product_discount_percentage=" + product_discount_percentage +
                ", product_colors=" + product_colors +
                ", product_sizes=" + product_sizes +
                ", product_connections=" + product_connections +
                ", product_subscriptions=" + product_subscriptions +
                ", is_favorite=" + is_favorite +
                ", item_quantity=" + item_quantity +
                ", isSelected=" + isSelected +
                '}';
    }
}