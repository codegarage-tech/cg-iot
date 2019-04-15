package tech.codegarage.iot.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.jaeger.library.StatusBarUtil;

import io.armcha.ribble.presentation.navigationview.NavigationId;
import io.armcha.ribble.presentation.navigationview.NavigationItem;
import io.armcha.ribble.presentation.navigationview.adapter.NavigationViewAdapter;
import io.armcha.ribble.presentation.navigationview.adapter.RecyclerArrayAdapter;
import io.armcha.ribble.presentation.utils.extensions.ViewExKt;
import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;
import tech.codegarage.iot.R;
import tech.codegarage.iot.base.BaseLocationActivity;
import tech.codegarage.iot.fragment.LoginFragment;
import tech.codegarage.iot.fragment.OwnDevicesFragment;
import tech.codegarage.iot.fragment.ProductsFragment;
import tech.codegarage.iot.fragment.SettingsFragment;
import tech.codegarage.iot.geocoding.ReverseGeocoderTask;
import tech.codegarage.iot.geocoding.UserLocationAddress;
import tech.codegarage.iot.model.User;
import tech.codegarage.iot.util.AllConstants;
import tech.codegarage.iot.util.AppUtil;
import tech.codegarage.iot.util.DataUtil;
import tech.codegarage.iot.util.FragmentUtilsManager;
import tech.codegarage.iot.util.Logger;
import tech.codegarage.iot.util.SessionUtil;

import static tech.codegarage.iot.util.AllConstants.BUNDLE_KEY_MESSAGE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class HomeActivity extends BaseLocationActivity {

    //Navigation drawer view
    private String TRANSLATION_X_KEY = "TRANSLATION_X_KEY";
    private String CARD_ELEVATION_KEY = "CARD_ELEVATION_KEY";
    private String SCALE_KEY = "SCALE_KEY";
    private NavigationViewAdapter navViewAdapter;
    private RecyclerView navRecyclerView;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private CardView contentHome;
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
    public void onLocationFound(Location location) {
//        if (location != null) {
//
//            //Check internet connection
//            if (NetworkManager.isConnected(getActivity())) {
//                if (FragmentUtilsManager.getVisibleSupportFragment(HomeActivity.this, lastSelectedItem.getNavigationId().name()) instanceof ProductsFragment) {
//                    //Request reverse geocoding for address
//                    if ((currentLocationTask != null) && (currentLocationTask.getStatus() == AsyncTask.Status.RUNNING)) {
//                        currentLocationTask.cancel(true);
//                    }
//
//                    currentLocationTask = new ReverseGeocoderTask(getActivity(), new LocationAddressListener() {
//                        @Override
//                        public void getLocationAddress(UserLocationAddress locationAddress) {
//                            if (locationAddress != null) {
//                                mLocationAddress = locationAddress;
//                                Logger.d(TAG, "UserLocationAddress: " + mLocationAddress.toString());
////                                        String addressText = String.format("%s, %s, %s, %s", locationAddress.getStreetAddress(), locationAddress.getCity(), locationAddress.getState(), locationAddress.getCountry());
//
//                                //Set address to the toolbar
//                                setToolBarTitle(mLocationAddress.getAddressLine());
//                            }
//                        }
//                    });
//                    currentLocationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, location);
//                }
//            }
//        }
    }

    @Override
    public LOCATION_UPDATE_FREQUENCY initLocationUpdateFrequency() {
        return LOCATION_UPDATE_FREQUENCY.ONCE;
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
        //Set app user information
        updateUserInfo();
        initNavigationDrawer();
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
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
                dialogBuilder
                        .withTitle(getString(R.string.dialog_message))
                        .withMessage(getString(R.string.dialog_do_you_wanna_close_the_app))
                        .withEffect(Effectstype.Newspager)
                        .withDuration(700)
                        .withButton1Text(getString(R.string.dialog_cancel))
                        .withButton2Text(getString(R.string.dialog_ok))
                        .isCancelableOnTouchOutside(true)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Dismiss dialog
                                dialogBuilder.dismiss();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Dismiss dialog
                                dialogBuilder.dismiss();
                                finish();
                            }
                        })
                        .show();
            }
        }
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
            AppUtil.loadImage(getActivity(), userAvatar, mAppUser.getImage(), false, true, false);
            userName.setText(mAppUser.getName());
        } else {
            AppUtil.loadImage(getActivity(), userAvatar, R.mipmap.ic_launcher_round, false, false, false);
            userName.setText("IoT");
        }
    }

    private void doLogout() {
        SessionUtil.setUser(getActivity(), "");
        initNavigationDrawer();
    }

    /*****************************
     * Navigation drawer methods *
     *****************************/
    public void initNavigationDrawer() {
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
        if (SessionUtil.getUser(getActivity()) != null) {
            lastSelectedItem = navViewAdapter.selectNavigationItem(NavigationId.OWN_DEVICES);
            changeNavigationFragment(lastSelectedItem, new OwnDevicesFragment());
        } else {
            lastSelectedItem = navViewAdapter.selectNavigationItem(NavigationId.PRODUCTS);
            changeNavigationFragment(lastSelectedItem, new ProductsFragment());
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

    private void handleNavigationItemClick(final NavigationItem item) {
        if (lastSelectedItem != null && lastSelectedItem.getNavigationId() != item.getNavigationId()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (item.getNavigationId()) {
                        case PRODUCTS:
                            changeNavigationFragment(item, new ProductsFragment());
                            break;
                        case ADD_DEVICE:
                            startActivity(new Intent(getActivity(), AddDeviceActivity.class));
                            break;
                        case OWN_DEVICES:
                            changeNavigationFragment(item, new OwnDevicesFragment());
                            break;
                        case SETTINGS:
                            changeNavigationFragment(item, new SettingsFragment());
                            break;
                        case LOGIN:
                            changeNavigationFragment(item, new LoginFragment());
                            break;
                        case LOGOUT:
                            doLogout();
                            break;
                    }
                }
            }, AllConstants.NAVIGATION_DRAWER_CLOSE_DELAY);
        }

        //Close drawer for any type of navigation item click
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void changeNavigationFragment(NavigationItem item, Fragment fragment) {
        setToolBarTitle(item.getNavigationId().getValue());

        lastSelectedItem = item;
        navViewAdapter.selectNavigationItem(lastSelectedItem.getNavigationId());
        Logger.d(TAG, "Clicked navigation item: " + lastSelectedItem.getNavigationId().getValue());
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_MESSAGE, lastSelectedItem.getNavigationId().getValue());
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

    public void setRightMenu(boolean visibility, View.OnClickListener onClickListener) {
        rightMenu.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        rightMenu.setBackgroundResource(R.drawable.vector_plus_circular_bg_white);
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
}
