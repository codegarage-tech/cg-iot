package com.meembusoft.iot.bluetooth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseUpdateListener;
import com.meembusoft.iot.util.Logger;

import java.util.UUID;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class BluetoothHandler {

    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothHandler mBluetoothHandler;
    public static final int REQUEST_CODE_TURN_ON_BLUETOOTH = 350;
    private ProgressDialog mProgressDialog;
    private static String TAG = BluetoothHandler.class.getSimpleName();
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBluetoothSocket = null;
    private BluetoothConnectTask mBluetoothConnectTask;
    private BroadcastReceiver mBroadcastReceiver = null;

    public static BluetoothHandler getInstance() {
        if (mBluetoothHandler == null) {
            mBluetoothHandler = new BluetoothHandler();
        }
        getBluetoothAdapter();
        return mBluetoothHandler;
    }

    private BluetoothHandler() {
    }

    private static BluetoothAdapter getBluetoothAdapter() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return mBluetoothAdapter;
    }

    public void turnOnBluetooth(Activity activity) {
        if (!isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, REQUEST_CODE_TURN_ON_BLUETOOTH);
        } else {
            // Notify through broadcast that bluetooth is already enabled
//            if (mBroadcastReceiver != null) {
//                Intent turnOnIntent = new Intent("Bluetooth_Turn_On_Action");
//                activity.sendBroadcast(turnOnIntent);
//            }
        }
    }

    public void turnOffBluetooth() {
        if (isBluetoothEnabled()) {
            getBluetoothAdapter().disable();
        }
    }

    public boolean isBluetoothEnabled() {
        return getBluetoothAdapter().isEnabled();
    }

    public void startScanning(Activity activity) {
        if (isBluetoothEnabled()) {
//            showProgressDialog(activity);
            getBluetoothAdapter().startDiscovery();
        } else {
            Toast.makeText(activity, "Please enable bluetooth first", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopScanning() {
        if (isBluetoothEnabled() && getBluetoothAdapter().isDiscovering()) {
            getBluetoothAdapter().cancelDiscovery();
//            dismissProgressDialog();
        }
    }

    public boolean isScanning() {
        return getBluetoothAdapter().isDiscovering();
    }

    public void registerReceiver(Activity activity, BroadcastReceiver broadcastReceiver) {
        mBroadcastReceiver = broadcastReceiver;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
//        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        activity.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void unRegisterReceiver(Activity activity, BroadcastReceiver broadcastReceiver) {
        activity.unregisterReceiver(broadcastReceiver);
        mBroadcastReceiver = null;
    }

    /***************************
     * Progress dialog methods *
     ***************************/
    public ProgressDialog showProgressDialog(Activity activity) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage(activity.getResources().getString(R.string.progress_dialog_loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }
            });
        }

        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        return mProgressDialog;
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /********************************
     * Bluetooth connection methods *
     ********************************/
    public BluetoothSocket getBluetoothSocket() {
        return mBluetoothSocket;
    }

    public void startBluetoothConnectionTask(Context context, BluetoothDevice bluetoothDevice, BaseUpdateListener baseUpdateListener) {
        stopBluetoothConnection();
        cancelBluetoothConnectionTask();
        mBluetoothConnectTask = new BluetoothConnectTask(context, bluetoothDevice, baseUpdateListener);
        mBluetoothConnectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void cancelBluetoothConnectionTask() {
        if (mBluetoothConnectTask != null && mBluetoothConnectTask.getStatus() == AsyncTask.Status.RUNNING) {
            mBluetoothConnectTask.cancel(true);
        }
    }

    public void stopBluetoothConnection() {
        if (mBluetoothSocket != null) { // If the btSocket is not busy{
            try {
                mBluetoothSocket.close(); // Close the connection
            } catch (Exception e) {
                Logger.d(TAG, "stopBluetoothConnection>>exception: " + e.getMessage());
            }
            mBluetoothSocket = null;
        }
    }

    private class BluetoothConnectTask extends AsyncTask<BluetoothDevice, Void, Boolean> {

        private BluetoothDevice mTDevice;
        private Context mContext;
        private BaseUpdateListener mBaseUpdateListener;

        private BluetoothConnectTask(Context context, BluetoothDevice device, BaseUpdateListener baseUpdateListener) {
            mContext = context;
            mTDevice = device;
            mBaseUpdateListener = baseUpdateListener;
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public Boolean doInBackground(BluetoothDevice... params) {
            try {
                /* Open bluetooth socket for communication with selected device */
                mBluetoothSocket = mTDevice.createRfcommSocketToServiceRecord(MY_UUID);

                /* Cancel discovery */
                mBluetoothAdapter.cancelDiscovery();

                /* Connect the device through the socket. This will block until it succeeds or throws an exception */
                mBluetoothSocket.connect();
                return true;
            } catch (Exception connectException) {
                /* Unable to connect; close the socket and get out */
                Logger.d(TAG, "BluetoothConnectTask>>doInBackground>>connect>>exception: " + connectException.getMessage());
                try {
                    mBluetoothSocket.close();
                    return false;
                } catch (Exception closeException) {
                    Logger.d(TAG, "BluetoothConnectTask>>doInBackground>>close>>exception: " + closeException.getMessage());
                    return false;
                }
            }
        }

        protected void onPostExecute(Boolean result) {
            if (!result) {
                Toast.makeText(mContext, "Connection Failed! Is it a SPP Bluetooth? Try again.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "Connection is made successfully.", Toast.LENGTH_LONG).show();
            }
            mBaseUpdateListener.onUpdate(result);
        }
    }

    /* Define a Handler to transfer data from the thread in which send and
     * receive is done. and then perform some action on received data, in this case the room temperature.
     */
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case BTSendReceiveThread.READ_SUCCESS:
                    byte[] writeBuf = (byte[]) msg.obj;
                    int begin = (int) msg.arg1;
                    int end = (int) msg.arg2;

//                    String RoomTemp = new String(writeBuf);
//                    RoomTemp = RoomTemp.substring(begin, end);
//                    RoomTempText.setText(RoomTemp + " C");
                    break;
            }
        }
    };
}