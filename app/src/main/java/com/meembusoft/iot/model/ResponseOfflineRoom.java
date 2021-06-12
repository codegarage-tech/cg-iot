package com.meembusoft.iot.model;

import java.util.ArrayList;
import java.util.List;

public class ResponseOfflineRoom {

    private int status = 0;
    private String message = "";
    private List<Room> data = new ArrayList<>();

    public ResponseOfflineRoom() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Room> getData() {
        return data;
    }

    public void setData(List<Room> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}