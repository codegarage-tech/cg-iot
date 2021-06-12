package com.meembusoft.fcmmanager.payload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.meembusoft.fcmmanager.FcmManager;
import com.meembusoft.fcmmanager.R;
import com.meembusoft.fcmmanager.util.Truss;
import com.squareup.moshi.Json;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class Link implements Payload {

    @Json(name = "title")
    private final String title;
    @Json(name = "message")
    public final String text;
    @Json(name = "url")
    private final String url;
    @Json(name = "open")
    private final boolean open;

    @Nullable
    private transient CharSequence display;

    public Link(String title, String text, String url, boolean open) {
        this.title = title;
        this.text = text;
        this.url = url;
        this.open = open;
    }

    @Nullable
    @Override
    public Class<? extends Activity> contentActivity() {
//        return (FcmManager.getInstance() == null ? null : FcmManager.getInstance().getNotificationContentIntentActivity(Link.class.getSimpleName()));
        return null;
    }

    @Nullable
    @Override
    public PendingIntent contentIntent(@NonNull Context context) {
        return null;
    }

    @Override
    public int icon() {
        return R.drawable.ic_link_24dp;
    }

    @Override
    public int notificationId() {
        return R.id.notification_id_link;
    }

    @Override
    @NonNull
    public NotificationCompat.Builder configure(@NonNull NotificationCompat.Builder builder) {
        @SuppressLint("RestrictedApi") Context context = builder.mContext;
        builder.setContentTitle(TextUtils.isEmpty(title) ? context.getString(R.string.payload_link) : title)
                .setContentText(text)
//                .setContentIntent(PendingIntent.getActivity(context, 0, intent(), 0))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        if (!TextUtils.isEmpty(url)) {
            builder.addAction(0, context.getString(R.string.payload_link_open), PendingIntent.getActivity(context, 0, intent(), 0));
        }
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
//                    .pushSpan(new StyleSpan(android.graphics.Typeface.BOLD)).append("message: ").popSpan().append(text).append('\n')
//                    .pushSpan(new StyleSpan(android.graphics.Typeface.BOLD)).append("url: ").popSpan().append(url).append('\n')
//                    .pushSpan(new StyleSpan(android.graphics.Typeface.BOLD)).append("open: ").popSpan().append(String.valueOf(open))
                    .build();
        }
        return display;
    }

    public boolean open() {
        return open;
    }

    public Intent intent() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", message='" + text + '\'' +
                ", url='" + url + '\'' +
                ", open=" + open + '\'' +
                ", display=" + display +
                '}';
    }
}
