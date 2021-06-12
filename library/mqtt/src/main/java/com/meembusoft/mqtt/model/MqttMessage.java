package com.meembusoft.mqtt.model;

public class MqttMessage {
    public final String topic;
    public final String message;

    public MqttMessage(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }
}