package com.meembusoft.fcmmanager.payload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.meembusoft.fcmmanager.FcmManager;
import com.meembusoft.fcmmanager.R;
import com.meembusoft.fcmmanager.util.RawDataType;
import com.meembusoft.fcmmanager.util.Truss;
import com.squareup.moshi.Json;

import java.util.Map;

import static com.meembusoft.fcmmanager.util.FcmUtil.INTENT_KEY_MESSAGE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class Raw implements Payload {

    @Json(name = "data")
    private final Map<String, String> data;

    Raw(Map<String, String> data) {
        this.data = data;
    }

    @Nullable
    private transient CharSequence display;

    @Nullable
    @Override
    public Class<? extends Activity> contentActivity() {
        String type = getJsonValue("type");
        if (!TextUtils.isEmpty(type)) {
            RawDataType rawDataType = RawDataType.getDetailType(type);
            if (rawDataType != null) {
                switch (rawDataType) {
                    case ANNOUNCEMENT_DETAIL:
                        Log.d(Raw.class.getSimpleName(), "rawDataType>>found ANNOUNCEMENT_DETAIL");
                        return FcmManager.getInstance().getNotificationContentIntentActivity(RawDataType.ANNOUNCEMENT_DETAIL.getValue());
                    case PRODUCT_DETAIL:
                        Log.d(Raw.class.getSimpleName(), "rawDataType>>found PRODUCT_DETAIL");
                        return FcmManager.getInstance().getNotificationContentIntentActivity(RawDataType.PRODUCT_DETAIL.getValue());
                    case ORDER_DETAIL:
                        Log.d(Raw.class.getSimpleName(), "rawDataType>>found ORDER_DETAIL");
                        return FcmManager.getInstance().getNotificationContentIntentActivity(RawDataType.ORDER_DETAIL.getValue());
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public PendingIntent contentIntent(@NonNull Context context) {
//        Intent intent = intent(context);
//        if (intent != null) {
//            return PendingIntent.getActivity(context, 0, intent, 0);
//        }
        return null;
    }

    @Override
    public int icon() {
        String type = getJsonValue("type");
        if (!TextUtils.isEmpty(type)) {
            RawDataType rawDataType = RawDataType.getDetailType(type);
            if (rawDataType != null) {
                switch (rawDataType) {
                    case ANNOUNCEMENT_DETAIL:
                        Log.d(Raw.class.getSimpleName(), "rawDataType>>found ANNOUNCEMENT_DETAIL");
                        return R.drawable.vector_announcement;
                    case PRODUCT_DETAIL:
                        Log.d(Raw.class.getSimpleName(), "rawDataType>>found PRODUCT_DETAIL");
                        return R.drawable.vector_product;
                    case ORDER_DETAIL:
                        Log.d(Raw.class.getSimpleName(), "rawDataType>>found ORDER_DETAIL");
                        return R.drawable.vector_order;
                }
            }
        }
        return R.drawable.ic_code_24dp;
    }

    @Override
    public int notificationId() {
        return R.id.notification_id_raw;
    }

    @Override
    @NonNull
    public NotificationCompat.Builder configure(@NonNull NotificationCompat.Builder builder) {
        @SuppressLint("RestrictedApi") Context context = builder.mContext;
        builder.setContentTitle(getJsonValue("title"))
                .setContentText(getJsonValue("message"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getJsonValue("message")));

        Intent intent = intent(context);
        if (intent != null) {
            builder.addAction(0, context.getString(R.string.payload_link_open), PendingIntent.getActivity(context, 0, intent, 0));
        }
        return builder;
    }

    @Override
    public synchronized CharSequence display() {
        if (display == null) {
//            display = Messages.moshi().adapter(Map.class).indent("  ").toJson(data);
            display = new Truss()
                    .pushSpan(new StyleSpan(android.graphics.Typeface.BOLD)).append(getJsonValue("title"))
                    .popSpan()
                    .append('\n')
                    .append('\n')
                    .append(getJsonValue("message"))
                    .build();
        }
        return display;
    }

    public Intent intent(Context context) {
        Class clazz = contentActivity();
        if (clazz != null) {
            String mData = getJsonValue("data");
            Log.d("FCMdata", "FCMdata: " + mData);
            Intent intent = new Intent(context, clazz);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(INTENT_KEY_MESSAGE, mData);
            return intent;
        }
        return null;
    }

    public String getJsonValue(String key) {
        return data.get(key);
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                ", display=" + display +
                '}';
    }
}