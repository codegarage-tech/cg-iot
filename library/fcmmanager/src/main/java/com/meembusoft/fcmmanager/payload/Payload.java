package com.meembusoft.fcmmanager.payload;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public interface Payload {

    @Nullable
    Class<? extends Activity> contentActivity();

    @Nullable
    PendingIntent contentIntent(@NonNull Context context);

    @DrawableRes
    int icon();

    @Nullable
    CharSequence display();

    @IdRes
    int notificationId();

    @NonNull
    NotificationCompat.Builder configure(@NonNull NotificationCompat.Builder builder);
}