package com.meembusoft.iot.model;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MQTTWifiMotorTwo {

    private int motorInstant = 0;
    private int motorReserved = 0;

    public MQTTWifiMotorTwo() {
    }

    public MQTTWifiMotorTwo(int motorInstant, int motorReserved) {
        this.motorInstant = motorInstant;
        this.motorReserved = motorReserved;
    }

    public int getMotorInstant() {
        return motorInstant;
    }

    public void setMotorInstant(int motorInstant) {
        this.motorInstant = motorInstant;
    }

    public int getMotorReserved() {
        return motorReserved;
    }

    public void setMotorReserved(int motorReserved) {
        this.motorReserved = motorReserved;
    }

    @Override
    public String toString() {
        return "{" +
                "motorInstant=" + motorInstant +
                ", motorReserved=" + motorReserved +
                '}';
    }
}