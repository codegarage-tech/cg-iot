package com.meembusoft.fcmmanager.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.meembusoft.fcmmanager.R;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FcmUtil {

    private static final String TAG = FcmUtil.class.getSimpleName();
    private static final String SESSION_KEY_TOKEN = "SESSION_KEY_TOKEN";
    private static final String SESSION_KEY_STOP_APP_NOTIFICATION = "SESSION_KEY_STOP_APP_NOTIFICATION";
    public static final String INTENT_KEY_MESSAGE = "INTENT_KEY_MESSAGE";

    public static String printStackTrace(@NonNull Throwable exception) {
        StringWriter trace = new StringWriter();
        exception.printStackTrace(new PrintWriter(trace));
        return trace.toString();
    }

    public static void copyToClipboard(@NonNull Context context, @Nullable CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
        }
    }

    public static void safeStartActivity(@NonNull Context context, @Nullable Intent intent) {
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, printStackTrace(e), Toast.LENGTH_LONG).show();
        }
    }

    public static void saveToken(Context context, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SESSION_KEY_TOKEN, value);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(SESSION_KEY_TOKEN, "");
    }

    public static boolean isFcmRegistered(Context context) {
        return (!isNullOrEmpty(getToken(context)));
    }

    public static void stopAppNotification(Context context, boolean isStopAppNotification) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SESSION_KEY_STOP_APP_NOTIFICATION, isStopAppNotification);
        editor.commit();
    }

    public static boolean isAppNotificationStopped(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(SESSION_KEY_STOP_APP_NOTIFICATION, false);
    }

    public static boolean isNullOrEmpty(String myString) {
        if (myString == null) {
            return true;
        }
        if (myString.length() == 0 || myString.equalsIgnoreCase("null")
                || myString.equalsIgnoreCase("")) {
            return true;
        }
        return false;
    }

    public static String getAnyKeyValueFromJson(String jsonResponse, String key) {
        String value = "";
        try {
            //Create json object from string
            JSONObject newJson = new JSONObject(jsonResponse);

            // Get keys from json
            Iterator<String> panelKeys = newJson.keys();

            while (panelKeys.hasNext()) {
                // get key from list
                JSONObject panel = newJson.getJSONObject(panelKeys.next());
                value = panel.getString(key);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            value = "";
        }

        return value;
    }

    public static <T> T getObjectFromJSONString(String jsonString, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);
    }

    public static <T> String getJSONStringFromObject(T object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static void applyMarqueeOnTextView(TextView textView) {
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setMarqueeRepeatLimit(-1);
        textView.setSelected(true);
    }
}