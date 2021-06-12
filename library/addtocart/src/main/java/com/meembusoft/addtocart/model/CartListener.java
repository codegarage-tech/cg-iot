package com.meembusoft.addtocart.model;

import com.meembusoft.realmmanager.RealmListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public interface CartListener  {

    public String getId();
    public double getPrice();
    public double getDiscountPercentage();
    public double getDiscountPrice();
    public double getPriceAfterDiscount();
    public boolean hasDiscount();
    public double getSinglePrice();
    public double getTotalPrice();
    public boolean isSelected();
}