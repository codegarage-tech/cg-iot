package com.meembusoft.iot.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseEnum;

/**
 * @author Md. Hozrot Belal
 * Email: belal.cse.brur@gmail.com
 */
public class LanguageSpinnerAdapter extends BaseAdapter {

    private Activity mActivity;
    private BaseEnum[] mData;
    private static LayoutInflater inflater = null;

    public LanguageSpinnerAdapter(Activity activity, BaseEnum[] data) {
        mActivity = activity;
        mData = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BaseEnum[] getData() {
        return mData;
    }

    public void setData(BaseEnum[] data) {
        mData = data;
        notifyDataSetChanged();
    }

    public int getItemPosition(BaseEnum name) {
        for (int i = 0; i < mData.length; i++) {
            if (mData[i].getLabel(mActivity).equalsIgnoreCase(name.getLabel(mActivity))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public BaseEnum getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.spinner_row_language, null);
        }
        TextView names = (TextView) vi.findViewById(R.id.tv_item_name);
        names.setText(getItem(position).getLabel(mActivity));
        return vi;
    }
}