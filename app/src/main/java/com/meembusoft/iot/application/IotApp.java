package com.meembusoft.iot.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.meembusoft.addtocart.AddToCartManager;
import com.meembusoft.fcmmanager.FcmManager;
import com.meembusoft.fcmmanager.activity.FcmPayloadActivity;
import com.meembusoft.fcmmanager.payload.App;
import com.meembusoft.fcmmanager.payload.Link;
import com.meembusoft.fcmmanager.payload.Ping;
import com.meembusoft.fcmmanager.payload.Text;
import com.meembusoft.fcmmanager.util.RawDataType;
import com.meembusoft.iot.activity.AnnouncementDetailActivity;
import com.meembusoft.iot.activity.OrderDetailActivity;
import com.meembusoft.iot.activity.ProductDetailActivity;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.localemanager.LocaleManager;

import io.armcha.ribble.RibbleMenu;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class IotApp extends Application {

    private static Application mContext;
    private static final String CANARO_EXTRA_BOLD_PATH = "fonts/canaro_extra_bold.otf";
    public static Typeface canaroExtraBold;

    @Override
    public void onCreate() {
        super.onCreate();

        if (mContext == null) {
            mContext = this;
        }

        // Init FCM information
        FcmManager.initFcmManager(mContext, new FcmManager.FcmBuilder()
                .addNotificationContentInfo(App.class.getSimpleName(), FcmPayloadActivity.class)
                .addNotificationContentInfo(Link.class.getSimpleName(), FcmPayloadActivity.class)
                .addNotificationContentInfo(Ping.class.getSimpleName(), FcmPayloadActivity.class)
                .addNotificationContentInfo(Text.class.getSimpleName(), FcmPayloadActivity.class)
                // Raw data can be different type
                .addNotificationContentInfo(RawDataType.PRODUCT_DETAIL.getValue(), ProductDetailActivity.class)
                .addNotificationContentInfo(RawDataType.ORDER_DETAIL.getValue(), OrderDetailActivity.class)
                .addNotificationContentInfo(RawDataType.ANNOUNCEMENT_DETAIL.getValue(), AnnouncementDetailActivity.class)
                .buildGcmManager()
        );

        // Init add to cart db
        AddToCartManager.initialize(mContext);

        // Init Locale
        LocaleManager.initialize(mContext);

        //Initialize logger
        new Logger.Builder()
//                .isLoggable(AppUtil.isDebug(mContext))
                .isLoggable(true)
                .build();

        // Initialize Ribble Menu
        RibbleMenu.initRibbleMenu(mContext);

        //For using vector drawable
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        //Initialize font
        initTypeface();

        //Multidex initialization
        MultiDex.install(mContext);
    }

    private void initTypeface() {
        canaroExtraBold = Typeface.createFromAsset(getAssets(), CANARO_EXTRA_BOLD_PATH);
    }

    public static Context getGlobalContext() {
        return mContext.getApplicationContext();
    }
}