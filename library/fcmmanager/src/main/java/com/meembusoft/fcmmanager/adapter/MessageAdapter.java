package com.meembusoft.fcmmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.fcmmanager.R;
import com.meembusoft.fcmmanager.payload.Payload;
import com.meembusoft.fcmmanager.util.Message;
import com.meembusoft.fcmmanager.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MessageAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final Map<String, Long> stableIds = new HashMap<>();

    private static final Map<Message, Boolean> selection = new HashMap<>();

    @NonNull
    private final List<Message<Payload>> messages = new ArrayList<>();

    public MessageAdapter(@NonNull List<Message<Payload>> messages) {
        this.messages.addAll(messages);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payload, parent, false);
        return new ViewHolder(view, this::onClick);
    }

    private void onClick(@Nullable Message<Payload> message) {
        if (message == null) {
            return;
        }
        Boolean value = selection.get(message);
        selection.put(message, value != null ? Boolean.valueOf(!value) : Boolean.TRUE);
        notifyItemChanged(messages.indexOf(message));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Message<Payload> message = messages.get(position);
        Boolean selected = selection.get(message);
        viewHolder.onBind(message, selected != null ? selected : Boolean.FALSE);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.onUnbind();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public long getItemId(int position) {
        String key = messages.get(position).id();
        Long value = stableIds.get(key);
        if (value == null) {
            value = (long) stableIds.size();
            stableIds.put(key, value);
        }
        return value;
    }

    public Message<Payload> removeItemAtPosition(int position) {
        Message<Payload> message = messages.remove(position);
        notifyItemRemoved(position);
        return message;
    }

    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    public int add(@NonNull Message<Payload> message) {
        messages.add(0, message);
        Collections.sort(messages);
        int indexOf = messages.indexOf(message);
        notifyItemInserted(indexOf);
        return indexOf;
    }
}