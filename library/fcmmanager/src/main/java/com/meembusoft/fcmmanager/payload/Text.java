package com.meembusoft.fcmmanager.payload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.meembusoft.fcmmanager.FcmManager;
import com.meembusoft.fcmmanager.R;
import com.meembusoft.fcmmanager.activity.CopyToClipboardActivity;
import com.meembusoft.fcmmanager.util.FcmUtil;
import com.meembusoft.fcmmanager.util.Truss;
import com.squareup.moshi.Json;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class Text implements Payload {

    @Json(name = "title")
    private final String title;
    @Json(name = "message")
    public final String text;
    @Json(name = "code")
    public final String code;
    @Json(name = "clipboard")
    private final boolean clipboard;

    @Nullable
    private transient CharSequence display;

    public Text(String title, String text, String code, boolean clipboard) {
        this.title = title;
        this.text = text;
        this.code = code;
        this.clipboard = clipboard;
    }

    @Nullable
    @Override
    public Class<? extends Activity> contentActivity() {
//        return (FcmManager.getInstance() == null ? null : FcmManager.getInstance().getNotificationContentIntentActivity(Text.class.getSimpleName()));
        return null;
    }

    @Nullable
    @Override
    public PendingIntent contentIntent(@NonNull Context context) {
        return null;
    }

    @Override
    public int icon() {
        return R.drawable.ic_chat_24dp;
    }

    @Override
    public int notificationId() {
        return R.id.notification_id_text;
    }

    @Override
    @NonNull
    public NotificationCompat.Builder configure(@NonNull NotificationCompat.Builder builder) {
        @SuppressLint("RestrictedApi") Context context = builder.mContext;
        final Intent intent = new Intent(context, CopyToClipboardActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, code);
        return builder.setContentTitle(TextUtils.isEmpty(title) ? context.getString(R.string.payload_text) : title)
                .setContentText(text)
//                .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
                .addAction(0, context.getString(R.string.payload_text_copy), PendingIntent.getActivity(context, 0, intent, 0))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));
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
//                    .pushSpan(new StyleSpan(android.graphics.Typeface.BOLD)).append("code: ").popSpan().append(code).append('\n')
//                    .pushSpan(new StyleSpan(android.graphics.Typeface.BOLD)).append("clipboard: ").popSpan().append(String.valueOf(clipboard))
                    .build();
        }
        return display;
    }

    public boolean clipboard() {
        return clipboard;
    }

    public void copyToClipboard(@NonNull final Context context) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    copyToClipboard(context);
                }
            });
        }
        FcmUtil.copyToClipboard(context.getApplicationContext(), code);
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", message='" + text + '\'' +
                ", code='" + code + '\'' +
                ", clipboard=" + clipboard + '\'' +
                ", display=" + display +
                '}';
    }
}