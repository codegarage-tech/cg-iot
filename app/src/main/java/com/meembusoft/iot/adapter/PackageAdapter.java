package com.meembusoft.iot.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.iot.model.Subscription;
import com.meembusoft.iot.viewholder.PackageViewHolder;

import java.security.InvalidParameterException;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PackageAdapter extends RecyclerArrayAdapter<Subscription> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public PackageAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new PackageViewHolder(parent, getContext());
            default:
                throw new InvalidParameterException();
        }
    }
}