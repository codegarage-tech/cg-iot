package com.meembusoft.iot.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.iot.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    public final TextView tvTitle;

    public HeaderViewHolder(View view) {
        super(view);

        tvTitle = (TextView) view.findViewById(R.id.tv_header_title);
    }
}