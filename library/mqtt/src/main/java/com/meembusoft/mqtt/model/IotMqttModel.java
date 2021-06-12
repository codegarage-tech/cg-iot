package com.meembusoft.mqtt.model;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

public class IotMqttModel extends MqttModel {

    private Gson gson = new Gson();
    private Activity activity;
    private String userId = "", deviceId = "", topic = "";
    private Callback callback;

    public IotMqttModel(Activity activity, String userId, String deviceId) {
        this.activity = activity;
        this.userId = userId;
        this.deviceId = deviceId;
        this.topic = "/" + userId + "/" + deviceId;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void subscribeToTopic() {
        subscribe(activity, topic);
    }

    public void publishToTopic(Object object) {
        publish(activity, topic, gson.toJson(object));
    }

    @Override
    public void onMqttConnection(MqttConnection event) {
        super.onMqttConnection(event);
    }

    @Override
    public void onMqttState(MqttState event) {
        super.onMqttState(event);
    }

    @Override
    public void onMqttMessage(MqttMessage event) {
        super.onMqttMessage(event);
        Log.i("IotMqttModel", "event.topic=" + event.topic + ", event.message=" + event.message);
        if (event.topic.equalsIgnoreCase(topic)) {
            callback.onTopicChanged(event.message);
        }
    }

    public interface Callback {
        void onConnectionChanged(String connection);

        void onTopicChanged(Object object);
    }
}