//package tech.codegarage.iot.viewholder;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.jude.easyrecyclerview.adapter.BaseViewHolder;
//
//import java.util.List;
//
//import tech.codegarage.iot.R;
//import tech.codegarage.iot.adapter.RoomAdapter;
//import tech.codegarage.iot.model.Room;
//
///**
// * @author Md. Rashadul Alam
// * Email: rashed.droid@gmail.com
// */
//public class RoomViewHolder extends BaseViewHolder<Room> {
//
//    private String TAG = "RoomViewHolder";
//    private TextView tvTitle;
//    private LinearLayout llSelected;
//
//    public RoomViewHolder(ViewGroup parent) {
//        super(parent, R.layout.row_room_item);
//
//        tvTitle = $(R.id.tv_title);
//        llSelected = $(R.id.ll_selected);
//    }
//
//    @Override
//    public void setData(final Room data) {
//        tvTitle.setText(data.getName());
//        llSelected.setVisibility(data.isSelected() ? View.VISIBLE : View.GONE);
//
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSelection(data);
//            }
//        });
//    }
//
//    private void setSelection(Room data) {
//        RoomAdapter roomAdapter = ((RoomAdapter) getOwnerAdapter());
//        List<Room> roomList = roomAdapter.getAllData();
//
//        if (roomList != null && roomList.size() > 0) {
//            for (Room room : roomList) {
//                if (room.getId() == data.getId()) {
//                    room.setSelected(true);
//                } else {
//                    room.setSelected(false);
//                }
//            }
//            roomAdapter.notifyDataSetChanged();
//        }
//    }
//}