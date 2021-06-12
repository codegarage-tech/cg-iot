package com.meembusoft.iot.model;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MQTTWifiMotorOne {

    private int motorInstant = 0;

    public MQTTWifiMotorOne() {
    }

    public MQTTWifiMotorOne(int motorInstant) {
        this.motorInstant = motorInstant;
    }

    public int getMotorInstant() {
        return motorInstant;
    }

    public void setMotorInstant(int motorInstant) {
        this.motorInstant = motorInstant;
    }

    @Override
    public String toString() {
        return "{" +
                "motorInstant=" + motorInstant +
                '}';
    }
}