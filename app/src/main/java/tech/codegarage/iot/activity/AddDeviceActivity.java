package tech.codegarage.iot.activity;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;
import com.reversecoder.library.event.OnSingleClickListener;

import io.armcha.ribble.presentation.widget.AnimatedTextView;
import tech.codegarage.iot.R;
import tech.codegarage.iot.adapter.AddDeviceViewPagerAdapter;
import tech.codegarage.iot.base.BaseLocationActivity;
import tech.codegarage.iot.fragment.SelectRoomFragment;
import tech.codegarage.iot.geocoding.ReverseGeocoderTask;
import tech.codegarage.iot.geocoding.UserLocationAddress;
import tech.codegarage.iot.util.AppUtil;
import tech.codegarage.iot.util.Logger;
import tech.codegarage.iot.view.LockableViewPager;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AddDeviceActivity extends BaseLocationActivity {

    //Toolbar
    private LinearLayout llLeftMenu, llRightMenu;
    private ImageView ivRightMenu;
    private AnimatedTextView toolbarTitle;

    private LockableViewPager vpAddDevice;
    private AddDeviceViewPagerAdapter addDeviceViewPagerAdapter;
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
        return R.layout.activity_add_device;
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

        addDeviceViewPagerAdapter = new AddDeviceViewPagerAdapter(getSupportFragmentManager());
        vpAddDevice.setAdapter(addDeviceViewPagerAdapter);
        vpAddDevice.setSwipable(false);
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
                        setTitle(getString(R.string.title_fragment_select_room));
//                        setRightMenu(true, R.drawable.vector_plus_circular_bg_white, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                showInputRoomDialog();
//                            }
//                        });
//                        llFooter.setBackgroundColor(getResources().getColor((R.color.colorShadeBlue)));
                        break;
                    case CHOOSE_DEVICE:
                        setTitle(getString(R.string.title_fragment_choose_device));
//                        setRightMenu(false, 0, null);
                        break;
                    case ATTACH_APPLIANCE:
                        setTitle(getString(R.string.title_fragment_attach_appliance));
//                        setRightMenu(false, 0, null);
                        break;
                    case ENSURE_CONNECTIVITY:
                        setTitle(getString(R.string.title_fragment_ensure_connectivity));
//                        setRightMenu(false, 0, null);
                        break;
                    case CONNECT_DEVICE:
                        setTitle(getString(R.string.title_fragment_connect_device));
//                        setRightMenu(false, 0, null);
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

    private boolean isScreenVerified(int position) {
        Logger.d(TAG, TAG + ">>" + "position is " + position);
        switch (addDeviceViewPagerAdapter.getScreenType(position)) {
            case SELECT_ROOM:
                SelectRoomFragment selectRoomFragment = (SelectRoomFragment) addDeviceViewPagerAdapter.getRegisteredFragment(position);
                if (selectRoomFragment != null) {
                    if (selectRoomFragment.isAllFieldsVerified()) {
                        Logger.d(TAG, TAG + ">>" + "food items are verified.");

                        return true;
                    } else {
                        Logger.d(TAG, TAG + ">>" + "food items are not verified.");
                    }
                } else {
                    Logger.d(TAG, TAG + ">>" + "food fragment is null.");
                }
                return false;
            case CHOOSE_DEVICE:
                return true;
            case ATTACH_APPLIANCE:
                return true;
            case ENSURE_CONNECTIVITY:
                return true;
            case CONNECT_DEVICE:
                return true;
            case PREVIEW_SCREEN:
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