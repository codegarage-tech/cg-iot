package com.meembusoft.iot.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ResizeWindowForSoftKeyboard {

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private Rect contentAreaOfWindowBounds = new Rect();

    public ResizeWindowForSoftKeyboard(Activity activity) {
        initContentView(activity);
    }

    public ResizeWindowForSoftKeyboard(View view) {
        if (view != null) {
            Activity activity = (Activity) view.getContext();
            initContentView(activity);
        }
    }

    private void initContentView(Activity activity) {
        if (activity != null) {
            FrameLayout content = activity.findViewById(android.R.id.content);
            mChildOfContent = content.getChildAt(0);
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    resizeContentChild();
                }
            });
            frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
        }
    }

    private void resizeContentChild() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int heightDifference = 0;
            if (heightDifference > (usableHeightNow / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightNow - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightNow;
            }
            mChildOfContent.layout(contentAreaOfWindowBounds.left, contentAreaOfWindowBounds.top, contentAreaOfWindowBounds.right, contentAreaOfWindowBounds.bottom);
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        mChildOfContent.getWindowVisibleDisplayFrame(contentAreaOfWindowBounds);
        return contentAreaOfWindowBounds.height();
    }
}