package com.meembusoft.iot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meembusoft.iot.R;
import com.meembusoft.iot.model.Image;
import com.meembusoft.iot.util.AppUtil;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {

    private Context mContext;
    private int mCount;
    private List<Image> mData = new ArrayList<>();

    public ImageSliderAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Image> data) {
        this.mData = data;
        this.mCount = data.size();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_detail_image_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        Image image = mData.get(position);
        viewHolder.imageGifContainer.setVisibility(View.GONE);
        viewHolder.imageDescriptionContainer.setVisibility(View.GONE);
        AppUtil.loadImage(mContext, viewHolder.imageViewBackground, image.getUrl(), false, false, true);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mCount;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;
        View imageDescriptionContainer;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            imageDescriptionContainer = itemView.findViewById(R.id.fl_shadow_container);
            this.itemView = itemView;
        }
    }
}
