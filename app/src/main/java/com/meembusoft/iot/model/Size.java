package com.meembusoft.iot.model;

import org.parceler.Parcel;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
@Parcel
public class Size {

    private String id = "";
    private String size_name = "";
    /*This is for local usage*/
    private boolean isSelected = false;

    public Size() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
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
                ", size_name='" + size_name + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}