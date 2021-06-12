package com.meembusoft.iot.model;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class User {

    private String user_id = "";
    private String user_facebook_id = "";
    private String user_name = "";
    private String user_phone = "";
    private String user_email = "";
    private String user_address = "";
    private String user_password = "";
    private Image user_image;

    public User() {
    }

    public User(String user_id, String user_facebook_id, String user_name, String user_phone, String user_email, String user_address, String user_password, Image user_image) {
        this.user_id = user_id;
        this.user_facebook_id = user_facebook_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_email = user_email;
        this.user_address = user_address;
        this.user_password = user_password;
        this.user_image = user_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_facebook_id() {
        return user_facebook_id;
    }

    public void setUser_facebook_id(String user_facebook_id) {
        this.user_facebook_id = user_facebook_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public Image getUser_image() {
        return user_image;
    }

    public void setUser_image(Image user_image) {
        this.user_image = user_image;
    }

    @Override
    public String toString() {
        return "{" +
                "user_id='" + user_id + '\'' +
                ", user_facebook_id='" + user_facebook_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_address='" + user_address + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_image=" + user_image +
                '}';
    }
}