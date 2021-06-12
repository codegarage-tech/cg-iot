package com.meembusoft.iot.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.meembusoft.iot.R;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class HangerLayout extends FrameLayout {

    private int bottomDragVisibleHeight; // Visible height
    private int bototmExtraIndicatorHeight = 0; // Height of bottom indicator
    private int dragTopDest = 0; // Top View Slide Target Position
    private static final int DECELERATE_THRESHOLD = 120;
    private static final int DRAG_SWITCH_DISTANCE_THRESHOLD = 100;
    private static final int DRAG_SWITCH_VEL_THRESHOLD = 800;

    private static final float MIN_SCALE_RATIO = 0.5f;
    private static final float MAX_SCALE_RATIO = 1.0f;

    private static final int STATE_CLOSE = 1;
    private static final int STATE_EXPANDED = 2;
    private int downState; // State when pressed

    private final ViewDragHelper mDragHelper;
    private final GestureDetectorCompat moveDetector;
    private int mTouchSlop = 5; // Threshold value to be judged as sliding, unit is pixel
    private int originTop, originX, originY; // The coordinates of topView in the initial state
    private View bottomView, topView; // FrameLayout's two child Views
    private String TAG = "DragLayout";

    private GotoDetailListener gotoDetailListener;

    public HangerLayout(Context context) {
        this(context, null);
    }

    public HangerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HangerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HangerLayout, 0, 0);
        bottomDragVisibleHeight = (int) a.getDimension(R.styleable.HangerLayout_bottomDragVisibleHeight, 0);
        bototmExtraIndicatorHeight = (int) a.getDimension(R.styleable.HangerLayout_bototmExtraIndicatorHeight, 0);
        a.recycle();

        mDragHelper = ViewDragHelper.create(this, 10f, new DragHelperCallback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
        moveDetector = new GestureDetectorCompat(context, new MoveDetector());
        moveDetector.setIsLongpressEnabled(false); // Do not handle long press events

        // The distance threshold for sliding is provided by the system
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bottomView = getChildAt(0);
        topView = getChildAt(1);

        topView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleState();
            }
        });
    }

//    // Go to next page
//    private void gotoDetailActivity() {
//        if (null != gotoDetailListener) {
//            gotoDetailListener.gotoDetail();
//        }
//    }

    public void toggleState() {
        // Click callback
        int state = getCurrentState();
        if (state == STATE_CLOSE) {
            // Initial state when clicked, need to expand
            Log.d(TAG, "onClick>>STATE_CLOSE>>originX: " + originX + " dragTopDest: " + dragTopDest);
            if (mDragHelper.smoothSlideViewTo(topView, originX, dragTopDest)) {
                ViewCompat.postInvalidateOnAnimation(HangerLayout.this);
            }
        } else {
            Log.d(TAG, "onClick>>STATE_EXPANDED>>originX: " + originX + " dragTopDest: " + dragTopDest);
            // When clicked, it is expanded and directly enters the details page
//                    gotoDetailActivity();
//                    if (mDragHelper.smoothSlideViewTo(bottomView, bottomView.getLeft(), dragTopDest)
//                    || mDragHelper.smoothSlideViewTo(topView, topView.getLeft(), dragTopDest-bottomDragVisibleHeight-topView.getMeasuredHeight())
//                    ) {
////                        postResetPosition();
//                        ViewCompat.postInvalidateOnAnimation(DragLayout.this);
//                    }

//                    if (mDragHelper.smoothSlideViewTo(topView, topView.getLeft(), bottomView.getBottom()-dragTopDest - bottomDragVisibleHeight)) {
            if (mDragHelper.smoothSlideViewTo(topView, topView.getLeft(), originTop)) {
                ViewCompat.postInvalidateOnAnimation(HangerLayout.this);
            }
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == topView) {
                Log.d(TAG, "DragHelperCallback>>onViewPositionChanged>>changedView is topView");
                processLinkageView();
            } else {
                Log.d(TAG, "DragHelperCallback>>onViewPositionChanged>>changedView is not topView");
            }
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (child == topView) {
                Log.d(TAG, "DragHelperCallback>>tryCaptureView>>changedView is topView");
                return true;
            } else {
                Log.d(TAG, "DragHelperCallback>>tryCaptureView>>changedView is not topView");
            }
            return false;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int currentTop = child.getTop();
            if (top > child.getTop()) {
                // When dragging down, resistance is minimal
                return currentTop + (top - currentTop) / 2;
            }

            int result;
            if (currentTop > DECELERATE_THRESHOLD * 3) {
                result = currentTop + (top - currentTop) / 2;
            } else if (currentTop > DECELERATE_THRESHOLD * 2) {
                result = currentTop + (top - currentTop) / 4;
            } else if (currentTop > 0) {
                result = currentTop + (top - currentTop) / 8;
            } else if (currentTop > -DECELERATE_THRESHOLD) {
                result = currentTop + (top - currentTop) / 16;
            } else if (currentTop > -DECELERATE_THRESHOLD * 2) {
                result = currentTop + (top - currentTop) / 32;
            } else if (currentTop > -DECELERATE_THRESHOLD * 3) {
                result = currentTop + (top - currentTop) / 48;
            } else {
                result = currentTop + (top - currentTop) / 64;
            }
            return result;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return child.getLeft();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 600;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 600;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalY = originY;
            if (downState == STATE_CLOSE) {
                // When pressed, the state is: initial state
                if (originY - releasedChild.getTop() > DRAG_SWITCH_DISTANCE_THRESHOLD || yvel < -DRAG_SWITCH_VEL_THRESHOLD) {
                    finalY = dragTopDest;
                }
            } else {
                // When pressed, the state is: expanded state
                boolean gotoBottom = releasedChild.getTop() - dragTopDest > DRAG_SWITCH_DISTANCE_THRESHOLD || yvel > DRAG_SWITCH_VEL_THRESHOLD;
                if (!gotoBottom) {
                    finalY = dragTopDest;

                    // If it is already expanded when you press it and drag it up again, you will enter the detail page
                    if (dragTopDest - releasedChild.getTop() > mTouchSlop) {
//                        gotoDetailActivity();
//                        postResetPosition();
//                        return;
                    }
                }
            }

            if (mDragHelper.smoothSlideViewTo(releasedChild, originX, finalY)) {
                ViewCompat.postInvalidateOnAnimation(HangerLayout.this);
            }
        }
    }


    private void postResetPosition() {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                topView.offsetTopAndBottom(dragTopDest - topView.getTop());
            }
        }, 500);
    }

    /**
     * The position of the top ImageView changes, and the bottom view needs to be scaled.
     */
    private void processLinkageView() {
        Log.d(TAG, "processLinkageView>> inside processLinkageView");
        Log.d(TAG, "processLinkageView>> topView.getTop(): " + topView.getTop() + " originY: " + originY);
        if (topView.getTop() > originY) {
            bottomView.setAlpha(0);
        } else {
            float alpha = (originY - topView.getTop()) * 0.01f;
            if (alpha > 1) {
                alpha = 1;
            }
            bottomView.setAlpha(alpha);
            int maxDistance = originY - dragTopDest;
            int currentDistance = topView.getTop() - dragTopDest;
            float scaleRatio = 1;
            float distanceRatio = (float) currentDistance / maxDistance;
            if (currentDistance > 0) {
                scaleRatio = MIN_SCALE_RATIO + (MAX_SCALE_RATIO - MIN_SCALE_RATIO) * (1 - distanceRatio);
            }
            bottomView.setScaleX(scaleRatio);
            bottomView.setScaleY(scaleRatio);
        }
    }

    class MoveDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx,
                                float dy) {
            // Dragged, touch does not pass down
            return Math.abs(dy) + Math.abs(dx) > mTouchSlop;
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * Get the current status
     */
    private int getCurrentState() {
        int state;
        if (Math.abs(topView.getTop() - dragTopDest) <= mTouchSlop) {
            state = STATE_EXPANDED;
        } else {
            state = STATE_CLOSE;
        }
        return state;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!changed) {
            return;
        }

        super.onLayout(changed, left, top, right, bottom);

        originTop = (int) topView.getTop();
        originX = (int) topView.getX();
        originY = (int) topView.getY();
        dragTopDest = bottomView.getBottom() - bottomDragVisibleHeight - topView.getMeasuredHeight();
    }

    /* The interception and processing of touch events are handed over to mDraghelper */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 1. The detector and mDragHelper determine whether interception is needed
        boolean yScroll = moveDetector.onTouchEvent(ev);
        boolean shouldIntercept = false;
        try {
            shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
        }

        // 2. When the contact is pressed directly to mDragHelper
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            downState = getCurrentState();
            mDragHelper.processTouchEvent(ev);
        }

        return shouldIntercept && yScroll;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // The calculation of the bottomMarginTop height still requires a clear mathematical model.
        // The effect achieved is that the topView.top and bottomView.bottom are centered before and after being expanded.
        int bottomMarginTop = (bottomDragVisibleHeight + topView.getMeasuredHeight() / 2 - bottomView.getMeasuredHeight() / 2) / 2 - bototmExtraIndicatorHeight;
        LayoutParams lp1 = (LayoutParams) bottomView.getLayoutParams();
        lp1.setMargins(0, bottomMarginTop, 0, 0);
        bottomView.setLayoutParams(lp1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Handed over to mDragHelper uniformly, drag effect is realized by DragHelperCallback
        try {
            mDragHelper.processTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void setGotoDetailListener(GotoDetailListener gotoDetailListener) {
        this.gotoDetailListener = gotoDetailListener;
    }

    public interface GotoDetailListener {
        public void gotoDetail();
    }
}
