//package com.meembusoft.iot.viewholder;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.jude.easyrecyclerview.adapter.BaseViewHolder;
//
//import com.meembusoft.iot.R;
//import com.meembusoft.iot.model.Room;
//
//
//public class RoomWiseViewHolder extends BaseViewHolder<Room> {
//
//
//    TextView tviItemName;
//    LinearLayout contentView;
//    Context mContext;
//
//    public RoomWiseViewHolder(ViewGroup parent, Context context) {
//        super(parent, R.layout.row_select_room_item);
//        mContext = context;
//
//        tviItemName = (TextView) itemView.findViewById(R.id.tv_title);
//        //  contentView = (LinearLayout) itemView.findViewById( R.id.content_view);
//
//    }
//
//    @Override
//    public void setData(final Room data) {
//        tviItemName.setText(data.getName());
//        if (data.isSelected()) {
//            tviItemName.setBackgroundResource(R.drawable.selected_room_item);
//            tviItemName.setTextColor(Color.parseColor("#FFFFFF"));
//        } else {
//            tviItemName.setBackgroundResource(R.drawable.unselect_room_item);
//            tviItemName.setTextColor(Color.parseColor("#3F51B5"));
//        }
//
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }
//}
