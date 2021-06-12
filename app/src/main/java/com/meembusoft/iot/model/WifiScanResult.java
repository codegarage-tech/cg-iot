package com.meembusoft.iot.model;

import org.parceler.Parcel;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
@Parcel
public class WifiScanResult {

    private String ssid = "";
    private String bssid = "";
    private String strength = "";
    private int frequency = 0;
    private String security = "";
    private String provider = "";
    private boolean isSelected = false;
    private boolean isConnected = false;

    public WifiScanResult() {
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    @Override
    public String toString() {
        return "{" +
                "ssid='" + ssid + '\'' +
                ", bssid='" + bssid + '\'' +
                ", strength='" + strength + '\'' +
                ", frequency='" + frequency + '\'' +
                ", security='" + security + '\'' +
                ", provider='" + provider + '\'' +
                ", isSelected=" + isSelected +
                ", isConnected=" + isConnected +
                '}';
    }
}