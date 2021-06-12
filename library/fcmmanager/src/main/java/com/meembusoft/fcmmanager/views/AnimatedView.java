package com.meembusoft.fcmmanager.views;

import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AnimatedView {

    public interface Action {
        public void action(View view);
    }

    public static <V extends View> void animate(final V view, long startDelay, final Action action) {
        final long duration = 170L;
        final float scaleFactor = 0.75f;
        view.clearAnimation();
        view.animate()
                .alpha(0f)
                .scaleX(scaleFactor)
                .setDuration(duration)
                .withLayer()
                .setInterpolator(new FastOutSlowInInterpolator())
                .setStartDelay(startDelay)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        action.action(view);
                        view.setScaleX(scaleFactor);
                        view.animate()
                                .scaleX(1f)
                                .alpha(1f)
                                .setListener(null)
                                .withLayer()
                                .setDuration(duration)
                                .start();
                    }
                })
                .start();
    }
}