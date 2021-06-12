package com.meembusoft.fcmmanager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AnimatedImageView extends AppCompatImageView {

    public AnimatedImageView(Context context) {
        super(context);
    }

    public AnimatedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAnimatedImage(int newImage, long startDelay) {
        changeImage(newImage, startDelay);
    }

    private void changeImage(final int newImage, long startDelay) {
        if ((int)getTag() == newImage) {
            return;
        }
        AnimatedView.animate(this, startDelay, new AnimatedView.Action() {
            @Override
            public void action(View view) {
                setImageResource(newImage);
                setTag(newImage);
            }
        });
    }
}