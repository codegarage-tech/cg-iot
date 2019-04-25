package tech.codegarage.iot.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tech.codegarage.iot.R;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {

    public final View rootView;
    public final TextView tvItemName;
//    public final TextView tvSubItemName;
    public final ImageView ivItemImage;
    public final LinearLayout llItemTick;

    public ItemViewHolder(View view) {
        super(view);

        rootView = view;
        tvItemName = (TextView) view.findViewById(R.id.tv_item_name);
//        tvSubItemName = (TextView) view.findViewById(R.id.tv_subitem_name);
        ivItemImage = (ImageView) view.findViewById(R.id.iv_item_image);
        llItemTick = (LinearLayout) view.findViewById(R.id.ll_item_tick);
    }
}