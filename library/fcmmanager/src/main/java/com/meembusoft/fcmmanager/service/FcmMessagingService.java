package com.meembusoft.fcmmanager.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.UiThread;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.meembusoft.fcmmanager.FcmManager;
import com.meembusoft.fcmmanager.payload.App;
import com.meembusoft.fcmmanager.payload.Link;
import com.meembusoft.fcmmanager.payload.Payload;
import com.meembusoft.fcmmanager.payload.Text;
import com.meembusoft.fcmmanager.util.FcmUtil;
import com.meembusoft.fcmmanager.util.Message;
import com.meembusoft.fcmmanager.util.Messages;
import com.meembusoft.fcmmanager.util.Notifications;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FcmMessagingService extends FirebaseMessagingService {

    private static final String TAG = FcmMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        if (!FcmUtil.isNullOrEmpty(token)) {
            Log.d(TAG, TAG + " >> " + "onNewToken: " + token);
            FcmUtil.saveToken(getApplicationContext(), token);
        }
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, TAG + ">>remoteMessage: " + remoteMessage.toString());
        Log.d(TAG, TAG + ">>remoteMessage(data): " + remoteMessage.getData().toString());

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Message<Payload> message = Message.from(remoteMessage);
                Log.d(TAG, TAG + ">>remoteMessage(message): " + message.toString());
                boolean silent = Boolean.valueOf(remoteMessage.getData().get("hide"));
                notifyAndExecute(message, silent, FcmMessagingService.this);
            }
        });

        //Check fcm is registered or not.
        // Device is only registered when the user is at logged in state
//        if (FcmUtil.getIsFcmRegistered(this) == 1) {
//            Log.d(TAG, TAG + ">> " + "FCM is registered");
//            Log.d(TAG, TAG + ">> " + remoteMessage.getData().toString());
//            Payload payload = Payload.with(remoteMessage);
//
//            //Save notification into preference
//            if (!payload.key().equalsIgnoreCase(OrderPayload.KEY)) {
//                Log.d(TAG, TAG + ">> " + "Saving app notification");
//                payload.saveToSharedPreferences(this);
//            } else {
//                Log.d(TAG, TAG + " >> " + "Avoid saving order notification");
//            }
//
//            //Show app notification
//            if (!payload.key().equalsIgnoreCase(OrderPayload.KEY) && (FcmUtil.getIsAppNotification(this) == 1) && payload.shouldShowNotification()) {
//                payload.showNotification(this);
//            } else {
//                Log.d(TAG, TAG + " >> " + "Avoid showing app notification");
//            }
//
//            //Show order notification
//            if (payload.key().equalsIgnoreCase(OrderPayload.KEY) && (FcmUtil.getIsOrderNotification(this) == 1) && payload.shouldShowNotification()) {
//                Log.d(TAG, TAG + " >> " + "Showing order notification");
//                payload.showNotification(this);
//            } else {
//                Log.d(TAG, TAG + " >> " + "Avoid showing order notification");
//            }
//            payload.execute(this);
//        } else {
//            Log.d(TAG, TAG + ">> " + "FCM is not registered");
//        }
    }

    @UiThread
    private void notifyAndExecute(Message<Payload> message, boolean silent, Context context) {
        if (!silent) {
            if (!FcmManager.getInstance().isAppNotificationStopped()) {
                Notifications.show(context, message);
            }
        }
        Messages.instance(context).add(message);
        Payload payload = message.payload();
        if (payload instanceof Link) {
            Link link = (Link) payload;
            if (link.open()) {
                startActivity(link.intent());
            }
        } else if (payload instanceof Text) {
            Text text = (Text) payload;
            if (text.clipboard()) {
                text.copyToClipboard(this);
            }
        } else if (payload instanceof App) {
            App app = (App) payload;
            if (app.open()) {
                startActivity(app.playStore());
            }
        }
    }
}