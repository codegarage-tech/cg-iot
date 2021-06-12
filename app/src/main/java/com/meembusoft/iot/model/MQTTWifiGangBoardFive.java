package com.meembusoft.iot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MQTTWifiGangBoardFive {

    private int light1 = 0;
    private int light1Regulator = 0;
    private int fan = 0;
    private int fanRegulator = 0;
    private int light2 = 0;
    private int socket = 0;

    public MQTTWifiGangBoardFive() {
    }

    public MQTTWifiGangBoardFive(int light1, int light1Regulator, int fan, int fanRegulator, int light2, int socket) {
        this.light1 = light1;
        this.light1Regulator = light1Regulator;
        this.fan = fan;
        this.fanRegulator = fanRegulator;
        this.light2 = light2;
        this.socket = socket;
    }

    public int getLight1() {
        return light1;
    }

    public void setLight1(int light1) {
        this.light1 = light1;
    }

    public int getLight1Regulator() {
        return light1Regulator;
    }

    public void setLight1Regulator(int light1Regulator) {
        this.light1Regulator = light1Regulator;
    }

    public int getFan() {
        return fan;
    }

    public void setFan(int fan) {
        this.fan = fan;
    }

    public int getFanRegulator() {
        return fanRegulator;
    }

    public void setFanRegulator(int fanRegulator) {
        this.fanRegulator = fanRegulator;
    }

    public int getLight2() {
        return light2;
    }

    public void setLight2(int light2) {
        this.light2 = light2;
    }

    public int getSocket() {
        return socket;
    }

    public void setSocket(int socket) {
        this.socket = socket;
    }

    @Override
    public String toString() {
        return "{" +
                "light1=" + light1 +
                ", light1Regulator=" + light1Regulator +
                ", fan=" + fan +
                ", fanRegulator=" + fanRegulator +
                ", light2=" + light2 +
                ", socket=" + socket +
                '}';
    }
}