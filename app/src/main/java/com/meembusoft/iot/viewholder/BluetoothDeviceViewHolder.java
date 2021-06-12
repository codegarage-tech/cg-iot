package com.meembusoft.iot.viewholder;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseUpdateListener;
import com.meembusoft.iot.bluetooth.BTSendReceiveThread;
import com.meembusoft.iot.bluetooth.BluetoothConnector;
import com.meembusoft.iot.bluetooth.BluetoothHandler;
import com.meembusoft.iot.bluetooth.PairRunnable;
import com.meembusoft.iot.bluetooth.UnpairRunnable;
import com.meembusoft.iot.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;


public class BluetoothDeviceViewHolder extends BaseViewHolder<BluetoothDevice> {

    private String TAG = BluetoothDeviceViewHolder.class.getSimpleName();
    private TextView tvDeviceName, tvDeviceAddress, tvDeviceStatus;
    private ImageView ivUnpair;
    //
    private static final int MAX_NAME_CHECKS = 3;
    private static final int NAME_CHECK_PERIOD = 1000;
    private int nameChecks;
//
//    private String lastSent = "";
//    private BTSendReceiveThread btsendrec;

    public BluetoothDeviceViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.row_bluetooth_device);
        tvDeviceName = $(R.id.tv_bluetooth_name);
        tvDeviceAddress = $(R.id.tv_bluetooth_address);
        tvDeviceStatus = $(R.id.tv_bluetooth_status);
        ivUnpair = $(R.id.iv_unpair);
    }

    @Override
    public void setData(final BluetoothDevice data) {

//        tvDeviceName.setText(data.getName());
        resolveName(data, tvDeviceName, getContext());
        tvDeviceAddress.setText(data.getAddress());
        if ((data.getBondState() == BluetoothDevice.BOND_BONDED) || (data.getBondState() == BluetoothDevice.BOND_NONE)) {
            tvDeviceStatus.setVisibility(View.INVISIBLE);
        } else {
            tvDeviceStatus.setVisibility(View.VISIBLE);
        }
        if (data.getBondState() == BluetoothDevice.BOND_BONDED) {
            ivUnpair.setVisibility(View.VISIBLE);
        } else {
            ivUnpair.setVisibility(View.INVISIBLE);
        }

        ivUnpair.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (data.getBondState() == BluetoothDevice.BOND_BONDED) {
                    tvDeviceStatus.setVisibility(View.VISIBLE);
                    tvDeviceStatus.setText("Unpairing...");
                    Thread mUnpairThread = new Thread(new UnpairRunnable(data));
                    mUnpairThread.start();
                }
            }
        });

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                try {
                    Logger.d(TAG, "btnPairDevice>>setOnClickListener>> data: " + data.toString());
                    if (data.getBondState() == BluetoothDevice.BOND_BONDED) {
                        tvDeviceStatus.setVisibility(View.VISIBLE);
                        tvDeviceStatus.setText("Connecting...");

//                        connectBluetooth(data, getContext());
                        BluetoothConnector bluetoothConnector = new BluetoothConnector(data, false, BluetoothAdapter.getDefaultAdapter(), null);
                        bluetoothConnector.connect();
                    } else {
                        tvDeviceStatus.setVisibility(View.VISIBLE);
                        tvDeviceStatus.setText("Pairing...");
                        Thread mPairThread = new Thread(new PairRunnable(data));
                        mPairThread.start();
                    }
                } catch (Exception e) {
                    Logger.d(TAG, "btnPairDevice>>setOnClickListener>> exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

//        if ((data.getBondState() == BluetoothDevice.BOND_BONDED)) {
//            btnPairDevice.setText("Unpair");
//        } else {
//            btnPairDevice.setText("Pair");
//        }
//
//        if (BluetoothHandler.getInstance().getBluetoothSocket() == null) {
//            btnConnect.setText("Connect");
//        } else {
//            btnConnect.setText("Disconnect");
//        }
//
//        if (TextUtils.isEmpty(lastSent) || lastSent.equalsIgnoreCase("0")) {
//            btnLight.setText("On");
//        } else{
//            btnLight.setText("Off");
//        }
//
//        btnPairDevice.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                try {
//                    Logger.d(TAG, "btnPairDevice>>setOnClickListener>> data: " + data.toString());
////                    ((BluetoothActivity)mContext).showProgressDialog();
//                    if ((data.getBondState() != BluetoothDevice.BOND_BONDED)) {
//                        Thread mPairThread = new Thread(new PairRunnable(data));
//                        mPairThread.start();
//                    } else {
//                        Thread mUnpairThread = new Thread(new UnpairRunnable(data));
//                        mUnpairThread.start();
//                    }
//                } catch (Exception e) {
//                    Logger.d(TAG, "btnPairDevice>>setOnClickListener>> exception: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        btnConnect.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                try {
//                    if (BluetoothHandler.getInstance().getBluetoothSocket() == null) {
//                        Logger.d(TAG, "btnConnect>>setOnClickListener>> data: " + data.toString());
//                        BluetoothHandler.getInstance().startBluetoothConnectionTask(mContext, data, new BaseUpdateListener() {
//                            @Override
//                            public void onUpdate(Object... update) {
//                                Object data = update[0];
//                                if (data != null) {
//                                    boolean isConnected = (boolean) data;
//                                    if (isConnected) {
//                                        btsendrec = new BTSendReceiveThread(BluetoothHandler.getInstance().getBluetoothSocket(), mHandler);
//                                        btsendrec.start();
//                                    }
//                                }
//                            }
//                        });
//                    } else {
//                        BluetoothHandler.getInstance().stopBluetoothConnection();
//                    }
//
//                    getOwnerAdapter().notifyDataSetChanged();
//                } catch (Exception e) {
//                    Logger.d(TAG, "btnConnect>>setOnClickListener>> exception: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        btnLight.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                try {
//                    if (TextUtils.isEmpty(lastSent) || lastSent.equalsIgnoreCase("0")) {
//                        btsendrec.Send("1");
//                        lastSent = "1";
//                    } else {
//                        btsendrec.Send("0");
//                        lastSent = "0";
//                    }
//                    getOwnerAdapter().notifyDataSetChanged();
//                } catch (Exception e) {
//                    Logger.d(TAG, "btnLight>>setOnClickListener>> exception: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    /**
     * Checks for the device name, for a maximum of {@link BluetoothDeviceViewHolder#MAX_NAME_CHECKS}
     * as the name may not have been resolved at binding.
     */
    private void resolveName(final BluetoothDevice device, final TextView deviceName, final Context context) {
        if (device != null) {
            String name = device.getName();
            boolean isEmptyName = TextUtils.isEmpty(name);

            if (isEmptyName) deviceName.setText(R.string.txt_unknown_device);
            else deviceName.setText(name);

            // Check later if device name is resolved
            if (nameChecks++ < MAX_NAME_CHECKS && isEmptyName)
                itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resolveName(device, deviceName, context);
                    }
                }, NAME_CHECK_PERIOD);
        }
    }

//    private void connectBluetooth(BluetoothDevice data, Context context) {
//        Logger.d(TAG, "btnConnect>>setOnClickListener>> data: " + data.toString());
//        BluetoothHandler.getInstance().startBluetoothConnectionTask(context, data, new BaseUpdateListener() {
//            @Override
//            public void onUpdate(Object... update) {
//                Object data = update[0];
//                if (data != null) {
//                    boolean isConnected = (boolean) data;
//                    if (isConnected) {
//                        BTSendReceiveThread btsendrec = new BTSendReceiveThread(BluetoothHandler.getInstance().getBluetoothSocket(), BluetoothHandler.getInstance().mHandler);
//                        btsendrec.start();
//                    }
//                }
//            }
//        });
//    }
}