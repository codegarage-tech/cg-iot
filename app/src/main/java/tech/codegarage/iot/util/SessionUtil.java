package tech.codegarage.iot.util;

import android.content.Context;

import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import tech.codegarage.iot.model.Device;
import tech.codegarage.iot.model.User;
import tech.codegarage.iot.retrofit.APIResponse;

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

    public static User getUser(Context context) {
        User mUser = null;
        String appUser = SessionManager.getStringSetting(context, SESSION_KEY_USER, "");
        if (!AllSettingsManager.isNullOrEmpty(appUser)) {
            mUser = APIResponse.getResponseObject(appUser, User.class);
        }
        return mUser;
    }

    public static void setUser(Context context, String jsonValue) {
        SessionManager.setStringSetting(context, SESSION_KEY_USER, jsonValue);
    }

    public static int getLastSelectedDevicePosition(Context context){
        return SessionManager.getIntegerSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE_POSITION, -1);
    }

    public static void setLastSelectedDevicePosition(Context context, int sectionPosition){
        SessionManager.setIntegerSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE_POSITION, sectionPosition);
    }

    public static String getLastSelectedDeviceSection(Context context){
        return SessionManager.getStringSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE_SECTION_NAME, "");
    }

    public static void setLastSelectedDeviceSection(Context context, String sectionName){
        SessionManager.setStringSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE_SECTION_NAME, sectionName);
    }

    public static Device getLastSelectedDevice(Context context){
        Device mDevice = null;
        String device = SessionManager.getStringSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE, "");
        if (!AllSettingsManager.isNullOrEmpty(device)) {
            mDevice = APIResponse.getResponseObject(device, Device.class);
        }
        return mDevice;
    }

    public static void setLastSelectedDevice(Context context, String deviceObjectString){
        SessionManager.setStringSetting(context, SESSION_KEY_LAST_SELECTED_DEVICE, deviceObjectString);
    }

    public static Device getTempChosenDevice(Context context){
        Device mDevice = null;
        String device = SessionManager.getStringSetting(context, SESSION_KEY_TEMP_CHOSEN_DEVICE, "");
        if (!AllSettingsManager.isNullOrEmpty(device)) {
            mDevice = APIResponse.getResponseObject(device, Device.class);
        }
        return mDevice;
    }

    public static void setTempChosenDevice(Context context, String deviceObjectString){
        SessionManager.setStringSetting(context, SESSION_KEY_TEMP_CHOSEN_DEVICE, deviceObjectString);
    }

    public static void setTempSelectedRoom(Context context, String roomName){
        SessionManager.setStringSetting(context, SESSION_KEY_TEMP_SELECTED_ROOM, roomName);
    }

    public static String getTempSelectedRoom(Context context){
        return SessionManager.getStringSetting(context, SESSION_KEY_TEMP_SELECTED_ROOM, "");
    }
}