package com.meembusoft.iot.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import com.meembusoft.iot.model.Device;
import com.meembusoft.iot.viewholder.RoomWiseDeviceViewHolder;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RoomWiseDeviceAdapter extends RecyclerArrayAdapter<Device> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public RoomWiseDeviceAdapter(Context context) {
        super( context );
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new RoomWiseDeviceViewHolder( parent, getContext() );
            default:
                throw new InvalidParameterException();
        }
    }

    public void updateItem(Device device) {
        int position = getItemPosition(device);

        if (position != -1) {
            List<Device> devices = new ArrayList<>();
            devices.addAll(getAllData());
            devices.remove(position);
            devices.add(position, device);

            removeAll();
            addAll(devices);
        }
//        else {
//            insert(device, 0);
//        }
        notifyDataSetChanged();
    }

    private int getItemPosition(Device device) {
        List<Device> devices = getAllData();
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getProduct().getProduct_id().equalsIgnoreCase(device.getProduct().getProduct_id())) {
                return i;
            }
        }
        return -1;
    }
}