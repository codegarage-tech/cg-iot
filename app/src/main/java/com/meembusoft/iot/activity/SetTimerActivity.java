package com.meembusoft.iot.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.captaindroid.tvg.Tvg;
import com.jaeger.library.StatusBarUtil;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.model.Device;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.Logger;
import com.zjun.widget.TimeRuleView;

import org.parceler.Parcels;

import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;

import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_EXTRA_DEVICE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SetTimerActivity extends BaseActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;

    //
    private TextView tvTime;
    private TextView tvTitle;
    private TimeRuleView timeRuleView;
    public Device mDevice;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_set_timer_view;
    }

    @Override
    public void initStatusBarView() {
        StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {
        Parcelable parcelable = intent.getParcelableExtra(INTENT_KEY_EXTRA_DEVICE);
        if (parcelable != null) {
            mDevice = Parcels.unwrap(parcelable);
            Logger.d(TAG, "mDevice: " + mDevice.toString());
        }
    }

    @Override
    public void initActivityViews() {
        //toolbar view
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbar_title);
        leftMenu = (AnimatedImageView) findViewById(R.id.left_menu);
        rightMenu = (ImageView) findViewById(R.id.right_menu);
        rightMenu.setVisibility(View.VISIBLE);
        rightMenu.setBackgroundResource(R.drawable.vector_plus_circular_bg_white);
       // tvTitle = (TextView) findViewById(R.id.tv_title);
       // tvTime = (TextView) findViewById(R.id.tv_time);
       // timeRuleView = (TimeRuleView) findViewById(R.id.time_rule_view);


//        // 模拟时间段数据
//        List<TimeRuleView.TimePart> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            TimeRuleView.TimePart part = new TimeRuleView.TimePart();
//            part.startTime = i * 1000;
//            part.endTime = part.startTime + new Random().nextInt(1000);
//            list.add(part);
//        }
//        trvTime.setTimePartList(list);


        Tvg.change((TextView) findViewById(R.id.tv_title), new int[]{
                Color.parseColor("#1565C0"),
                Color.parseColor("#7632A7"),
                Color.parseColor("#64B678"),
                Color.parseColor("#839094"),
                Color.parseColor("#8446CC"),
        });
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.title_activity_set_timer));
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });

//        timeRuleView.setOnTimeChangedListener(new TimeRuleView.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(int newTimeValue) {
//                tvTime.setText(TimeRuleView.formatTimeHHmmss(newTimeValue));
//            }
//        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        finish();
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissProgressDialog();
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setToolBarTitle(String title) {
        toolbarTitle.setAnimatedText(title, 0L);

        //For marquee address
        AppUtil.applyMarqueeOnTextView(toolbarTitle);
    }
}