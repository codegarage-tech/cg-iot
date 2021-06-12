package com.meembusoft.iot.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.meembusoft.iot.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PopUpSeekBar extends View {

    private int width;
    private int height;
    private int diameterDefault = 150;
    private float circleRadius = 35F;
    private float fingerX, fingerXmin = circleRadius, fingerXMax;
    private float selectedTextSize;
    private float strokeWidth = 2F;
    private int colorText, colorLine, colorBall, colorBallStroke;
    private Path linePath;
    private Paint linePaint, ballPaint, ballStrokePaint, txtBallPaint;
    private String unit = "";
    private float valueMin, valueMax;
    private float selectedText;
    private OnSelectedListener selectedListener;
    private boolean isScrolling = false;

    public PopUpSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public PopUpSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PopUpSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PopUpSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.selectedTextSize = dp2px(context, 15F);
        this.valueMin = 1;
        this.valueMax = 4;

        this.colorText = Color.WHITE;
        this.fingerX = this.circleRadius;

        initAttr(context, attrs);

        this.linePaint = new Paint();
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setColor(colorLine);
        this.linePaint.setStrokeWidth(strokeWidth);

        this.txtBallPaint = new Paint();
        this.txtBallPaint.setAntiAlias(true);
        this.txtBallPaint.setStyle(Paint.Style.FILL);
        this.txtBallPaint.setColor(colorText);
        this.txtBallPaint.setStrokeWidth(strokeWidth);
        this.txtBallPaint.setTextSize(selectedTextSize);

        this.ballPaint = new Paint();
        this.ballPaint.setAntiAlias(true);
        this.ballPaint.setStyle(Paint.Style.FILL);
        this.ballPaint.setColor(colorBall);

        this.ballStrokePaint = new Paint();
        this.ballStrokePaint.setAntiAlias(true);
        this.ballStrokePaint.setStyle(Paint.Style.STROKE);
        this.ballStrokePaint.setColor(colorBallStroke);
        this.ballStrokePaint.setStrokeWidth(strokeWidth);

        this.linePath = new Path();
        this.linePath.moveTo((float) fingerX, (float) 100);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PopUpSeekBar);

            this.colorBall = attributes.getColor(R.styleable.PopUpSeekBar_fsb_color_ball, Color.BLACK);
            this.colorBallStroke = attributes.getColor(R.styleable.PopUpSeekBar_fsb_color_ball_stroke, Color.BLACK);
            this.colorLine = attributes.getColor(R.styleable.PopUpSeekBar_fsb_color_line, Color.BLACK);
            this.colorText = attributes.getColor(R.styleable.PopUpSeekBar_fsb_color_value_selected, Color.WHITE);
            this.valueMin = attributes.getFloat(R.styleable.PopUpSeekBar_fsb_value_min, 1);
            this.valueMax = attributes.getFloat(R.styleable.PopUpSeekBar_fsb_value_max, 3);
            this.selectedText = attributes.getFloat(R.styleable.PopUpSeekBar_fsb_value_selected, 1);
            this.unit = attributes.getString(R.styleable.PopUpSeekBar_fsb_unit) + "";
            attributes.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = this.getMySize(this.diameterDefault, widthMeasureSpec);
        fingerXMax = width - circleRadius;
        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = this.diameterDefault;
        } else {
            height = this.getMySize(this.diameterDefault, heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String text = (selectedText) + unit;

        linePath.reset();
        linePath.moveTo(0, (float) 2 * height / 3);
        linePath.lineTo(this.fingerX - circleRadius * 2 * 3, (float) 2 * height / 3);
        linePath.lineTo(width, (float) 2 * height / 3);
        canvas.drawPath(linePath, linePaint);

        if (isScrolling()) {
            canvas.drawCircle(fingerX, (float) 2 * height / 6F, circleRadius, ballPaint);
            canvas.drawCircle(fingerX, (float) 2 * height / 6F, circleRadius - strokeWidth, ballStrokePaint);
            canvas.drawText(text, fingerX - getTextWidth(txtBallPaint, text) / 2, ((float) 2 * height / 6F) + getTextHeight(txtBallPaint, text) / 2, txtBallPaint);
        } else {
            canvas.drawCircle(fingerX, (float) 2 * height / 3F, circleRadius, ballPaint);
            canvas.drawCircle(fingerX, (float) 2 * height / 3F, circleRadius - strokeWidth, ballStrokePaint);
            canvas.drawText(text, fingerX - getTextWidth(txtBallPaint, text) / 2, ((float) 2 * height / 3F) + getTextHeight(txtBallPaint, text) / 2, txtBallPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int[] location = new int[2];
        getLocationOnScreen(location);
        int baseX = location[0];
        int baseY = location[1];
        int lastX;
        int lastY;
        Log.d("FloatSeekBar", "FloatSeekBar>>onTouchEvent>>baseX: " + baseX + " baseY: " + baseY);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                Log.d("FloatSeekBar", "FloatSeekBar>>onTouchEvent(ACTION_DOWN)>>baseX: " + baseX + " baseY: " + baseY);

                if (((lastY > (baseY + (height / 2F) - 10F)) && (lastY < (baseY + height + 10F)))) {
                    Log.d("FloatSeekBar", "FloatSeekBar>>onTouchEvent(ACTION_DOWN)>>valid touch point is found.");
                    fingerX = event.getX();
                    if (fingerX < fingerXmin) fingerX = fingerXmin;
                    if (fingerX > fingerXMax) fingerX = fingerXMax;
                } else {
                    Log.d("FloatSeekBar", "FloatSeekBar>>onTouchEvent(ACTION_DOWN)>>valid touch point is not found, ignoring..");
                    postInvalidate();
                    return false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                Log.d("FloatSeekBar", "FloatSeekBar>>onTouchEvent(ACTION_MOVE)>>lastX: " + lastX + " lastY: " + lastY);
                if (isScrolling || ((lastY > (baseY + (height / 2F) - 10F)) && (lastY < (baseY + height + 10F)))) {
                    Log.d("FloatSeekBar", "FloatSeekBar>>onTouchEvent(ACTION_MOVE)>>valid touch point is found.");
                    isScrolling = true;
                    fingerX = event.getX();
                    if (fingerX < fingerXmin) fingerX = fingerXmin;
                    if (fingerX > fingerXMax) fingerX = fingerXMax;
                    postInvalidate();
                } else {
                    Log.d("FloatSeekBar", "FloatSeekBar>>onTouchEvent(ACTION_MOVE)>>valid touch point is not found, ignoring..");
                    postInvalidate();
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                isScrolling = false;
                postInvalidate();
                break;
        }
        selectedText = getFormattedDecimal(valueMin + (valueMax - valueMin) * (fingerX - fingerXmin) / (fingerXMax - fingerXmin));
        if (selectedListener != null) {
            selectedListener.onSelected(selectedText);
        }
        if (!isScrolling) {
            if (selectedListener != null) {
                selectedListener.onFingerUp();
            }
        }
        return true;
    }

    /***************
     * View Helper *
     ***************/
    private float getFormattedDecimal(float originalValue) {
        float optimizedValue = 0f;
        try {
            optimizedValue = Float.valueOf(new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH)).format(originalValue));
        } catch (Exception ex) {
            Log.d("FloatSeekBar", "FloatSeekBar>>getFormattedDecimal>>error: " + ex.getMessage());
        }
        return optimizedValue;
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                mySize = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                mySize = size;
                break;
            case MeasureSpec.EXACTLY:
                mySize = size;
                break;
        }
        return mySize;
    }

    private float px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return px / scale + 0.5f;
    }

    private float dp2px(Context context, float dp) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float getTextWidth(Paint paint, String str) {
        float iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (float) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    private float getTextHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return (float) rect.height();
    }

    /*****************
     * Getter/Setter *
     *****************/
    public boolean isScrolling() {
        return isScrolling;
    }

    public void setValueMax(int valueMax) {
        this.valueMax = valueMax;
    }

    public void setValueMin(int valueMin) {
        this.valueMin = valueMin;
    }

    public float getValueMax() {
        return valueMax;
    }

    public float getValueMin() {
        return valueMin;
    }

    public int getColorText() {
        return colorText;
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
        this.txtBallPaint.setColor(this.colorText);
    }

    public int getColorLine() {
        return colorLine;
    }

    public void setColorLine(int colorLine) {
        this.colorLine = colorLine;
        this.linePaint.setColor(this.colorLine);
    }

    public int getColorBall() {
        return colorBall;
    }

//    public void setSelectedText(int selectedText) {
//        this.selectedText = selectedText;
//        postInvalidate();
//    }

    public void setColorBall(int colorBall) {
        this.colorBall = colorBall;
        this.ballPaint.setColor(this.colorBall);
    }

    public void setSelectedListener(OnSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    public float getSelectedText() {
        return selectedText;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void resetSeekBar() {
        fingerX = circleRadius;
        selectedText = valueMin;
        invalidate();
    }

    public interface OnSelectedListener {

        void onSelected(float value);

        void onFingerUp();
    }
}