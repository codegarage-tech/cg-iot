package com.meembusoft.iot.viewholder;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import com.meembusoft.iot.R;
import com.meembusoft.iot.model.Room;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RoomViewHolder extends BaseViewHolder<Room> {

    private TextView tviItemName;

    public RoomViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_select_room_item);

        tviItemName = (TextView) itemView.findViewById(R.id.tv_title);
    }

    @Override
    public void setData(final Room data) {
        tviItemName.setText(data.getRoom_name());
        if (data.isSelected()) {
            tviItemName.setBackgroundResource(R.drawable.selected_room_item);
            tviItemName.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            tviItemName.setBackgroundResource(R.drawable.unselect_room_item);
            tviItemName.setTextColor(Color.parseColor("#3F51B5"));
        }
    }
}