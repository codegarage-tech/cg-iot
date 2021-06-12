package com.meembusoft.fcmmanager.viewholder;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.fcmmanager.R;
import com.meembusoft.fcmmanager.payload.App;
import com.meembusoft.fcmmanager.payload.Link;
import com.meembusoft.fcmmanager.payload.Payload;
import com.meembusoft.fcmmanager.payload.Raw;
import com.meembusoft.fcmmanager.payload.Text;
import com.meembusoft.fcmmanager.util.FcmUtil;
import com.meembusoft.fcmmanager.util.Message;
import com.meembusoft.fcmmanager.views.TimeAgoTextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public final class ViewHolder extends RecyclerView.ViewHolder {

    public interface OnClickListener {
        void onClick(@Nullable Message<Payload> message);
    }

    enum Action {
        PRIMARY,
        SECONDARY,
        TERTIARY
    }

    @Nullable
    private Message<Payload> message;

    private boolean selected = false;

    private final ImageView icon;

    private final TimeAgoTextView timestamp;

    private final TextView raw, text;

    private final Button button1, button2, button3;

    private final View selector;

    public ViewHolder(@NonNull View itemView, @NonNull final OnClickListener listener) {
        super(itemView);
        selector = itemView.findViewById(R.id.item_selector);
        icon = itemView.findViewById(R.id.item_icon);
        timestamp = itemView.findViewById(R.id.item_timestamp);
        raw = itemView.findViewById(R.id.item_raw);
        text = itemView.findViewById(R.id.item_text);
        button1 = itemView.findViewById(R.id.item_btn_1);
        button2 = itemView.findViewById(R.id.item_btn_2);
        button3 = itemView.findViewById(R.id.item_btn_3);
        button1.setOnClickListener(v -> execute(Action.PRIMARY, payload()));
        button2.setOnClickListener(v -> execute(Action.SECONDARY, payload()));
        button3.setOnClickListener(v -> execute(Action.TERTIARY, payload()));
        itemView.setOnClickListener(v -> listener.onClick(message));
        itemView.setOnLongClickListener(v -> {
            listener.onClick(message);
            return true;
        });
    }

    @Nullable
    private Payload payload() {
        return message != null ? message.payload() : null;
    }

    private void renderContent() {
        selector.setActivated(selected);
        if (selected) {
            text.setText(null);
            text.setVisibility(GONE);
//            Map data = message != null ? message.data() : null;
//            String display = data != null ? Messages.moshi().adapter(Message.class).indent("  ").toJson(message) : null;
            Payload payload = payload();
            CharSequence display = payload != null ? payload.display() : null;
            raw.setText(display);
            raw.setVisibility(TextUtils.isEmpty(display) ? GONE : VISIBLE);
        } else {
            Payload payload = payload();
            CharSequence display = payload != null ? payload.display() : null;
            text.setText(display);
            text.setVisibility(TextUtils.isEmpty(display) ? GONE : VISIBLE);
            raw.setText(null);
            raw.setVisibility(GONE);
        }
    }

    private void render(@NonNull Action action, @NonNull Button button, @Nullable Payload payload) {
        if (payload instanceof App) {
            switch (action) {
                case PRIMARY:
                    button.setVisibility(VISIBLE);
                    button.setText(R.string.payload_app_store);
                    return;
                case SECONDARY:
                    button.setText(R.string.payload_app_uninstall);
                    button.setVisibility(((App) payload).isInstalled(itemView.getContext()) ? VISIBLE : GONE);
                    return;
            }
        } else if (payload instanceof Link) {
            if (action == Action.PRIMARY) {
                button.setVisibility(VISIBLE);
                button.setText(R.string.payload_link_open);
                return;
            }
        } else if (payload instanceof Text) {
            if (action == Action.PRIMARY) {
                button.setVisibility(VISIBLE);
                button.setText(R.string.payload_text_copy);
                return;
            }
        } else if (payload instanceof Raw) {
            if (action == Action.PRIMARY) {
                Intent intent = ((Raw) payload).intent(itemView.getContext());
                if (intent != null) {
                    button.setVisibility(VISIBLE);
                    button.setText(R.string.payload_link_open);
                }
                return;
            }
        }

        button.setVisibility(GONE);
        button.setText(null);
    }

    private void execute(@NonNull Action action, @Nullable Payload payload) {
        Context context = itemView.getContext();
        if (payload instanceof App) {
            App app = (App) payload;
            switch (action) {
                case PRIMARY:
                    FcmUtil.safeStartActivity(context, app.playStore());
                    break;
                case SECONDARY:
                    FcmUtil.safeStartActivity(context, app.uninstall());
                    break;
            }
        } else if (payload instanceof Link) {
            if (action == Action.PRIMARY) {
                FcmUtil.safeStartActivity(context, ((Link) payload).intent());
            }
        } else if (payload instanceof Text) {
            if (action == Action.PRIMARY) {
                FcmUtil.copyToClipboard(context, ((Text) payload).text);
            }
        } else if (payload instanceof Raw) {
            if (action == Action.PRIMARY) {
                Intent intent = ((Raw) payload).intent(context);
                if (intent != null) {
                    context.startActivity(intent);
                }
            }
        }
    }

    public void onBind(@NonNull Message<Payload> message, boolean selected) {
        this.message = message;
        this.selected = selected;
        icon.setImageResource(message.payload().icon());
        timestamp.setTimestamp(Math.min(message.sentTime(), System.currentTimeMillis()));
        renderContent();
        renderButtons();
    }

    private void renderButtons() {
        Payload payload = message != null ? message.payload() : null;
        render(Action.PRIMARY, button1, payload);
        render(Action.SECONDARY, button2, payload);
        render(Action.TERTIARY, button3, payload);
    }

    public void onUnbind() {
        this.message = null;
        timestamp.setTimestamp(TimeAgoTextView.NO_TIMESTAMP);
    }
}