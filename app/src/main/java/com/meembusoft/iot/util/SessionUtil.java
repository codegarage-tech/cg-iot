package com.meembusoft.iot.util;

import android.content.Context;

import com.meembusoft.iot.model.Product;
import com.meembusoft.iot.model.User;
import com.meembusoft.retrofitmanager.APIResponse;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SessionUtil {

    // Session key
    private static final String SESSION_KEY_USER = "SESSION_KEY_USER";
    private static final String SESSION_KEY_LAST_SELECTED_DEVICE_POSITION = "SESSION_KEY_LAST_SELECTED_DEVICE_POSITION";
    private static final String SESSION_KEY_LAST_SELECTED_DEVICE_SECTION_NAME = "SESSION_KEY_LAST_SELECTED_DEVICE_SECTION_NAME";
    private static final String SESSION_KEY_LAST_SELECTED_DEVICE = "SESSION_KEY_LAST_SELECTED_DEVICE";
    private static final String SESSION_KEY_TEMP_CHOSEN_DEVICE = "SESSION_KEY_TEMP_CHOSEN_DEVICE";
    private static final String SESSION_KEY_TEMP_SELECTED_ROOM = "SESSION_KEY_TEMP_SELECTED_ROOM";
    private static final String SESSION_KEY_TEMP_SELECTED_WIFI = "SESSION_KEY_TEMP_SELECTED_WIFI";
    private static final String SESSION_KEY_TEMP_SELECTED_WIFI_PASSWORD = "SESSION_KEY_TEMP_SELECTED_WIFI_PASSWORD";
    private static final String SESSION_KEY_TEMP_SELECTED_CONNECTION = "SESSION_KEY_TEMP_SELECTED_CONNECTION";
    // Wifi gang board five
    private static final String SESSION_KEY_WGB5_SETTINGS_FAN_SPEED = "SESSION_KEY_WGB5_SETTINGS_FAN_SPEED";

    public static User getUser(Context context) {
        User mUser = null;
        String appUser = SessionManager.getStringSetting(context, SESSION_KEY_USER, "");
        if (!AllSettingsManager.isNullOrEmpty(appUser)) {
            mUser = APIResponse.getObjectFromJSONString(appUser, User.class);
        }
        return mUser;
    }

    public static void setUser(Context context, String jsonValue) {
        SessionManager.setStringSetting(context, SESSION_KEY_USER, jsonValue);
    }

    public static int getLastSelectedDevicePosition(Context context) {
        return SessionManager.getIntegerSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE_POSITION, -1);
    }

    public static void setLastSelectedDevicePosition(Context context, int sectionPosition) {
        SessionManager.setIntegerSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE_POSITION, sectionPosition);
    }

    public static String getLastSelectedDeviceSection(Context context) {
        return SessionManager.getStringSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE_SECTION_NAME, "");
    }

    public static void setLastSelectedDeviceSection(Context context, String sectionName) {
        SessionManager.setStringSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE_SECTION_NAME, sectionName);
    }

    public static Product getLastSelectedDevice(Context context) {
        Product mProduct = null;
        String device = SessionManager.getStringSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE, "");
        if (!AllSettingsManager.isNullOrEmpty(device)) {
            mProduct = APIResponse.getObjectFromJSONString(device, Product.class);
        }
        return mProduct;
    }

    public static void setLastSelectedDevice(Context context, String deviceObjectString) {
        SessionManager.setStringSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE, deviceObjectString);
    }

    public static Product getTempChosenDevice(Context context) {
        Product mProduct = null;
        String device = SessionManager.getStringSetting(context, SESSION_KEY_TEMP_CHOSEN_DEVICE, "");
        if (!AllSettingsManager.isNullOrEmpty(device)) {
            mProduct = APIResponse.getObjectFromJSONString(device, Product.class);
        }
        return mProduct;
    }

    public static void setTempChosenDevice(Context context, String deviceObjectString) {
        SessionManager.setStringSetting(context, SESSION_KEY_TEMP_CHOSEN_DEVICE, deviceObjectString);
    }

    public static void setTempSelectedRoom(Context context, String roomName) {
        SessionManager.setStringSetting(context, SESSION_KEY_TEMP_SELECTED_ROOM, roomName);
    }

    public static String getTempSelectedRoom(Context context) {
        return SessionManager.getStringSetting(context, SESSION_KEY_TEMP_SELECTED_ROOM, "");
    }

    public static void setTempSelectedWifi(Context context, String wifiName) {
        SessionManager.setStringSetting(context, SESSION_KEY_TEMP_SELECTED_WIFI, wifiName);
    }

    public static String getTempSelectedWifi(Context context) {
        return SessionManager.getStringSetting(context, SESSION_KEY_TEMP_SELECTED_WIFI, "");
    }

    public static void setTempSelectedWifiPassword(Context context, String wifiPassword) {
        SessionManager.setStringSetting(context, SESSION_KEY_TEMP_SELECTED_WIFI_PASSWORD, wifiPassword);
    }

    public static String getTempSelectedWifiPassword(Context context) {
        return SessionManager.getStringSetting(context, SESSION_KEY_TEMP_SELECTED_WIFI_PASSWORD, "");
    }

    public static void setTempSelectedConnectionType(Context context, String connectionType) {
        SessionManager.setStringSetting(context, SESSION_KEY_TEMP_SELECTED_CONNECTION, connectionType);
    }

    public static String getTempSelectedConnectionType(Context context) {
        return SessionManager.getStringSetting(context, SESSION_KEY_TEMP_SELECTED_CONNECTION, "");
    }

    // Wifi gang board five
    public static void setControlFanSpeed(Context context, String deviceId, boolean isControlFanSpeed) {
        SessionManager.setBooleanSetting(context, (SESSION_KEY_WGB5_SETTINGS_FAN_SPEED + "_" + deviceId), isControlFanSpeed);
    }

    public static boolean isControlFanSpeed(Context context, String deviceId) {
        return SessionManager.getBooleanSetting(context, (SESSION_KEY_WGB5_SETTINGS_FAN_SPEED + "_" + deviceId), false);
    }
}