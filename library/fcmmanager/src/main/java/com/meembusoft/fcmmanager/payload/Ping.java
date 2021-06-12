package com.meembusoft.fcmmanager.payload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.meembusoft.fcmmanager.FcmManager;
import com.meembusoft.fcmmanager.R;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class Ping implements Payload {

    @Nullable
    @Override
    public Class<? extends Activity> contentActivity() {
//        return (FcmManager.getInstance() == null ? null : FcmManager.getInstance().getNotificationContentIntentActivity(Ping.class.getSimpleName()));
        return null;
    }

    @Nullable
    @Override
    public PendingIntent contentIntent(@NonNull Context context) {
        return null;
    }

    @Override
    public int icon() {
        return R.drawable.ic_notifications_none_24dp;
    }

    @Override
    public int notificationId() {
        return R.id.notification_id_ping;
    }

    @Override
    @NonNull
    public NotificationCompat.Builder configure(@NonNull NotificationCompat.Builder builder) {
        @SuppressLint("RestrictedApi") Context context = builder.mContext;
        return builder.setContentTitle(context.getString(R.string.payload_ping));
    }

    @Nullable
    @Override
    public CharSequence display() {
        return null;
    }
}