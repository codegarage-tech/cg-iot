package com.meembusoft.iot.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.iot.R;
import com.meembusoft.iot.adapter.SizeAdapter;
import com.meembusoft.iot.model.Size;
import com.reversecoder.library.event.OnSingleClickListener;

public class SizeViewHolder extends BaseViewHolder<Size> {

    //    private TextView tvColor, tvColorSelected;
    private Context mContext;

    public SizeViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.row_product_detail_size);
        mContext = context;

//        tvColor = (TextView) itemView.findViewById(R.id.tv_color);
//        tvColorSelected = (TextView) itemView.findViewById(R.id.tv_color_selected);
    }

    @Override
    public void setData(final Size data) {
        TextView tvSize = (TextView) itemView;
        tvSize.setText(data.getSize_name().charAt(0) + "");

        if (data.isSelected()) {
            tvSize.setBackgroundResource(R.drawable.shape_circular_bg_view);
            tvSize.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        } else {
            tvSize.setBackgroundResource(R.drawable.shape_round_border_bg_grey);
            tvSize.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                SizeAdapter sizeAdapter = (SizeAdapter) getOwnerAdapter();
                for (Size size : sizeAdapter.getAllData()) {
                    if (size.getSize_name().equalsIgnoreCase(data.getSize_name())) {
                        size.setSelected(true);
                    } else {
                        size.setSelected(false);
                    }
                }
                sizeAdapter.notifyDataSetChanged();
            }
        });
    }
}