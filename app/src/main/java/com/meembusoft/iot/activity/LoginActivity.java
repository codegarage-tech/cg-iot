package com.meembusoft.iot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.model.Image;
import com.meembusoft.iot.model.User;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.util.SessionUtil;
import com.meembusoft.retrofitmanager.APIResponse;
import com.reversecoder.library.event.OnSingleClickListener;

import bz.kakadu.sociallogin.SocialLogin;
import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;

import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_REQUEST_CODE_LOGIN;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class LoginActivity extends BaseActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;

    private TextView tvSignupNow, tvForgotPassword;
    private LinearLayout llLogin;
    private RelativeLayout rlFacebookLogin, rlGoogleLogin;

    //Background task

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_login;
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

        tvSignupNow = (TextView) findViewById(R.id.tv_signup_now);
        tvForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
        llLogin = (LinearLayout) findViewById(R.id.ll_login);
        rlFacebookLogin = (RelativeLayout) findViewById(R.id.rl_fb_login);
        rlGoogleLogin = (RelativeLayout) findViewById(R.id.rl_google_login);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.title_activity_login));
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });

        tvSignupNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignup = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intentSignup);
                finish();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPassword = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intentForgotPassword);
            }
        });

        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User("1", "", "Md. Rashadul Alam", "01794620787", "rashed.droid@gmail.com", "Ka-13, South Kuril", "123456", new Image("1", ""));
                SessionUtil.setUser(getActivity(), APIResponse.getJSONStringFromObject(user));
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

        rlFacebookLogin.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                startActivityForResult(SocialLogin.Companion.loginIntent(getActivity(), SocialLogin.LoginType.FB), INTENT_KEY_REQUEST_CODE_LOGIN);
            }
        });

        rlGoogleLogin.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                startActivityForResult(SocialLogin.Companion.loginIntent(getActivity(), SocialLogin.LoginType.GOOGLE), INTENT_KEY_REQUEST_CODE_LOGIN);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INTENT_KEY_REQUEST_CODE_LOGIN:
                if (data != null) {
                    Parcelable parcelable = data.getParcelableExtra("loginResult");

                    if (parcelable != null) {
                        Logger.d(TAG, TAG + ">>onActivityResult>>loginResult: " + parcelable);
//                        AlertDialog.Builder(this)
//                                .setMessage(result.toString())
//                                .setTitle("Login result")
//                                .setPositiveButton(android.R.string.ok, null)
//                                .apply {
//                            if (result?.isSuccess == true) {
//                                setNeutralButton("Logout") { _, _ ->
//                                        SocialLogin.logout(
//                                                this@MainActivity,
//                                    SocialLogin.LoginType.GOOGLE
//                                    )
//                                }
//                            }
//                        }
//                        .show()

                    }
                }
                break;
        }
    }
}