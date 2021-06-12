package com.meembusoft.iot.activity;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.badoualy.stepperindicator.StepperIndicator;
import com.jaeger.library.StatusBarUtil;
import com.meembusoft.iot.R;
import com.meembusoft.iot.adapter.AddDeviceViewPagerAdapterTest;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.base.BaseLocationActivity;
import com.meembusoft.iot.enumeration.AddDeviceScreenTypeTest;
import com.meembusoft.iot.fragment.ConnectDeviceFragment;
import com.meembusoft.iot.fragment.EnsureConnectivityFragment;
import com.meembusoft.iot.fragment.SelectProductFragmentTest;
import com.meembusoft.iot.fragment.SelectRoomFragmentTest;
import com.meembusoft.iot.geocoding.ReverseGeocoderTask;
import com.meembusoft.iot.geocoding.UserLocationAddress;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.util.SessionUtil;
import com.meembusoft.iot.view.LockableViewPager;
import com.reversecoder.library.event.OnSingleClickListener;

import java.util.List;

import io.armcha.ribble.presentation.widget.AnimatedTextView;

import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_REQUEST_CODE_LOGIN;
import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_REQUEST_CODE_SELECT_DEVICE;
import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_REQUEST_CODE_SELECT_WIFI;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AddDeviceActivityTest extends BaseActivity {

    //Toolbar
    private LinearLayout llLeftMenu, llRightMenu;
    private ImageView ivRightMenu;
    private AnimatedTextView toolbarTitle;

    private LockableViewPager vpAddDevice;
    private StepperIndicator stepperIndicator;

    private AddDeviceViewPagerAdapterTest addDeviceViewPagerAdapter;
    private LinearLayout llFooter, llTask;
    private Button btnTask;
    private ImageView btnPrevious, btnNext;

    //Background task
    private ReverseGeocoderTask currentLocationTask;
    private UserLocationAddress mLocationAddress;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_add_device_test;
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
        llLeftMenu = (LinearLayout) findViewById(R.id.ll_left_menu);
        llRightMenu = (LinearLayout) findViewById(R.id.ll_right_menu);
        ivRightMenu = (ImageView) findViewById(R.id.right_menu);
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbar_title);
        stepperIndicator = (StepperIndicator) findViewById(R.id.stepper_indicator_add_device);
        vpAddDevice = (LockableViewPager) findViewById(R.id.vp_add_device);
        llFooter = (LinearLayout) findViewById(R.id.ll_footer);
        btnPrevious = (ImageView) findViewById(R.id.btn_previous);
        llTask = (LinearLayout) findViewById(R.id.ll_task);
        btnTask = (Button) findViewById(R.id.btn_task);
        btnNext = (ImageView) findViewById(R.id.btn_next);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        toolbarTitle.setAnimatedText(getString(R.string.title_activity_add_device), 0L);
        AppUtil.applyMarqueeOnTextView(toolbarTitle);

        // Initialize suggested room data
//        if (!DataBaseManager.hasRoomInitialized(getActivity())) {
//            Logger.d(TAG, TAG + ">> Room data are not initialized");
//            DataBaseManager.initSuggestedRooms(getActivity());
//        } else {
//            Logger.d(TAG, TAG + ">> Room data has already initialized");
//        }

        // Setup viewpager fragments
        addDeviceViewPagerAdapter = new AddDeviceViewPagerAdapterTest(getSupportFragmentManager());
        vpAddDevice.setAdapter(addDeviceViewPagerAdapter);
        vpAddDevice.setSwipable(false);
        stepperIndicator.setViewPager(vpAddDevice, true);

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        llLeftMenu.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                initActivityBackPress();
            }
        });

        vpAddDevice.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //this variable is needed for the very first time viewpage fragment selection
            boolean first = true;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (first && positionOffset == 0 && positionOffsetPixels == 0) {
                    onPageSelected(0);
                    first = false;
                }
                Logger.d(TAG, "onPageScrolled: " + "position: " + position + "positionOffset: " + positionOffset + "positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Logger.d(TAG, "onPageSelected: " + "position: " + position);
                int lastPagePosition = addDeviceViewPagerAdapter.getCount() - 1;
                int firstPagePosition = 0;
                if (position == firstPagePosition) {
                    btnTask.setBackgroundResource(R.drawable.selector_plus_circle);
                    llTask.setVisibility(View.GONE);
                    btnPrevious.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                } else if (position == lastPagePosition) {
                    btnTask.setBackgroundResource(R.drawable.selector_tick_circle);
                    llTask.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                } else {
                    llTask.setVisibility(View.GONE);
                    btnPrevious.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }

                switch (addDeviceViewPagerAdapter.getScreenType(position)) {
                    case SELECT_ROOM:
                        setTitle(getString(R.string.title_activity_add_device));
//                        setRightMenu(true, R.drawable.vector_plus_circular_bg_white, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                showInputRoomDialog();
//                            }
//                        });
//                        llFooter.setBackgroundColor(getResources().getColor((R.color.colorShadeBlue)));
                        break;
//                    case CHOOSE_DEVICE:
//                        setTitle(getString(R.string.title_fragment_choose_device));
////                        setRightMenu(false, 0, null);
//                        break;
//                    case ATTACH_APPLIANCE:
//                        setTitle(getString(R.string.title_fragment_attach_appliance));
////                        setRightMenu(false, 0, null);
//                        break;
                    case ENSURE_CONNECTIVITY:
                        setTitle(getString(R.string.title_fragment_ensure_connectivity));
//                        setRightMenu(false, 0, null);
                        break;
                    case CONNECT_DEVICE:
                        setTitle(getString(R.string.title_fragment_connect_device));
                        SelectProductFragmentTest connectDeviceFragment = (SelectProductFragmentTest) addDeviceViewPagerAdapter.getRegisteredFragment(position);
                        if (connectDeviceFragment != null) {
                          //  connectDeviceFragment.startWifiScanTask();
                        }
                        break;
                    case PREVIEW_SCREEN:
                        setTitle(getString(R.string.title_fragment_preview));
//                        setRightMenu(false, 0, null);
                        break;
                }

//                if (position == lastPagePosition) {
//                    llFooter.setBackgroundColor(getResources().getColor((R.color.colorPrimaryDark)));
//                } else {
//                    llFooter.setBackgroundColor(getResources().getColor((android.R.color.transparent)));
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Logger.d(TAG, "onPageScrollStateChanged: " + "state: " + state);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInLastPage = vpAddDevice.getCurrentItem() == addDeviceViewPagerAdapter.getCount() - 1;
                if (!isInLastPage) {
                    if (isScreenVerified(vpAddDevice.getCurrentItem())) {
                        vpAddDevice.setCurrentItem(vpAddDevice.getCurrentItem() + 1);
                    }
                }
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFirstPage = vpAddDevice.getCurrentItem() == 0;
                if (!isFirstPage) {
                    // Stop wifi scanner if running
                    if (addDeviceViewPagerAdapter.getScreenType(vpAddDevice.getCurrentItem()) == AddDeviceScreenTypeTest.CONNECT_DEVICE) {
                        SelectProductFragmentTest connectDeviceFragment = (SelectProductFragmentTest) addDeviceViewPagerAdapter.getRegisteredFragment(vpAddDevice.getCurrentItem());
                        if (connectDeviceFragment != null) {
                           // connectDeviceFragment.destroyTask();
                        }
                    }

                    // Go to previous fragment
                    vpAddDevice.setCurrentItem(vpAddDevice.getCurrentItem() - 1);
                }
            }
        });
        btnTask.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                boolean isFirstPage = vpAddDevice.getCurrentItem() == 0;
                boolean isInLastPage = vpAddDevice.getCurrentItem() == addDeviceViewPagerAdapter.getCount() - 1;
                if (isFirstPage) {
                } else if (isInLastPage) {

                }
            }
        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_KEY_REQUEST_CODE_SELECT_DEVICE:
                if (data != null) {
                    Logger.d(TAG, TAG + " >>> " + "INTENT_KEY_REQUEST_CODE_SELECT_DEVICE: " );
                    openFragmentOnActivityResult(requestCode, resultCode, data);
                }
                break;

            case INTENT_KEY_REQUEST_CODE_SELECT_WIFI:
                if (data != null) {
                    Logger.d(TAG, TAG + " >>> " + "INTENT_KEY_REQUEST_CODE_SELECT_WIFI: " );
                    openFragmentOnActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }

    @Override
    public void initActivityBackPress() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            SessionUtil.setTempSelectedWifi(getActivity(), "");
            finish();
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void openFragmentOnActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof SelectProductFragmentTest) {
                    ((SelectProductFragmentTest) fragment).initFragmentOnResult(requestCode, resultCode, data);

                } else if (fragment instanceof EnsureConnectivityFragment) {
                    ((EnsureConnectivityFragment) fragment).initFragmentOnResult(requestCode, resultCode, data);

                }
            }
        }
    }
    private boolean isScreenVerified(int position) {
        Logger.d(TAG, TAG + ">>" + "position is " + position);
        switch (addDeviceViewPagerAdapter.getScreenType(position)) {
            case SELECT_ROOM:
                SelectRoomFragmentTest selectRoomFragment = (SelectRoomFragmentTest) addDeviceViewPagerAdapter.getRegisteredFragment(position);
                if (selectRoomFragment != null) {
                    if (selectRoomFragment.isAllFieldsVerified()) {
                        Logger.d(TAG, TAG + ">>" + "room selection is verified.");
                        return true;
                    } else {
                        Logger.d(TAG, TAG + ">>" + "room selection is not verified.");
                    }
                } else {
                    Logger.d(TAG, TAG + ">>" + "room fragment is null.");
                }
                return false;
            case CONNECT_DEVICE:
                SelectProductFragmentTest selectProductFragment = (SelectProductFragmentTest) addDeviceViewPagerAdapter.getRegisteredFragment(position);
                if (selectProductFragment != null) {
                    if (selectProductFragment.isAllFieldsVerified()) {
                        Logger.d(TAG, TAG + ">>" + "picking device is verified.");
                        return true;
                    } else {
                        Logger.d(TAG, TAG + ">>" + "picking device is not verified.");
                    }
                } else {
                    Logger.d(TAG, TAG + ">>" + "choose device fragment is null.");
                }
                return false;
//            case ATTACH_APPLIANCE:
//                return true;
            case ENSURE_CONNECTIVITY:
                EnsureConnectivityFragment ensureConnectivityFragment = (EnsureConnectivityFragment) addDeviceViewPagerAdapter.getRegisteredFragment(position);
                if (ensureConnectivityFragment != null) {
                    if (ensureConnectivityFragment.isAllFieldsVerified()) {
                        Logger.d(TAG, TAG + ">>" + "ensure connectivity is verified.");
                        return true;
                    } else {
                        Logger.d(TAG, TAG + ">>" + "ensure connectivity is not verified.");
                    }
                } else {
                    Logger.d(TAG, TAG + ">>" + "ensure connectivity fragment is null.");
                }
                return false;

        }
        return false;
    }

    public void setRightMenu(boolean visible, int resDrawableId, View.OnClickListener onClickListener) {
        ivRightMenu.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        ivRightMenu.setBackgroundResource(resDrawableId);
        ivRightMenu.setOnClickListener(onClickListener);
    }

    public void setTitle(String title) {
        toolbarTitle.setAnimatedText(title, 0L);
    }
}