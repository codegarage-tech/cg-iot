package com.meembusoft.iot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.animationmanager.AnimationManager;
import com.meembusoft.animationmanager.listener.AnimationUpdateListener;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseUpdateListener;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.localemanager.LocaleManager;
import com.reversecoder.library.util.AllSettingsManager;

import me.wangyuwei.particleview.ParticleView;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SplashActivity extends AppCompatActivity {

    private TextView tvAppVersion;
    private ImageView ivAppLogo;
    private ParticleView particleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Locale manager
        LocaleManager.initialize(SplashActivity.this);

        setContentView(R.layout.activity_splash);

        initStatusBar();
        initActivityViews();
        initActivityViewsData();
    }

    private void initStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
        StatusBarUtil.setTransparent(SplashActivity.this);
    }

    public void initActivityViews() {
        tvAppVersion = (TextView) findViewById(R.id.tv_app_version);
        ivAppLogo = (ImageView) findViewById(R.id.iv_app_logo_short);
        particleView = (ParticleView) findViewById(R.id.pv_title);
    }

    public void initActivityViewsData() {
        // Start particle animation
        particleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                particleView.startAnim();
            }
        }, 200);
        particleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                //Navigate to the next screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigateNextScreen();
                    }
                }, 1000);
            }
        });

        // Set app version
        String appVersion = AppUtil.getApplicationVersion(SplashActivity.this);
        if (!AllSettingsManager.isNullOrEmpty(appVersion)) {
            tvAppVersion.setText(getString(R.string.txt_version) + ": " + appVersion);
        }

        // Rotate app logo
        AnimationManager.makeRotateAnimation(ivAppLogo, 3, new AnimationUpdateListener() {
            @Override
            public void onUpdate(Object... update) {
                if ((boolean) update[0]) {
                    //Navigate to the next screen
//                    navigateNextScreen();
                }
            }
        });
    }

    private void navigateNextScreen() {
        Intent intentAppUser = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intentAppUser);
        finish();
    }
}