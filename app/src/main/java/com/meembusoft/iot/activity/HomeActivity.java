package com.meembusoft.iot.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eggheadgames.aboutbox.AboutConfig;
import com.eggheadgames.aboutbox.IAnalytic;
import com.eggheadgames.aboutbox.IDialog;
import com.google.android.material.navigation.NavigationView;
import com.jaeger.library.StatusBarUtil;
import com.meembusoft.fcmmanager.util.OnTokenUpdateListener;
import com.meembusoft.fcmmanager.util.TokenFetcher;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.fragment.CategoryWiseProductFragment;
import com.meembusoft.iot.fragment.DashboardFragment;
import com.meembusoft.iot.fragment.SettingsFragment;
import com.meembusoft.iot.geocoding.ReverseGeocoderTask;
import com.meembusoft.iot.geocoding.UserLocationAddress;
import com.meembusoft.iot.model.User;
import com.meembusoft.iot.util.AllConstants;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.DataUtil;
import com.meembusoft.iot.util.FragmentUtilsManager;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.util.SessionUtil;
import com.reversecoder.library.network.NetworkManager;

import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.dialog.PopupDialog;
import io.armcha.ribble.presentation.navigationview.NavigationId;
import io.armcha.ribble.presentation.navigationview.NavigationItem;
import io.armcha.ribble.presentation.navigationview.adapter.NavigationViewAdapter;
import io.armcha.ribble.presentation.navigationview.adapter.RecyclerArrayAdapter;
import io.armcha.ribble.presentation.utils.extensions.ViewExKt;
import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;

import static com.meembusoft.iot.util.AllConstants.BUNDLE_KEY_MESSAGE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class HomeActivity extends BaseActivity {

    //Navigation drawer view
    private String TRANSLATION_X_KEY = "TRANSLATION_X_KEY";
    private String CARD_ELEVATION_KEY = "CARD_ELEVATION_KEY";
    private String SCALE_KEY = "SCALE_KEY";
    private String CURRENT_SCREEN = "CURRENT_SCREEN";
    private NavigationViewAdapter navViewAdapter;
    private RecyclerView navRecyclerView;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private CardView contentHome;

    // Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;
    private NavigationItem lastSelectedItem;

    //Header view
    private ImageView userAvatar;
    private TextView userName;
    private TextView userInfo;

    //Background task
    private ReverseGeocoderTask currentLocationTask;
    private UserLocationAddress mLocationAddress;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_home;
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

        navView = (NavigationView) findViewById(R.id.navigation_view);
        navRecyclerView = (RecyclerView) navView.findViewById(R.id.navigation_view_recycler_view);
        userAvatar = (ImageView) navView.findViewById(R.id.userAvatar);
        userName = (TextView) navView.findViewById(R.id.userName);
        userInfo = (TextView) navView.findViewById(R.id.userInfo);
        contentHome = (CardView) findViewById(R.id.mainView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        initNavigationDrawer(savedInstanceState);
        initFCM();
        initAboutPage();
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
//                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
//                dialogBuilder
//                        .withTitle(getString(R.string.dialog_message))
//                        .withMessage(getString(R.string.dialog_do_you_wanna_close_the_app))
//                        .withEffect(Effectstype.Newspager)
//                        .withDuration(700)
//                        .withButton1Text(getString(R.string.dialog_cancel))
//                        .withButton2Text(getString(R.string.dialog_ok))
//                        .isCancelableOnTouchOutside(true)
//                        .setButton1Click(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //Dismiss dialog
//                                dialogBuilder.dismiss();
//                            }
//                        })
//                        .setButton2Click(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //Dismiss dialog
//                                dialogBuilder.dismiss();
//                                finish();
//                            }
//                        })
//                        .show();

                //Close app dialog
                showAppCloseDialog(getResources().getString(R.string.dialog_close_app_title), getResources().getString(R.string.dialog_do_you_want_to_close_the_app));
//                CloseAppDialog closeAppDialog = new CloseAppDialog(getActivity(), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case DialogInterface.BUTTON_POSITIVE:
//                                finish();
//                                break;
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                break;
//                            case DialogInterface.BUTTON_NEUTRAL:
//                                break;
//                        }
//                    }
//                });
//                closeAppDialog.initView().show();
            }
        }
    }

    private void showAppCloseDialog(String title, String message) {
        PopupDialog.create(getActivity())
                .outsideTouchHide(false)
                .dismissTime(1000 * 5)
                .controller(AlertController.build()
                        .title(title + "\n")
                        .message(message)
                        .clickDismiss(true)
                        .negativeButton(getString(R.string.dialog_cancel), null)
                        .positiveButton(getString(R.string.dialog_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }))
                .show();
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissProgressDialog();

        if (currentLocationTask != null && currentLocationTask.getStatus() == AsyncTask.Status.RUNNING) {
            currentLocationTask.cancel(true);
        }
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //Update app user information
        updateUserInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void updateUserInfo() {
        User mAppUser = SessionUtil.getUser(getActivity());
        if (mAppUser != null) {
            Logger.d(TAG, TAG + " >>> " + "mAppUser: " + mAppUser.toString());
            AppUtil.loadImage(getActivity(), userAvatar, mAppUser.getUser_image(), false, true, false);
            userName.setText(mAppUser.getUser_name());
            userInfo.setVisibility(View.GONE);
        } else {
            AppUtil.loadImage(getActivity(), userAvatar, R.mipmap.ic_launcher, false, false, false);
            userName.setText(getString(R.string.app_name_navigation));
            userInfo.setVisibility(View.VISIBLE);
            userInfo.setText(getString(R.string.txt_version) + ": " + AppUtil.getApplicationVersion(getActivity()));
        }
        // Set white tint of the avatar
        userAvatar.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorWhite));
    }

    /*****************************
     * Navigation drawer methods *
     *****************************/
    public void initNavigationDrawer(Bundle savedInstanceState) {
        //Initialize navigation menu
        navViewAdapter = new NavigationViewAdapter(getActivity());
        navViewAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                handleNavigationItemClick(navViewAdapter.getItem(position));
            }
        });
        navRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        navRecyclerView.setAdapter(navViewAdapter);
        navRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        navViewAdapter.addAll(DataUtil.getUserMenu(getActivity()));
        //For the very first time when activity is launched, need to initialize main fragment
        if (savedInstanceState == null) {
            if (SessionUtil.getUser(getActivity()) != null) {
                lastSelectedItem = navViewAdapter.selectNavigationItem(NavigationId.DASHBOARD);
                changeNavigationFragment(lastSelectedItem, new DashboardFragment());
            } else {
                lastSelectedItem = navViewAdapter.selectNavigationItem(NavigationId.PRODUCTS);
                changeNavigationFragment(lastSelectedItem, new CategoryWiseProductFragment());
            }
        } else {
            // When activity is recreated, such as language changes
            String currentScreen = savedInstanceState.getString(CURRENT_SCREEN);
            if (currentScreen.equalsIgnoreCase(SettingsFragment.class.getSimpleName())) {
                lastSelectedItem = navViewAdapter.selectNavigationItem(NavigationId.SETTINGS);
                changeNavigationFragment(lastSelectedItem, new SettingsFragment());
            }

            // Update user information
            updateUserInfo();
        }

        //Initialize drawer
        drawerLayout.setDrawerElevation(0f);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float moveFactor = navView.getWidth() * slideOffset;
                contentHome.setTranslationX(moveFactor);
                ViewExKt.setScale(contentHome, 1 - slideOffset / 4);
                contentHome.setCardElevation(slideOffset * AppUtil.toPx(HomeActivity.this, 10));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setLeftMenu(false);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setLeftMenu(true);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);

                if (newState == DrawerLayout.STATE_SETTLING) {
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        // Drawer started opening
                    } else {
                        // Drawer started closing
                    }
                }
            }
        });

        //Initialize menu action
        setLeftMenu(true);
    }

    public void doLogout(NavigationId selectedNavigationId) {
        SessionUtil.setUser(getActivity(), "");
        refreshNavigationDrawerItems(selectedNavigationId);
    }

    public void refreshNavigationDrawerItems(NavigationId selectedItemId) {
        updateUserInfo();
        if (navViewAdapter != null) {
            navViewAdapter.clear();
            navViewAdapter.addAll(DataUtil.getUserMenu(getActivity()));

            // Reset selected item
            lastSelectedItem = navViewAdapter.selectNavigationItem(selectedItemId);
        }
    }

    private void handleNavigationItemClick(final NavigationItem item) {
        if (lastSelectedItem != null && lastSelectedItem.getNavigationId() != item.getNavigationId()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (item.getNavigationId()) {
                        case PRODUCTS:
                            changeNavigationFragment(item, new CategoryWiseProductFragment());
                            break;
                        case ADD_DEVICE:
                            startActivity(new Intent(getActivity(), AddDeviceActivityTest.class));
                            break;
                        case DASHBOARD:
                            changeNavigationFragment(item, new DashboardFragment());
                            break;
                        case SETTINGS:
                            changeNavigationFragment(item, new SettingsFragment());
                            break;
                        case LOGOUT:
                            doLogout(NavigationId.PRODUCTS);
                            lastSelectedItem = navViewAdapter.selectNavigationItem(NavigationId.PRODUCTS);
                            changeNavigationFragment(lastSelectedItem, new CategoryWiseProductFragment());
                            break;
                    }
                }
            }, AllConstants.NAVIGATION_DRAWER_CLOSE_DELAY);
        }

        //Close drawer for any type of navigation item click
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void changeNavigationFragment(NavigationItem item, Fragment fragment) {
        setToolBarTitle(item.getNavigationId().getLabel(getActivity()));

        lastSelectedItem = item;
        navViewAdapter.selectNavigationItem(lastSelectedItem.getNavigationId());
        Logger.d(TAG, "Clicked navigation item: " + lastSelectedItem.getNavigationId().toString());
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_MESSAGE, lastSelectedItem.getNavigationId().toString());
        fragment.setArguments(bundle);
        FragmentUtilsManager.changeSupportFragment(HomeActivity.this, fragment);
    }

    public void changeFragment(Fragment fragment, String title) {
        setToolBarTitle(title);
        setLeftMenu(false);
        FragmentUtilsManager.changeSupportFragmentWithAnim(HomeActivity.this, fragment, true, false, FragmentUtilsManager.TransitionType.SlideHorizontal);
    }

    public void setLeftMenu(boolean isHamburger) {
        if (isHamburger) {
            leftMenu.setAnimatedImage(R.drawable.hamb, 0L);
            leftMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        } else {
            leftMenu.setAnimatedImage(R.drawable.arrow_left, 0L);
            leftMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initActivityBackPress();
                }
            });
        }
    }

    public void setRightMenu(boolean visibility, int resId, View.OnClickListener onClickListener) {
        rightMenu.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        if (resId > 0) {
            rightMenu.setBackgroundResource(resId);
        }
        rightMenu.setOnClickListener(onClickListener);
    }

    public void setToolBarTitle(String title) {
        toolbarTitle.setAnimatedText(title, 0L);

        //For marquee address
        AppUtil.applyMarqueeOnTextView(toolbarTitle);
    }

    public void setLockMode(boolean isLocked) {
        drawerLayout.setDrawerLockMode(isLocked ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putFloat(TRANSLATION_X_KEY, contentHome.getTranslationX());
            outState.putFloat(CARD_ELEVATION_KEY, ViewExKt.getScale(contentHome));
            outState.putFloat(SCALE_KEY, contentHome.getCardElevation());
            outState.putString(CURRENT_SCREEN, getSupportFragmentManager().findFragmentById(R.id.container).getClass().getSimpleName());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if (savedState != null) {
            contentHome.setTranslationX(savedState.getFloat(TRANSLATION_X_KEY));
            ViewExKt.setScale(contentHome, savedState.getFloat(CARD_ELEVATION_KEY));
            contentHome.setCardElevation(savedState.getFloat(SCALE_KEY));
        }
    }

    /***************
     * FCM methods *
     ***************/
    private void initFCM() {
        if (NetworkManager.isConnected(getActivity())) {
            TokenFetcher tokenFetcher = new TokenFetcher(getActivity(), new OnTokenUpdateListener() {
                @Override
                public void onTokenUpdate(String update) {

                }
            });
            tokenFetcher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    /**************************
     * Methods for about page *
     **************************/
    private void initAboutPage() {
        AboutConfig aboutConfig = AboutConfig.getInstance();
        aboutConfig.appName = getString(R.string.app_name);
        aboutConfig.appIcon = R.mipmap.ic_launcher;
        aboutConfig.version = AppUtil.getApplicationVersion(getActivity());
        aboutConfig.author = getString(R.string.app_author);
        aboutConfig.aboutLabelTitle = getString(R.string.mal_title_about);
        aboutConfig.packageName = getApplicationContext().getPackageName();
        aboutConfig.buildType = AboutConfig.BuildType.GOOGLE;

        aboutConfig.facebookUserName = getString(R.string.app_publisher_facebook_id);
        aboutConfig.twitterUserName = getString(R.string.app_publisher_twitter_id);
        aboutConfig.webHomePage = getString(R.string.app_publisher_profile_website);

        // app publisher for "Try Other Apps" item
        aboutConfig.appPublisherId = getString(R.string.app_publisher_id);

        // if pages are stored locally, then you need to override aboutConfig.dialog to be able use custom WebView
        aboutConfig.companyHtmlPath = getString(R.string.app_publisher_company_html_path);
        aboutConfig.privacyHtmlPath = getString(R.string.app_publisher_privacy_html_path);
        aboutConfig.acknowledgmentHtmlPath = getString(R.string.app_publisher_acknowledgment_html_path);

        aboutConfig.dialog = new IDialog() {
            @Override
            public void open(AppCompatActivity appCompatActivity, String url, String tag) {
                // handle custom implementations of WebView. It will be called when user click to web items. (Example: "Privacy", "Acknowledgments" and "About")
            }
        };

        aboutConfig.analytics = new IAnalytic() {
            @Override
            public void logUiEvent(String s, String s1) {
                // handle log events.
            }

            @Override
            public void logException(Exception e, boolean b) {
                // handle exception events.
            }
        };
        // set it only if aboutConfig.analytics is defined.
        aboutConfig.logUiEventName = "Log";

        // Contact Support email details
        aboutConfig.emailAddress = getString(R.string.app_author_email);
        aboutConfig.emailSubject = "[" + getString(R.string.app_name) + "]" + "[" + AppUtil.getApplicationVersion(getActivity()) + "]" + " " + getString(R.string.app_contact_subject);
        aboutConfig.emailBody = getString(R.string.app_contact_body);
    }
}