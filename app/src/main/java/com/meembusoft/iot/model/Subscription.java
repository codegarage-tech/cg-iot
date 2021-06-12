package com.meembusoft.iot.model;

import org.parceler.Parcel;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
@Parcel
public class Subscription {

    private String id = "";
    private String subscription_name = "";
    private double subscription_price = 0;
    private String subscription_description = "";
    /*This is for local usage*/
    private boolean isSelected = false;

    public Subscription() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscription_name() {
        return subscription_name;
    }

    public void setSubscription_name(String subscription_name) {
        this.subscription_name = subscription_name;
    }

    public double getSubscription_price() {
        return subscription_price;
    }

    public void setSubscription_price(double subscription_price) {
        this.subscription_price = subscription_price;
    }

    public String getSubscription_description() {
        return subscription_description;
    }

    public void setSubscription_description(String subscription_description) {
        this.subscription_description = subscription_description;
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
                "id='" + id + '\'' +
                ", subscription_name='" + subscription_name + '\'' +
                ", subscription_price='" + subscription_price + '\'' +
                ", subscription_description='" + subscription_description + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}