package tech.codegarage.iot.stepper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.task.__IEsptouchTask;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.EspNetUtil;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.lang.ref.WeakReference;
import java.util.List;

import ernestoyaquello.com.verticalstepperform.Step;
import tech.codegarage.iot.R;
import tech.codegarage.iot.activity.HomeActivity;
import tech.codegarage.iot.base.BaseUpdateListener;
import tech.codegarage.iot.broadcast.WiFiReceiver;
import tech.codegarage.iot.model.ParamWifiConnect;
import tech.codegarage.iot.util.KeyboardManager;
import tech.codegarage.iot.util.Logger;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ConnectDevice extends Step<String> {

    private LinearLayout llChoseConnection, llEditConnection;
    private EditText edtChoseSsid, edtChosePassword;
    private Button btnConnectDevice;
    private BaseUpdateListener mBaseUpdateListener;
    private ParamWifiConnect mParamWifiConnect = null;
    private String mSSID = "", mBssid = "", mErrorText = "";
    private static String TAG = ConnectDevice.class.getSimpleName();
    private boolean isWifiChosenByUser = false;

    // ESP touch
    private EsptouchAsyncTask4 mTask;
    private WiFiReceiver mWiFiReceiver;

    public ConnectDevice(String title) {
        this(title, "");
    }

    public ConnectDevice(String title, BaseUpdateListener baseUpdateListener) {
        this(title, "");
        this.mBaseUpdateListener = baseUpdateListener;
    }

    public ConnectDevice(String title, String subtitle) {
        super(title, subtitle);
    }

    @NonNull
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View connectDevice = inflater.inflate(R.layout.layout_step_connect_device, null, false);

        llChoseConnection = connectDevice.findViewById(R.id.ll_chose_connection);
        llEditConnection = connectDevice.findViewById(R.id.ll_edit_connection);
        edtChoseSsid = connectDevice.findViewById(R.id.edt_chose_ssid);
        edtChoseSsid.setHint(getContext().getString(R.string.txt_input_ssid));
        edtChosePassword = connectDevice.findViewById(R.id.edt_chose_password);
        edtChosePassword.setHint(getContext().getString(R.string.txt_input_password));
        btnConnectDevice = connectDevice.findViewById(R.id.btn_connect_device);
        mErrorText = getContext().getString(R.string.txt_device_is_not_connected);

        llEditConnection.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (mBaseUpdateListener != null) {
                    mBaseUpdateListener.onUpdate(view);
                }
            }
        });

        // Start ESP touch
        StartScanTask();
        btnConnectDevice.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // Check internet connection
                if (!NetworkManager.isConnected(getContext())) {
                    Toast.makeText(getContext(), getContext().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hide keyboard
                KeyboardManager.hideKeyboard((HomeActivity) getContext());

                // Check fields
                if (AllSettingsManager.isNullOrEmpty(edtChoseSsid.getText().toString())) {
                    Toast.makeText(getContext(), getContext().getString(R.string.toast_please_input_your_ssid), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (AllSettingsManager.isNullOrEmpty(edtChosePassword.getText().toString())) {
                    Toast.makeText(getContext(), getContext().getString(R.string.toast_please_input_your_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                byte[] ssid = edtChoseSsid.getTag() == null ? ByteUtil.getBytesByString(edtChoseSsid.getText().toString()) : (byte[]) edtChoseSsid.getTag();
                byte[] password = ByteUtil.getBytesByString(edtChosePassword.getText().toString());
                byte[] bssid = EspNetUtil.parseBssid2bytes(mBssid);
                byte[] deviceCount = "1".getBytes();
                byte[] broadcast = {(byte) 1};

                if (mTask != null) {
                    mTask.cancelEsptouch();
                }
                mTask = new EsptouchAsyncTask4((HomeActivity) getContext());
                mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ssid, bssid, password, deviceCount, broadcast);
            }
        });

        return connectDevice;
    }

    @Override
    protected void onStepOpened(boolean animated) {
        // No need to do anything here
    }

    @Override
    protected void onStepClosed(boolean animated) {
        // No need to do anything here
    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {
        // No need to do anything here
    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {
        // No need to do anything here
    }

    @Override
    public String getStepData() {
        return mParamWifiConnect != null ? mParamWifiConnect.getSsid() : getContext().getString(R.string.txt_empty);
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return mParamWifiConnect != null ? mParamWifiConnect.getSsid() : getContext().getString(R.string.txt_empty);
    }

    @Override
    public void restoreStepData(String data) {
        edtChoseSsid.setText(mSSID);
        edtChoseSsid.setTag(ByteUtil.getBytesByString(mSSID));
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        if (mParamWifiConnect != null) {
            return new IsDataValid(true);
        } else {
            return new IsDataValid(false, mErrorText);
        }
    }

    private void refreshMessage(String errorMessage) {
        mErrorText = errorMessage;
        markAsCompletedOrUncompleted(true);
    }

    public void setChoseConnection(String ssid, String bssid, int frequency, boolean chosenByUser) {
        // Save user interaction
        isWifiChosenByUser = chosenByUser;

        // Process wifi credentials
        mSSID = ssid;
        if (mSSID.startsWith("\"") && mSSID.endsWith("\"")) {
            mSSID = mSSID.substring(1, mSSID.length() - 1);
        }
        edtChoseSsid.setText(mSSID);
        byte[] byteSSID = ByteUtil.getBytesByString(mSSID);
        edtChoseSsid.setTag(byteSSID);
//        byte[] ssidOriginalData = AppUtil.getOriginalSsidBytes(info);
//        edtChoseSsid.setTag(ssidOriginalData);
        mBssid = bssid;
        Logger.d(TAG, "setItem(onWifiChanged after setting): " + "mSSID>> " + mSSID + " mBssid>> " + mBssid);

        // Refresh steps message
        refreshMessage("");

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

    private void StartScanTask() {
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
                if (mBaseUpdateListener != null) {
                    mBaseUpdateListener.onUpdate(wifiInfo, scanResults);
                }
            }
        });
        mWiFiReceiver.startReceiver();
    }

    private void onWifiChanged(WifiInfo info) {
        boolean connected = info != null && info.getNetworkId() != -1;
        if (!connected) {
            edtChoseSsid.setText("");
            edtChoseSsid.setTag(null);
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