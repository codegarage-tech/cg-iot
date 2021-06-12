package com.meembusoft.iot.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.iot.R;
import com.meembusoft.iot.activity.DeviceDetailActivity;
import com.meembusoft.iot.activity.HomeActivity;
import com.meembusoft.iot.model.Device;
import com.meembusoft.iot.util.AllConstants;
import com.meembusoft.iot.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;

import org.parceler.Parcels;

public class FavoriteViewHolder extends BaseViewHolder<Device> {

    private TextView tvItemName;
    private ImageView ivItem;
    private LinearLayout contentView;
    private Context mContext;

    public FavoriteViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.row_favorite_item2);
        mContext = context;

        ivItem = (ImageView) itemView.findViewById(R.id.iv_item_image);
        tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
        contentView = (LinearLayout) itemView.findViewById(R.id.content_view);
    }

    @Override
    public void setData(final Device data) {

        tvItemName.setText(data.getProduct().getProduct_name());
        AppUtil.loadImage(mContext, ivItem, data.getProduct().getProduct_images().get(0).getUrl(), false, true, false);

        contentView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent deviceIntent = new Intent(getContext(), DeviceDetailActivity.class);
                deviceIntent.putExtra(AllConstants.INTENT_KEY_EXTRA_DEVICE, Parcels.wrap(data));
                ((HomeActivity) getContext()).startActivityForResult(deviceIntent, AllConstants.INTENT_KEY_REQUEST_CODE_FAVORITE_DEVICE);
            }
        });
    }
}
