package com.meembusoft.fcmmanager.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AnimatedTextView extends AppCompatTextView {

    private String CANARO_EXTRA_BOLD_PATH = "fonts/canaro_extra_bold.otf";

    public AnimatedTextView(Context context) {
        super(context);
    }

    public AnimatedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAnimatedText(CharSequence text, long startDelay) {
        changeText(text, startDelay);
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), CANARO_EXTRA_BOLD_PATH));
    }

    private void changeText(final CharSequence newText, long startDelay) {
        if (getText() == newText) {
            return;
        }
        AnimatedView.animate(this, startDelay, new AnimatedView.Action() {
            @Override
            public void action(View view) {
                setText(newText);
            }
        });
    }
}