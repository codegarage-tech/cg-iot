package com.meembusoft.fcmmanager.payload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.meembusoft.fcmmanager.FcmManager;
import com.meembusoft.fcmmanager.R;
import com.meembusoft.fcmmanager.util.Truss;
import com.squareup.moshi.Json;

import static com.meembusoft.fcmmanager.util.FcmUtil.INTENT_KEY_MESSAGE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class App implements Payload {

    @Json(name = "title")
    private final String title;
    @Json(name = "message")
    public final String text;
    @Json(name = "package")
    private final String packageName;
    @Json(name = "open")
    private final boolean open;

    @Nullable
    private transient CharSequence display;

    public App(String title, String text, String packageName, boolean open) {
        this.title = title;
        this.text = text;
        this.packageName = packageName;
        this.open = open;
    }

    @Nullable
    @Override
    public Class<? extends Activity> contentActivity() {
        return (FcmManager.getInstance() == null ? null : FcmManager.getInstance().getNotificationContentIntentActivity(App.class.getSimpleName()));
    }

    @Nullable
    @Override
    public PendingIntent contentIntent(@NonNull Context context) {
        return null;
    }

    @Override
    public int icon() {
        return R.drawable.ic_shop_24dp;
    }

    @Override
    public int notificationId() {
        return R.id.notification_id_app;
    }

    @Override
    @NonNull
    public NotificationCompat.Builder configure(@NonNull NotificationCompat.Builder builder) {
        @SuppressLint("RestrictedApi") Context context = builder.mContext;
        builder.setContentTitle(TextUtils.isEmpty(title) ? context.getString(R.string.payload_app) : title)
                .setContentText(text)
//                .setContentIntent(PendingIntent.getActivity(context, 0, playStore(), 0))
                .addAction(0, context.getString(R.string.payload_app_store), PendingIntent.getActivity(context, 0, playStore(), 0));
        if (isInstalled(context)) {
            builder.addAction(0, context.getString(R.string.payload_app_uninstall), PendingIntent.getActivity(context, 0, uninstall(), 0));
        }
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        return builder;
    }

    @Override
    public synchronized CharSequence display() {
        if (display == null) {
            display = new Truss()
                    .pushSpan(new StyleSpan(android.graphics.Typeface.BOLD)).append(title)
                    .popSpan()
                    .append('\n')
                    .append('\n')
                    .append(text)
//                    .pushSpan(new StyleSpan(android.graphics.Typeface.BOLD)).append("package: ").popSpan().append(packageName).append('\n')
//                    .pushSpan(new StyleSpan(android.graphics.Typeface.BOLD)).append("open: ").popSpan().append(String.valueOf(open))
                    .build();
        }
        return display;
    }

    public Intent playStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public Intent uninstall() {
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public boolean isInstalled(@NonNull Context context) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public boolean open() {
        return open;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", message='" + text + '\'' +
                ", packageName='" + packageName + '\'' +
                ", open=" + open + '\'' +
                ", display=" + display +
                '}';
    }
}