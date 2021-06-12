package com.meembusoft.iot.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eggheadgames.aboutbox.AboutBoxUtils;
import com.eggheadgames.aboutbox.AboutConfig;
import com.jaeger.library.StatusBarUtil;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.IntentManager;
import com.reversecoder.library.event.OnSingleClickListener;

import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DownloadAppActivity extends BaseActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;

    private ImageView ivQRCode;
    private TextView tvPlayStoreUrl;
    private String packageName = "", playStoreUrl;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_download_app;
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

    }

    @Override
    public void initActivityViews() {
        //toolbar view
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbar_title);
        leftMenu = (AnimatedImageView) findViewById(R.id.left_menu);
        rightMenu = (ImageView) findViewById(R.id.right_menu);

        ivQRCode = (ImageView) findViewById(R.id.iv_qr_code);
        tvPlayStoreUrl = (TextView) findViewById(R.id.tv_play_store_url);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        setQRCode();
        setToolBarTitle(getString(R.string.title_activity_download_app));
        setRightMenu(true, R.drawable.vector_share_empty_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentManager.shareToAllAvailableApps(getActivity(), getString(R.string.txt_share_with), "", playStoreUrl);
            }
        });
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        leftMenu.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                initActivityBackPress();
            }
        });

        tvPlayStoreUrl.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                AboutBoxUtils.openApp(getActivity(), AboutConfig.BuildType.GOOGLE, packageName);
            }
        });
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

    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    public void setRightMenu(boolean visibility, int resId, View.OnClickListener onClickListener) {
        rightMenu.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        if (resId > 0) {
            rightMenu.setBackgroundResource(resId);
        }
        rightMenu.setOnClickListener(onClickListener);
    }

    public void setToolBarTitle(String title) {
        toolbarTitle.setAnimatedText(title.toUpperCase(), 0L);

        //For marquee address
        AppUtil.applyMarqueeOnTextView(toolbarTitle);
    }

    private void setQRCode() {
        packageName = getActivity().getPackageName();
        playStoreUrl = String.format(getString(R.string.app_play_store_url), packageName);
        AppUtil.generateQRCode(playStoreUrl, ivQRCode, 250, 250);

        tvPlayStoreUrl.setText(playStoreUrl);
        tvPlayStoreUrl.setPaintFlags(tvPlayStoreUrl.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}