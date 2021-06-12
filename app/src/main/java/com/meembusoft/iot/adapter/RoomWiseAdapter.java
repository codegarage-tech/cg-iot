//package com.meembusoft.iot.adapter;
//
//import android.content.Context;
//import android.view.ViewGroup;
//
//import com.jude.easyrecyclerview.adapter.BaseViewHolder;
//import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
//
//import java.security.InvalidParameterException;
//
//import com.meembusoft.iot.model.Favourite;
//import com.meembusoft.iot.model.RoomWise;
//import com.meembusoft.iot.viewholder.FavoriteViewHolder;
//import com.meembusoft.iot.viewholder.RoomWiseViewHolder;
//
///**
// * @author Md. Rashadul Alam
// * Email: rashed.droid@gmail.com
// */
//public class RoomWiseAdapter extends RecyclerArrayAdapter<RoomWise> {
//
//    private static final int VIEW_TYPE_REGULAR = 1;
//    Context mContext;
//
//    public RoomWiseAdapter(Context context) {
//        super( context );
//        mContext = context;
//    }
//
//    @Override
//    public int getViewType(int position) {
//        return VIEW_TYPE_REGULAR;
//    }
//
//    @Override
//    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case VIEW_TYPE_REGULAR:
//                return new RoomWiseViewHolder( parent,mContext );
//            default:
//                throw new InvalidParameterException();
//        }
//    }
//}