package com.meembusoft.iot.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.iot.R;
import com.meembusoft.iot.adapter.ColorAdapter;
import com.meembusoft.iot.model.Color;
import com.meembusoft.iot.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;

public class ColorViewHolder extends BaseViewHolder<Color> {

    private TextView tvColor, tvColorSelected;
    private Context mContext;

    public ColorViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.row_product_detail_color);
        mContext = context;

        tvColor = (TextView) itemView.findViewById(R.id.tv_color);
        tvColorSelected = (TextView) itemView.findViewById(R.id.tv_color_selected);
    }

    @Override
    public void setData(final Color data) {
        tvColorSelected.setText(data.getColor_name().charAt(0) + "");
        tvColorSelected.setVisibility(data.isSelected() ? View.GONE : View.VISIBLE);

        int selectedColor = 0;
        if (data.getColor_name().toLowerCase().equalsIgnoreCase("white")) {
            selectedColor = R.color.colorWhite;
            tvColorSelected.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        } else if (data.getColor_name().toLowerCase().equalsIgnoreCase("blue")) {
            selectedColor = R.color.colorPrimaryDark;
            tvColorSelected.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        } else if (data.getColor_name().toLowerCase().equalsIgnoreCase("pink")) {
            selectedColor = R.color.colorPink;
            tvColorSelected.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        }
        AppUtil.applyViewTint(tvColor, selectedColor);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                ColorAdapter colorAdapter = (ColorAdapter) getOwnerAdapter();
                for (Color color : colorAdapter.getAllData()) {
                    if (color.getColor_name().equalsIgnoreCase(data.getColor_name())) {
                        color.setSelected(true);
                    } else {
                        color.setSelected(false);
                    }
                }
                colorAdapter.notifyDataSetChanged();
            }
        });
    }
}