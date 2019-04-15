package tech.codegarage.iot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.reversecoder.library.util.AllSettingsManager;

import me.wangyuwei.particleview.ParticleView;
import tech.codegarage.iot.R;
import tech.codegarage.iot.base.BaseUpdateListener;
import tech.codegarage.iot.util.AppUtil;

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
        String appVersion = AppUtil.getAppVersion(SplashActivity.this);
        if (!AllSettingsManager.isNullOrEmpty(appVersion)) {
            tvAppVersion.setText("Version: " + appVersion);
        }

        // Rotate app logo
        AppUtil.makeRotateAnimation(ivAppLogo, 3, new BaseUpdateListener() {
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