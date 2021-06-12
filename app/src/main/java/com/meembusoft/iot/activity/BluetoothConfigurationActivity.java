package com.meembusoft.iot.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jaeger.library.StatusBarUtil;
import com.jem.fliptabs.FlipTab;
import com.meembusoft.animationmanager.recyclerview.ItemAddAnimation;
import com.meembusoft.iot.R;
import com.meembusoft.iot.adapter.BluetoothDeviceAdapter;
import com.meembusoft.iot.base.BaseLocationActivity;
import com.meembusoft.iot.bluetooth.BluetoothHandler;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.armcha.ribble.presentation.widget.AnimatedTextView;

import static com.meembusoft.iot.bluetooth.BluetoothHandler.REQUEST_CODE_TURN_ON_BLUETOOTH;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class BluetoothConfigurationActivity extends BaseLocationActivity {

    //Toolbar
    private LinearLayout llLeftMenu, llRightMenu;
    private ImageView ivRightMenu;
    private AnimatedTextView toolbarTitle;

    //    private Button btnTurnOn, btnTurnOff, btnScanStart, btnScanStop;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvDevices;
    private BluetoothDeviceAdapter mBluetoothDeviceAdapter;
    // Flip tab
    private FlipTab flipTabBluetooth;
    private List<BluetoothDevice> mScanResults = new ArrayList<>();

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
//                if (FragmentUtilsManager.getVisibleSupportFragment(HomeActivity.this, lastSelectedItem.getNavigationId().name()) instanceof CategoryWiseProductFragment) {
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
        return R.layout.activity_bluetooth_configure;
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

        flipTabBluetooth = (FlipTab) findViewById(R.id.fliptab_bluetooth);
//        btnTurnOn = (Button) findViewById(R.id.btnTurnOn);
//        btnTurnOff = (Button) findViewById(R.id.btnTurnOff);
//        btnScanStart = (Button) findViewById(R.id.btnScanStart);
//        btnScanStop = (Button) findViewById(R.id.btnScanStop);
        rvDevices = (RecyclerView) findViewById(R.id.rvDevices);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        toolbarTitle.setAnimatedText(getString(R.string.title_activity_bluetooth_configuration), 0L);
        AppUtil.applyMarqueeOnTextView(toolbarTitle);

        flipTabBluetooth.selectLeftTab(true);

        mBluetoothDeviceAdapter = new BluetoothDeviceAdapter(getActivity());
        rvDevices.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvDevices.setAdapter(mBluetoothDeviceAdapter);
        rvDevices.setItemAnimator(new ItemAddAnimation());

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        llLeftMenu.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                initActivityBackPress();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Disable swipe to refresh while refreshing
                swipeRefreshLayout.setRefreshing(true);

                Logger.d(TAG, "Scanning is started from >> onRefresh");
                startScanning();
            }
        });

        flipTabBluetooth.setTabSelectedListener(new FlipTab.TabSelectedListener() {
            @Override
            public void onTabSelected(boolean isLeftTab, @NotNull String tabTextValue) {
                mBluetoothDeviceAdapter.clear();
                for (BluetoothDevice bluetoothDevice : mScanResults) {
                    addBluetoothDevice(bluetoothDevice, flipTabBluetooth.isLeftSelected());
                }
            }

            @Override
            public void onTabReselected(boolean isLeftTab, @NotNull String tabTextValue) {

            }
        });

//        btnTurnOn.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                BluetoothHandler.getInstance().turnOnBluetooth(getActivity());
//            }
//        });
//        btnTurnOff.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                BluetoothHandler.getInstance().turnOffBluetooth();
//            }
//        });
//        btnScanStart.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                // Stop scanning
//                BluetoothHandler.getInstance().stopScanning();
//                // Clear previous data
//                mScanResults.clear();
//                mBluetoothDeviceAdapter.clear();
//                // Start scanning
//                BluetoothHandler.getInstance().startScanning(getActivity());
//            }
//        });
//        btnScanStop.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                BluetoothHandler.getInstance().stopScanning();
//            }
//        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, "initActivityOnResult>> requestCode: " + requestCode + " resultCode: " + resultCode);

        switch (requestCode) {
            case REQUEST_CODE_TURN_ON_BLUETOOTH:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getActivity(), getString(R.string.txt_bluetooth_is_enabled), Toast.LENGTH_SHORT).show();
                    Logger.d(TAG, "Scanning is started from >> initActivityOnResult>>REQUEST_CODE_TURN_ON_BLUETOOTH");
                    startScanning();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getActivity(), getString(R.string.txt_you_need_enable_bluetooth_for_serial_communication), Toast.LENGTH_SHORT).show();
                    initActivityBackPress();
                }
                break;
            case REQUEST_CODE_LOCATION_SETTINGS_PERMISSION:
                if (resultCode == RESULT_OK) {
                    if (BluetoothHandler.getInstance().isBluetoothEnabled()) {
                        Logger.d(TAG, "Scanning is started from >> initActivityOnResult>>REQUEST_CODE_LOCATION_SETTINGS_PERMISSION");
                        startScanning();
                    } else {
                        BluetoothHandler.getInstance().turnOnBluetooth(getActivity());
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getActivity(), getString(R.string.txt_you_need_enable_location_for_bluetooth_searching), Toast.LENGTH_SHORT).show();
                    initActivityBackPress();
                }
                break;
        }
    }

    @Override
    public void initActivityBackPress() {
        finish();
    }

    @Override
    public void initActivityDestroyTasks() {

    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onResume() {
        super.onResume();

        BluetoothHandler.getInstance().registerReceiver(getActivity(), mBluetoothBroadcastReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();

        BluetoothHandler.getInstance().unRegisterReceiver(getActivity(), mBluetoothBroadcastReceiver);
        BluetoothHandler.getInstance().stopScanning();
    }

    private final BroadcastReceiver mBluetoothBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.d(TAG, "mBluetoothBroadcastReceiver>>onReceive>> action: " + action);

            switch (action) {
                // This case is for manual on/off
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    if (state == BluetoothAdapter.STATE_ON) {
                        Toast.makeText(getActivity(), getString(R.string.txt_bluetooth_is_enabled), Toast.LENGTH_SHORT).show();
                    } else if (state == BluetoothAdapter.STATE_OFF) {
                        Toast.makeText(getActivity(), getString(R.string.txt_bluetooth_is_disabled), Toast.LENGTH_SHORT).show();
                    }
                    break;
//                case BluetoothDevice.ACTION_ACL_CONNECTED:
//                    Toast.makeText(getActivity(), getString(R.string.txt_connected), Toast.LENGTH_SHORT).show();
//                    break;
//                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
//
//                    break;
//                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
//                    Toast.makeText(getActivity(), getString(R.string.txt_disconnected), Toast.LENGTH_SHORT).show();
//                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Toast.makeText(getActivity(), getString(R.string.txt_bluetooth_scanning_is_started), Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
//                    BluetoothHandler.getInstance().dismissProgressDialog();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), getString(R.string.txt_bluetooth_scanning_is_finished), Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    Object object = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (object != null) {
                        BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (device != null) {
                            Logger.d(TAG, "mBluetoothBroadcastReceiver>>onReceive>> device: " + device.getName() + " paired: " + (device.getBondState() == BluetoothDevice.BOND_BONDED));
                            mScanResults.add(device);
                            addBluetoothDevice(device, flipTabBluetooth.isLeftSelected());
                        }
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (mDevice != null) {
                        Logger.d(TAG, "mBluetoothBroadcastReceiver>>onReceive>> mDevice: " + mDevice.toString());
                    }

                    // Check bonding state
                    int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                    int prevBondState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                    if (bondState == BluetoothDevice.BOND_BONDED && prevBondState == BluetoothDevice.BOND_BONDING) {
                        // After paired remove from available list and add to paired list
                        if (mBluetoothDeviceAdapter != null) {
                            if (flipTabBluetooth.isLeftSelected()) {
                                mBluetoothDeviceAdapter.addDevice(mDevice);
                            } else {
                                mBluetoothDeviceAdapter.removeItem(mDevice);
                            }
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.txt_paired), Toast.LENGTH_SHORT).show();
                    } else if (bondState == BluetoothDevice.BOND_NONE && prevBondState == BluetoothDevice.BOND_BONDED) {
                        // After unpaired remove from paired list and add to available list
                        if (mBluetoothDeviceAdapter != null) {
                            if (!flipTabBluetooth.isLeftSelected()) {
                                mBluetoothDeviceAdapter.addDevice(mDevice);
                            } else {
                                mBluetoothDeviceAdapter.removeItem(mDevice);
                            }
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.txt_unpaired), Toast.LENGTH_SHORT).show();
                    }

                    // Refresh list
                    dismissProgressDialog();
                    mBluetoothDeviceAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void addBluetoothDevice(BluetoothDevice bluetoothDevice, boolean isLeftTab) {
        if (bluetoothDevice != null && mBluetoothDeviceAdapter != null) {
            if (isLeftTab) {
                if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    mBluetoothDeviceAdapter.add(bluetoothDevice);
                }
            } else {
                if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mBluetoothDeviceAdapter.add(bluetoothDevice);
                }
            }
        }
    }

    private void startScanning() {
        // Stop scanning
        BluetoothHandler.getInstance().stopScanning();
        // Clear previous data
        mScanResults.clear();
        mBluetoothDeviceAdapter.clear();
        // Start scanning
        BluetoothHandler.getInstance().startScanning(getActivity());
    }
}