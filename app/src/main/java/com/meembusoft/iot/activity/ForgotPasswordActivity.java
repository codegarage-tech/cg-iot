package com.meembusoft.iot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;

import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ForgotPasswordActivity extends BaseActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;

    private Button btnSubmitEmailAddress;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_forgot_password;
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

        btnSubmitEmailAddress = (Button) findViewById(R.id.btn_submit_email_address);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.title_activity_forgot_password));
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });

        btnSubmitEmailAddress.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }
        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
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