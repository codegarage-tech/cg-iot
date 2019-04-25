package tech.codegarage.iot.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.task.__IEsptouchTask;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.reversecoder.library.event.OnSingleClickListener;

import java.lang.ref.WeakReference;
import java.util.List;

import tech.codegarage.iot.R;
import tech.codegarage.iot.activity.AddDeviceActivity;
import tech.codegarage.iot.activity.HomeActivity;
import tech.codegarage.iot.base.BaseFragment;
import tech.codegarage.iot.base.BaseUpdateListener;
import tech.codegarage.iot.broadcast.WiFiReceiver;
import tech.codegarage.iot.model.ParamWifiConnect;
import tech.codegarage.iot.stepper.ConnectDevice;
import tech.codegarage.iot.util.AppUtil;
import tech.codegarage.iot.util.Logger;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ConnectDeviceFragment extends BaseFragment {

    private ImageView ivConnectDevice;
    private LinearLayout llChooseWifi;
    private EditText edtSsid, edtPassword;
    private String mSSID = "", mBssid = "";
    private ParamWifiConnect mParamWifiConnect = null;
    private static String TAG = ConnectDeviceFragment.class.getSimpleName();
    private boolean isWifiChosenByUser = false;

    // ESP touch
    private EsptouchAsyncTask4 mTask;
    private WiFiReceiver mWiFiReceiver;

    public static ConnectDeviceFragment newInstance() {
        ConnectDeviceFragment fragment = new ConnectDeviceFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_connect_device;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        ivConnectDevice = (ImageView) parentView.findViewById(R.id.iv_connect_device);
        llChooseWifi = (LinearLayout) parentView.findViewById(R.id.ll_choose_wifi);
        edtSsid = (EditText) parentView.findViewById(R.id.edt_ssid);
        edtPassword = (EditText) parentView.findViewById(R.id.edt_password);
    }

    @Override
    public void initFragmentViewsData() {
        AppUtil.loadImage(getActivity(), ivConnectDevice, R.drawable.connect_to_wifi, true, false, false);
    }

    @Override
    public void initFragmentActions() {
        llChooseWifi.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }
        });
    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initFragmentUpdate(Object object) {

    }

//    public boolean isAllFieldsVerified() {
//        if (checkoutFoodAdapter.getCount() > 0) {
//            return true;
//        } else {
//            Toast.makeText(getActivity(), getString(R.string.toast_please_add_to_cart_first), Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    }

    private void refreshMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void setChoseConnection(String ssid, String bssid, int frequency, boolean chosenByUser) {
        // Save user interaction
        isWifiChosenByUser = chosenByUser;

        // Process wifi credentials
        mSSID = ssid;
        if (mSSID.startsWith("\"") && mSSID.endsWith("\"")) {
            mSSID = mSSID.substring(1, mSSID.length() - 1);
        }
        edtSsid.setText(mSSID);
        byte[] byteSSID = ByteUtil.getBytesByString(mSSID);
        edtSsid.setTag(byteSSID);
//        byte[] ssidOriginalData = AppUtil.getOriginalSsidBytes(info);
//        edtChoseSsid.setTag(ssidOriginalData);
        mBssid = bssid;
        Logger.d(TAG, "setItem(onWifiChanged after setting): " + "mSSID>> " + mSSID + " mBssid>> " + mBssid);

        // Check frequency barrier
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (frequency > 4900 && frequency < 5900) {
                // Connected 5G wifi. Device does not support 5G
                // Refresh steps message
                refreshMessage(getContext().getString(R.string.txt_wifi_5g_message));
            }
        }
    }

    /*********************
     * ESP Touch methods *
     *********************/
    public void destroyTask() {
        mWiFiReceiver.stopReceiver();
    }

    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };

    public void startWifiScanTask() {
        mWiFiReceiver = new WiFiReceiver(getContext(), false, true, new BaseUpdateListener() {
            @Override
            public void onUpdate(Object... update) {
                // Process location changes
                String action = update[0] + "";
                Logger.d(TAG, "action: " + action);
                if (action.equalsIgnoreCase(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                    onLocationChanged();
                }

                // Process connected wifi
                WifiInfo wifiInfo = (WifiInfo) update[1];
                Logger.d(TAG, "wifiInfo: " + wifiInfo);
                if (!isWifiChosenByUser) {
                    Logger.d(TAG, "Auto setting ssid");
                    onWifiChanged(wifiInfo);
                } else {
                    Logger.d(TAG, "Skipping auto setting ssid due to user interaction");
                }

                // Process wifi scan result
                List<ScanResult> scanResults = (List<ScanResult>) update[2];
                Logger.d(TAG, "scanResults: " + scanResults.size());

                // Send result to fragment for updating list
//                if (mBaseUpdateListener != null) {
//                    mBaseUpdateListener.onUpdate(wifiInfo, scanResults);
//                }
            }
        });
        mWiFiReceiver.startReceiver();
    }

    private void onWifiChanged(WifiInfo info) {
        boolean connected = info != null && info.getNetworkId() != -1;
        if (!connected) {
            edtSsid.setText("");
            edtSsid.setTag(null);
            mBssid = "";

            // Refresh steps message
            refreshMessage(getContext().getString(R.string.txt_wifi_disconnected_or_changed));

            if (mTask != null) {
                mTask.cancelEsptouch();
                mTask = null;
            }
        } else {
            setChoseConnection(info.getSSID(), info.getBSSID(), (Build.VERSION.SDK_INT > 20 ? info.getFrequency() : 0), false);
        }
    }

    private void onLocationChanged() {
        boolean enable;
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            enable = false;
        } else {
            boolean locationGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean locationNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            enable = locationGPS || locationNetwork;
        }

        if (!enable) {
            // Refresh steps message
            refreshMessage(getContext().getString(R.string.txt_location_disable_message));
        }
    }

    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        ((HomeActivity) getContext()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " is connected to the wifi";
                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    class EsptouchAsyncTask4 extends AsyncTask<byte[], Void, List<IEsptouchResult>> {
        private WeakReference<HomeActivity> mActivity;

        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();
        private ProgressDialog mProgressDialog;
        private IEsptouchTask mEsptouchTask;
        private String ssid = "", bssid = "", password = "";
        private int deviceCount = -1;
        private boolean broadcast = false;

        EsptouchAsyncTask4(HomeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        void cancelEsptouch() {
            cancel(true);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (mEsptouchTask != null) {
                mEsptouchTask.interrupt();
            }
        }

        @Override
        protected void onPreExecute() {
            Activity activity = mActivity.get();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("Esptouch is configuring, please wait for a moment...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (__IEsptouchTask.DEBUG) {
                            Logger.d(TAG, "progress dialog back pressed canceled");
                        }
                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getText(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            synchronized (mLock) {
                                if (__IEsptouchTask.DEBUG) {
                                    Logger.d(TAG, "progress dialog cancel button canceled");
                                }
                                if (mEsptouchTask != null) {
                                    mEsptouchTask.interrupt();
                                }
                            }
                        }
                    });
            mProgressDialog.show();
        }

        @Override
        protected List<IEsptouchResult> doInBackground(byte[]... params) {
            HomeActivity activity = mActivity.get();
            int taskResultCount;
            synchronized (mLock) {
                byte[] apSsid = params[0];
                byte[] apBssid = params[1];
                byte[] apPassword = params[2];
                byte[] deviceCountData = params[3];
                byte[] broadcastData = params[4];
                ssid = new String(apSsid);
                bssid = new String(apBssid);
                password = new String(apPassword);
                deviceCount = Integer.parseInt(new String(deviceCountData));
                broadcast = broadcastData[0] == 1;

                taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
                Context context = activity.getApplicationContext();
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, context);
                mEsptouchTask.setPackageBroadcast(broadcastData[0] == 1);
                mEsptouchTask.setEsptouchListener(myListener);
            }
            return mEsptouchTask.executeForResults(taskResultCount);
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            HomeActivity activity = mActivity.get();
            mProgressDialog.dismiss();
            if (result == null) {
                // Refresh steps message
                refreshMessage(activity.getString(R.string.txt_create_esptouch_task_failed));
                return;
            }

            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("Esptouch success, bssid = ")
                                .append(resultInList.getBssid())
                                .append(", InetAddress = ")
                                .append(resultInList.getInetAddress().getHostAddress())
                                .append("\n");
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's ")
                                .append(result.size() - count)
                                .append(" more result(s) without showing\n");
                    }
                    Logger.d(TAG, "success result: " + sb.toString());

                    // Step is completed
                    mParamWifiConnect = new ParamWifiConnect(ssid, bssid, password, deviceCount, broadcast);
                    refreshMessage("");
                    Toast.makeText(activity, activity.getString(R.string.txt_connected_with_the_device), Toast.LENGTH_SHORT).show();
                } else {
                    // Refresh steps message
                    refreshMessage(activity.getString(R.string.txt_esptouch_fail));
                }
            }

            // This is for testing purpose, assume the step is completed
//            mParamWifiConnect = new ParamWifiConnect(ssid, bssid, password, deviceCount, broadcast);
//            refreshMessage("");
//            Toast.makeText(activity, activity.getString(R.string.txt_connected_with_the_device), Toast.LENGTH_SHORT).show();

            mTask = null;
        }
    }
}