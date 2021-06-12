package com.meembusoft.retrofitmanager;

import com.google.gson.Gson;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class APIResponse<F> {

    private int status = 0;
    private String msg = "";
    private int total_count = 0;
    private F data;
    private String current_datetime = "";

    public static <T> T getObjectFromJSONString(String jsonString, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);
    }

    public static <T> String getJSONStringFromObject(T object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public F getData() {
        return data;
    }

    public void setData(F data) {
        this.data = data;
    }

    public String getCurrent_datetime() {
        return current_datetime;
    }

    public void setCurrent_datetime(String current_datetime) {
        this.current_datetime = current_datetime;
    }

    @Override
    public String toString() {
        return "{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", total_count=" + total_count +
                ", data=" + data +
                ", current_datetime='" + current_datetime + '\'' +
                '}';
    }
}
