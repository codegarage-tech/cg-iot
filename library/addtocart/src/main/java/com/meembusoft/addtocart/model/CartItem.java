package com.meembusoft.addtocart.model;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
@Parcel
public class CartItem extends RealmObject implements CartListener {

    @PrimaryKey
    private String id;
    private String description;
    private double price = 0;
    private String name;
    private String image;
    private String size;
    private int quantity = 0;
    private String color;
    private double discountPrice = 0;
    private double discountPercentage = 0;
    private boolean isSelected = false;

    public CartItem() {
    }

    public CartItem(String id, double price, String name) {
        this.id = id;
        this.price = price;
        this.name = name;
    }

    public CartItem(String id, String description, double price, String name, String image) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.name = name;
        this.image = image;
    }

    /*******************
     * Override methods *
     ********************/
    public String getId() {
        return id;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getDiscountPercentage() {
        return discountPercentage;
    }

    @Override
    public double getDiscountPrice() {
        if (discountPrice > 0) {
            return discountPrice;
        } else if (discountPercentage > 0) {
            return (getPrice() * getDiscountPercentage()) / 100;
        } else {
            return 0;
        }
    }

    @Override
    public double getPriceAfterDiscount() {
        return getPrice() - getDiscountPrice();
    }

    @Override
    public boolean hasDiscount() {
        return (getDiscountPrice() > 0);
    }

    @Override
    public double getSinglePrice() {
        return (hasDiscount() ? getPriceAfterDiscount() : getPrice());
    }

    @Override
    public double getTotalPrice() {
        return getQuantity() * getSinglePrice();
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    /************************************
     * Locale getter and setter methods *
     ************************************/
    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + getId() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", price=" + getPrice() +
                ", name='" + getName() + '\'' +
                ", image=" + getImage() +
                ", size='" + getSize() + '\'' +
                ", quantity=" + getQuantity() +
                ", color='" + getColor() + '\'' +
                ", hasDiscount=" + hasDiscount() +
                ", discountPrice=" + getDiscountPrice() +
                ", discountPercentage=" + getDiscountPercentage() +
                ", priceAfterDiscount=" + getPriceAfterDiscount() +
                ", singlePrice=" + getSinglePrice() +
                ", totalPrice=" + getTotalPrice() +
                ", isSelected=" + isSelected() +
                '}';
    }
}