package com.meembusoft.fcmmanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.util.UUID;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
final class Uuid {

    private static final String KEY = "uuid";

    /**
     * @return UUID.randomUUID() or a previously generated UUID, stored in SharedPreferences
     */
    static String get(@NonNull Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (prefs.contains(KEY)) {
            final String value = prefs.getString(KEY, null);
            if (!TextUtils.isEmpty(value)) {
                return value;
            }
        }
        final String uuid = UUID.randomUUID().toString();
        prefs.edit().putString(KEY, uuid).apply();
        return uuid;
    }
}