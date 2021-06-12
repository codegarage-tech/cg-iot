package com.meembusoft.animationmanager;

import android.animation.Animator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.meembusoft.animationmanager.flytocart.CircleAnimationUtil;
import com.meembusoft.animationmanager.listener.AnimationUpdateListener;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AnimationManager {

    public static void makeFlyAnimation(Activity activity, View sourceView, View sourceViewCopy, View destinationView, int timeMilliSecond, Animator.AnimatorListener animatorListener) {
        new CircleAnimationUtil().attachActivity(activity)
                .setTargetView(sourceView)
                .setTargetViewCopy(sourceViewCopy)
                .setMoveDuration(timeMilliSecond)
                .setDestView(destinationView)
                .setAnimationListener(animatorListener).startAnimation();
    }

    public static void makeFlyAnimation(Activity activity, View sourceView, int sourceViewBitmap, View destinationView, int timeMilliSecond, Animator.AnimatorListener animatorListener) {

        //Create a copy view
        ImageView animImg = new ImageView(activity);
//        Bitmap bm = ((BitmapDrawable) sourceView.getDrawable()).getBitmap();
        //  animImg.setImageBitmap(sourceViewBitmap);
        animImg.setImageResource(sourceViewBitmap);

        ViewGroup anim_mask_layout = CircleAnimationUtil.createAnimLayout(activity);
        anim_mask_layout.addView(animImg);

        int[] startXY = new int[2];
        sourceView.getLocationInWindow(startXY);
        final View v = CircleAnimationUtil.addViewToAnimLayout(activity, animImg, startXY, true);
        if (v == null) {
            return;
        }

        new CircleAnimationUtil().attachActivity(activity)
                .setTargetView(sourceView)
                .setTargetViewCopy(v)
                .setMoveDuration(timeMilliSecond)
                .setDestView(destinationView)
                .setAnimationListener(animatorListener).startAnimation();
    }

    public static RotateAnimation makeRotateAnimation(final View view, final int rotationCount, final AnimationUpdateListener animationUpdateListener) {
        int previousDegrees = 0;
        int degrees = 360;
        final RotateAnimation animation = new RotateAnimation(previousDegrees, degrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setDuration(1500);
        animation.setAnimationListener(new Animation.AnimationListener() {

            int count = 0;

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (rotationCount > 0) {
                    count++;
                    if (count == rotationCount) {
                        if (animationUpdateListener != null) {
                            animationUpdateListener.onUpdate(true);
                        }
                    } else {
                        view.startAnimation(animation);
                    }
                } else {
                    view.startAnimation(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
        return animation;
    }
}