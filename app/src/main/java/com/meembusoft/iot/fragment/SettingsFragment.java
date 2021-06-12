package com.meembusoft.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.eggheadgames.aboutbox.activity.AboutActivity;
import com.eggheadgames.aboutbox.listener.LicenseClickListener;
import com.github.zagum.switchicon.SwitchIconView;
import com.meembusoft.fcmmanager.FcmManager;
import com.meembusoft.iot.R;
import com.meembusoft.iot.activity.AboutProfileActivity;
import com.meembusoft.fcmmanager.activity.AppNotificationsActivity;
import com.meembusoft.iot.activity.CartActivity;
import com.meembusoft.iot.activity.DownloadAppActivity;
import com.meembusoft.iot.activity.FavoriteProductActivity;
import com.meembusoft.iot.activity.HomeActivity;
import com.meembusoft.iot.activity.LoginActivity;
import com.meembusoft.iot.activity.LoginViaQRCodeActivity;
import com.meembusoft.iot.activity.OnlineSupportActivity;
import com.meembusoft.iot.activity.OrdersActivity;
import com.meembusoft.iot.adapter.LanguageSpinnerAdapter;
import com.meembusoft.iot.base.BaseFragment;
import com.meembusoft.iot.enumeration.Language;
import com.meembusoft.iot.util.AllConstants;
import com.meembusoft.iot.util.SessionUtil;
import com.meembusoft.localemanager.LocaleManager;
import com.meembusoft.localemanager.languagesupport.LanguagesSupport;
import com.reversecoder.attributionpresenter.activity.LicenseActivity;
import com.reversecoder.library.event.OnSingleClickListener;

import io.armcha.ribble.presentation.navigationview.NavigationId;

import static android.app.Activity.RESULT_OK;
import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_REQUEST_CODE_LOGIN;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SettingsFragment extends BaseFragment {

    private SwitchIconView switchStopNotification;
    private LinearLayout llAccountLoggedIn, llAccountLoggedOut;
    private RelativeLayout rlLoginOrCreateAccount, rlLoginViaQrCode, rlAboutProfile, rlOrders, rlFavoriteProduct, rlCart, rlLogout, rlStopNotification, rlAppNotifications, rlAboutApp, rlDownloadApp, rlOnlineSupport;
    private Spinner spinnerLanguage;
    private LanguageSpinnerAdapter languageSpinnerAdapter;
    private boolean isFirstTimeLanguageSelection = true;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        llAccountLoggedIn = parentView.findViewById(R.id.ll_account_logged_in);
        llAccountLoggedOut = parentView.findViewById(R.id.ll_account_logged_out);
        rlLoginOrCreateAccount = parentView.findViewById(R.id.rl_login_or_create_account);
        rlLoginViaQrCode = parentView.findViewById(R.id.rl_login_via_qr_code);
        rlAboutProfile = parentView.findViewById(R.id.rl_about_profile);
        rlOrders = parentView.findViewById(R.id.rl_orders);
        rlFavoriteProduct = parentView.findViewById(R.id.rl_favorite_product);
        rlCart = parentView.findViewById(R.id.rl_cart);
        rlLogout = parentView.findViewById(R.id.rl_logout);
        switchStopNotification = parentView.findViewById(R.id.switch_stop_notification);
        rlStopNotification = parentView.findViewById(R.id.rl_stop_notification);
        rlAppNotifications = parentView.findViewById(R.id.rl_app_notifications);
        spinnerLanguage = parentView.findViewById(R.id.spinner_language);
        rlAboutApp = parentView.findViewById(R.id.rl_about_app);
        rlDownloadApp = parentView.findViewById(R.id.rl_download_app);
        rlOnlineSupport = parentView.findViewById(R.id.rl_online_support);
    }

    @Override
    public void initFragmentViewsData() {
        refreshAccountView();
        refreshNotificationView();
        initLanguageSpinner();
    }

    @Override
    public void initFragmentActions() {
        rlLoginOrCreateAccount.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivityForResult(intentLogin, INTENT_KEY_REQUEST_CODE_LOGIN);
            }
        });

        rlLoginViaQrCode.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentLoginViaQRCode = new Intent(getActivity(), LoginViaQRCodeActivity.class);
                getActivity().startActivity(intentLoginViaQRCode);
            }
        });

        rlAboutProfile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentAboutProfile = new Intent(getActivity(), AboutProfileActivity.class);
                getActivity().startActivity(intentAboutProfile);
            }
        });

        rlOrders.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentOrders = new Intent(getActivity(), OrdersActivity.class);
                getActivity().startActivity(intentOrders);
            }
        });

        rlFavoriteProduct.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentFavorite = new Intent(getActivity(), FavoriteProductActivity.class);
                getActivity().startActivity(intentFavorite);
            }
        });

        rlCart.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentCart = new Intent(getActivity(), CartActivity.class);
                getActivity().startActivity(intentCart);
            }
        });

        rlLogout.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                ((HomeActivity) getActivity()).doLogout(NavigationId.SETTINGS);
                refreshAccountView();
            }
        });

        rlStopNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean stopNotificationStatus = FcmManager.getInstance().isAppNotificationStopped();
                switchStopNotification.setIconEnabled(stopNotificationStatus);
                FcmManager.getInstance().stopAppNotification(!stopNotificationStatus);
            }
        });

        rlAppNotifications.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentAppNotifications = new Intent(getActivity(), AppNotificationsActivity.class);
                getActivity().startActivity(intentAppNotifications);
            }
        });

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // To avoid infinite loop while selecting previous selection at the very first time isFirstTimeLanguageSelection is used
                if (!isFirstTimeLanguageSelection) {
                    if (position == 1) {
                        LocaleManager.setLocale(getActivity(), LanguagesSupport.Language.ENGLISH);
                    } else if (position == 2) {
                        LocaleManager.setLocale(getActivity(), LanguagesSupport.Language.BENGALI);
                    }
                } else {
                    isFirstTimeLanguageSelection = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rlAboutApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                AboutActivity.launch(getActivity());
                AboutActivity.setLicenseListener(new LicenseClickListener() {
                    @Override
                    public void onLicenseClick() {
                        Intent intentLicense = new Intent(getActivity(), LicenseActivity.class);
                        startActivity(intentLicense);
                    }
                });
            }
        });

        rlDownloadApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(getActivity(), DownloadAppActivity.class);
                startActivity(intent);
            }
        });

        rlOnlineSupport.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentOnlineSupport = new Intent(getActivity(), OnlineSupportActivity.class);
                startActivity(intentOnlineSupport);
            }
        });
    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AllConstants.INTENT_KEY_REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    refreshAccountView();
                    ((HomeActivity) getActivity()).refreshNavigationDrawerItems(NavigationId.SETTINGS);
                }
                break;
        }
    }

    @Override
    public void initFragmentUpdate(Object object) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setRightMenu(false, -1, null);
        ((HomeActivity) getActivity()).setLockMode(false);
    }

    private void refreshAccountView() {
        if (SessionUtil.getUser(getActivity()) != null) {
            llAccountLoggedIn.setVisibility(View.VISIBLE);
            llAccountLoggedOut.setVisibility(View.GONE);
        } else {
            llAccountLoggedIn.setVisibility(View.GONE);
            llAccountLoggedOut.setVisibility(View.VISIBLE);
        }
    }

    private void refreshNotificationView(){
        switchStopNotification.setIconEnabled(!FcmManager.getInstance().isAppNotificationStopped());
    }

    private void initLanguageSpinner() {
        languageSpinnerAdapter = new LanguageSpinnerAdapter(getActivity(), Language.values());
        spinnerLanguage.setAdapter(languageSpinnerAdapter);
        // For the very first time select language as per previous selection
        if ((LocaleManager.getSelectedLanguageId(getActivity()).equalsIgnoreCase(LanguagesSupport.Language.ENGLISH))) {
            spinnerLanguage.setSelection(1);
        } else if ((LocaleManager.getSelectedLanguageId(getActivity()).equalsIgnoreCase(LanguagesSupport.Language.BENGALI))) {
            spinnerLanguage.setSelection(2);
        } else {
            spinnerLanguage.setSelection(0);
        }
    }
}