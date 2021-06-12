package com.meembusoft.fcmmanager.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.meembusoft.fcmmanager.R;
import com.meembusoft.fcmmanager.payload.Payload;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public final class Notifications {

    private Notifications() {
    }

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context) {
        String id = context.getString(R.string.notification_channel_id);
        CharSequence name = context.getString(R.string.notification_channel_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setShowBadge(true);
        getNotificationManager(context).createNotificationChannel(channel);
    }

    @NonNull
    private static NotificationCompat.Builder getNotificationBuilder(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context);
        }
        return new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setLocalOnly(true)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
    }

    public static void show(@NonNull Context context, @NonNull Message<Payload> message) {
        Payload payload = message.payload();
        NotificationCompat.Builder notificationBuilder = Notifications.getNotificationBuilder(context).setSmallIcon(payload.icon());
        PendingIntent pendingIntent = payload.contentIntent(context);
        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent);
        }
        Notification notification = payload.configure(notificationBuilder).build();
        getNotificationManager(context).notify(message.id(), payload.notificationId(), notification);
    }

    public static void removeAll(Context context) {
        getNotificationManager(context).cancelAll();
    }
}