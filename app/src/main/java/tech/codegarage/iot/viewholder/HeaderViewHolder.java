package tech.codegarage.iot.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import tech.codegarage.iot.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    public final TextView tvTitle;

    public HeaderViewHolder(View view) {
        super(view);

        tvTitle = (TextView) view.findViewById(R.id.tv_header_title);
    }
}