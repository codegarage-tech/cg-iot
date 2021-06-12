package com.meembusoft.iot.view.viewfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ImageViewFactory implements ViewSwitcher.ViewFactory {

    Context mContext;

    public ImageViewFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public View makeView() {
        final ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        final ViewGroup.LayoutParams lp = new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lp);

        return imageView;
    }
}