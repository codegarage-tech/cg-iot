package com.meembusoft.iot.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.iot.R;
import com.meembusoft.iot.activity.DeviceDetailActivity;
import com.meembusoft.iot.activity.HomeActivity;
import com.meembusoft.iot.model.Device;
import com.meembusoft.iot.util.AllConstants;
import com.meembusoft.iot.util.AppUtil;

import org.parceler.Parcels;


public class RoomWiseDeviceViewHolder extends BaseViewHolder<Device> {

    private TextView tvDeviceName;
    private ImageView ivDevice;

    public RoomWiseDeviceViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.row_my_room_item);

        ivDevice = (ImageView) itemView.findViewById(R.id.iv_device);
        tvDeviceName = (TextView) itemView.findViewById(R.id.tv_device_name);
    }

    @Override
    public void setData(final Device data) {
        tvDeviceName.setText(data.getProduct().getProduct_name());
        AppUtil.loadImage(getContext(), ivDevice, data.getProduct().getProduct_images().get(0).getUrl(), false, false, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent roomIntent = new Intent(getContext(), DeviceDetailActivity.class);
                roomIntent.putExtra(AllConstants.INTENT_KEY_EXTRA_DEVICE, Parcels.wrap(data));
                ((HomeActivity) getContext()).startActivityForResult(roomIntent, AllConstants.INTENT_KEY_REQUEST_CODE_ROOM_WISE_DEVICE);
            }
        });
    }
}