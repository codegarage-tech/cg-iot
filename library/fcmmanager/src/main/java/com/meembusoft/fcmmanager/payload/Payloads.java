package com.meembusoft.fcmmanager.payload;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;
import com.meembusoft.fcmmanager.util.Messages;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public final class Payloads {

    public static final PolymorphicJsonAdapterFactory JSON_ADAPTER = PolymorphicJsonAdapterFactory.of(Payload.class, "type")
            .withSubtype(App.class, App.class.getSimpleName().toLowerCase())
            .withSubtype(Link.class, Link.class.getSimpleName().toLowerCase())
            .withSubtype(Ping.class, Ping.class.getSimpleName().toLowerCase())
            .withSubtype(Raw.class, Raw.class.getSimpleName().toLowerCase())
            .withSubtype(Text.class, Text.class.getSimpleName().toLowerCase());

    private static final Map<String, Class<? extends Payload>> CLASSES = new HashMap<String, Class<? extends Payload>>() {{
        put(App.class.getSimpleName().toLowerCase(), App.class);
        put(Link.class.getSimpleName().toLowerCase(), Link.class);
        put(Ping.class.getSimpleName().toLowerCase(), Ping.class);
        put(Raw.class.getSimpleName().toLowerCase(), Raw.class);
        put(Text.class.getSimpleName().toLowerCase(), Text.class);
    }};

    private Payloads() {
    }

    @NonNull
    public static Payload extract(@NonNull RemoteMessage message) {
        Map<String, String> data = message.getData();
        Set<Map.Entry<String, String>> entries = data.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            Class<? extends Payload> clazz = CLASSES.get(key);
            if (clazz != null) {
                try {
                    JsonAdapter<? extends Payload> adapter = Messages.moshi().adapter(clazz);
                    Payload payload = adapter.fromJson(value);
                    if (payload != null) {
                        return payload;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Raw(data);
    }
}