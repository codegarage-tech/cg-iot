package com.meembusoft.fcmmanager.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
@Keep
final class Token {

    private static final String INTENT_ACTION = Token.class.getName() + "Update";

    private static final Intent INTENT = new Intent(INTENT_ACTION);

    private static final IntentFilter INTENT_FILTER = new IntentFilter(INTENT_ACTION);

    private static final String EXTRA_KEY = "token";

    public interface Handler {
        void handle(String token);
    }

    private static LocalBroadcastManager manager(@NonNull Context context) {
        return LocalBroadcastManager.getInstance(context);
    }

    public static void broadcast(@NonNull Context context, @NonNull String token) {
        Intent intent = new Intent(INTENT);
        intent.putExtra(EXTRA_KEY, token);
        manager(context).sendBroadcast(intent);
    }

    @NonNull
    public static BroadcastReceiver create(final Handler handler) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handler.handle(intent.getStringExtra(EXTRA_KEY));
            }
        };
    }

    public static void register(@NonNull Context context, @NonNull BroadcastReceiver receiver) {
        manager(context).registerReceiver(receiver, INTENT_FILTER);
    }

    public static void unregister(@NonNull Context context, @NonNull BroadcastReceiver receiver) {
        manager(context).unregisterReceiver(receiver);
    }
}