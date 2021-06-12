package com.meembusoft.iot.view.viewfactory;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.StyleRes;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class TextViewFactory implements ViewSwitcher.ViewFactory {

    @StyleRes
    final int styleId;
    final boolean center;
    Context mContext;

    public TextViewFactory(Context context, @StyleRes int styleId, boolean center) {
        this.styleId = styleId;
        this.center = center;
        this.mContext = context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View makeView() {
        final TextView textView = new TextView(mContext);

        if (center) {
            textView.setGravity(Gravity.CENTER);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            textView.setTextAppearance(mContext, styleId);
        } else {
            textView.setTextAppearance(styleId);
        }

        return textView;
    }

}