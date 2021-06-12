package com.meembusoft.iot.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
@Parcel
public class Room {

    private String room_id = "";
    private String room_name = "";
    private String room_thumbnail = "";
    private List<Device> devices = new ArrayList<>();
    /*This is for local usage*/
    private boolean isSelected = false;


    public Room() {
    }

    public Room(String room_id, String room_name) {
        this.room_id = room_id;
        this.room_name = room_name;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_thumbnail() {
        return room_thumbnail;
    }

    public void setRoom_thumbnail(String room_thumbnail) {
        this.room_thumbnail = room_thumbnail;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
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
                "room_id='" + room_id + '\'' +
                ", room_name='" + room_name + '\'' +
                ", room_thumbnail='" + room_thumbnail + '\'' +
                ", devices=" + devices +
                ", isSelected=" + isSelected +
                '}';
    }
}