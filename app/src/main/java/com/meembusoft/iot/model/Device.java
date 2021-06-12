package com.meembusoft.iot.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
@Parcel
public class Device {

    private String device_id = "";
    private Product product;
    private Color device_color;
    private Size device_size;
    private Configuration device_configuration;
    private Subscription device_subscription;
    private List<Configuration> device_configurations = new ArrayList<>();
    private int is_favorite;

    public Device() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Color getDevice_color() {
        return device_color;
    }

    public void setDevice_color(Color device_color) {
        this.device_color = device_color;
    }

    public Size getDevice_size() {
        return device_size;
    }

    public void setDevice_size(Size device_size) {
        this.device_size = device_size;
    }

    public Configuration getDevice_configuration() {
        return device_configuration;
    }

    public void setDevice_configuration(Configuration device_configuration) {
        this.device_configuration = device_configuration;
    }

    public Subscription getDevice_subscription() {
        return device_subscription;
    }

    public void setDevice_subscription(Subscription device_subscription) {
        this.device_subscription = device_subscription;
    }

    public List<Configuration> getDevice_configurations() {
        return device_configurations;
    }

    public void setDevice_configurations(List<Configuration> device_configurations) {
        this.device_configurations = device_configurations;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    @Override
    public String toString() {
        return "{" +
                "device_id='" + device_id + '\'' +
                ", product=" + product +
                ", device_color=" + device_color +
                ", device_size=" + device_size +
                ", device_configuration=" + device_configuration +
                ", device_subscription=" + device_subscription +
                ", device_configurations=" + device_configurations +
                ", is_favorite=" + is_favorite +
                '}';
    }
}