package tech.codegarage.iot.model;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ParamWifiConnect {

    private String ssid = "";
    private String bssid = "";
    private String password = "";
    private int deviceCount = -1;
    private boolean broadcast = false;

    public ParamWifiConnect() {
    }

    public ParamWifiConnect(String ssid, String bssid, String password, int deviceCount, boolean broadcast) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.password = password;
        this.deviceCount = deviceCount;
        this.broadcast = broadcast;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    @Override
    public String toString() {
        return "{" +
                "ssid='" + ssid + '\'' +
                ", bssid='" + bssid + '\'' +
                ", password='" + password + '\'' +
                ", deviceCount=" + deviceCount +
                ", broadcast=" + broadcast +
                '}';
    }
}