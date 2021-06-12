package com.meembusoft.fcmmanager;

import android.app.Activity;
import android.content.Context;

import com.meembusoft.fcmmanager.util.FcmUtil;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FcmManager {

    private static FcmManager mFcmManager;
    private static FcmBuilder mFcmBuilder;
    private Context mContext;

    public static FcmManager getInstance() {
        if (mFcmManager == null) {
            try {
                throw new Exception("FCM manager is not initialized, please initialize first!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return mFcmManager;
    }

    public static FcmManager initFcmManager(Context context, FcmBuilder fcmBuilder) {
        // This null check will avoid adding new payloads in future
//        if (mFcmManager == null) {
        mFcmManager = new FcmManager(context, fcmBuilder);
//        }
        return mFcmManager;
    }

    private FcmManager(Context context, FcmBuilder fcmBuilder) {
        mContext = context.getApplicationContext();
        mFcmBuilder = fcmBuilder;
    }

    public Class<? extends Activity> getNotificationContentIntentActivity(@NotNull String payloadKey) {
        return mFcmBuilder.getNotificationContentIntentActivity(payloadKey);
    }

    public boolean isAppNotificationStopped() {
        return FcmUtil.isAppNotificationStopped(mContext);
    }

    public void stopAppNotification(boolean isStopAppNotification) {
        FcmUtil.stopAppNotification(mContext, isStopAppNotification);
    }

    public static class FcmBuilder {
        private HashMap<String, Class<? extends Activity>> contentIntentClasses = new HashMap<>();

        public FcmBuilder() {
        }

        public FcmBuilder addNotificationContentInfo(@NotNull String payloadKey, @NotNull Class<? extends Activity> contentIntentActivity) {
            contentIntentClasses.put(payloadKey, contentIntentActivity);
            return this;
        }

        public Class<? extends Activity> getNotificationContentIntentActivity(@NotNull String payloadKey) {
            if (!contentIntentClasses.containsKey(payloadKey)) {
                try {
                    throw new Exception("FCM manager is not initialized properly, please add addNotificationContentInfo first!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return contentIntentClasses.get(payloadKey);
        }

        public FcmBuilder buildGcmManager() {
            return this;
        }
    }
}