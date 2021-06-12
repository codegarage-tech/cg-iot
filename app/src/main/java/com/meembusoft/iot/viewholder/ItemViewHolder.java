package com.meembusoft.iot.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.iot.R;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {

    public final View rootView;
    public final TextView tvItemName;
    public final TextView tvItemPrice;
    public final ImageView ivItemImage;
    public final LinearLayout llItemTick;
    public final LinearLayout llContainer, llShadow;
    public final RelativeLayout rlShadowLeft, rlShadowRight;

    public ItemViewHolder(View view) {
        super(view);

        rootView = view;
        tvItemName = (TextView) view.findViewById(R.id.tv_item_name);
        tvItemPrice = (TextView) view.findViewById(R.id.tv_item_price);
        ivItemImage = (ImageView) view.findViewById(R.id.iv_item_image);
        llItemTick = (LinearLayout) view.findViewById(R.id.ll_item_tick);
        llContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        llShadow = (LinearLayout) view.findViewById(R.id.ll_shadow);
        rlShadowLeft = (RelativeLayout) view.findViewById(R.id.rl_shadow_left);
        rlShadowRight = (RelativeLayout) view.findViewById(R.id.rl_shadow_right);
    }
}