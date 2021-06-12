package com.meembusoft.iot.model;

import org.parceler.Parcel;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
@Parcel
public class Connection {

    private String connection_id = "";
    private String connection_name = "";
    private String connection_thumbnail = "";

    public Connection() {
    }

    public String getConnection_id() {
        return connection_id;
    }

    public void setConnection_id(String connection_id) {
        this.connection_id = connection_id;
    }

    public String getConnection_name() {
        return connection_name;
    }

    public void setConnection_name(String connection_name) {
        this.connection_name = connection_name;
    }

    public String getConnection_thumbnail() {
        return connection_thumbnail;
    }

    public void setConnection_thumbnail(String connection_thumbnail) {
        this.connection_thumbnail = connection_thumbnail;
    }

    @Override
    public String toString() {
        return "{" +
                "connection_id='" + connection_id + '\'' +
                ", connection_name='" + connection_name + '\'' +
                ", connection_thumbnail='" + connection_thumbnail + '\'' +
                '}';
    }
}