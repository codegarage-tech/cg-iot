package com.meembusoft.iot.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.meembusoft.iot.base.BaseUpdateListener;
import com.meembusoft.iot.util.AlarmManager;
import com.meembusoft.iot.util.Logger;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class WiFiReceiver extends BroadcastReceiver implements AlarmManager.Callback {

    protected final static long POLLING_INTERVAL = 20 * 1000; // every 20 second
    private WifiManager mWifiManager;
    private AlarmManager mAlarm;
    private boolean mRegistered = false;
    private Context mContext;
    private String mLocationProvider = LocationManager.NETWORK_PROVIDER;
    private LocationManager mLocationManager;
    private static String TAG = WiFiReceiver.class.getSimpleName();
    private boolean mIsWritable = false, mIsRepeatable = false;
    private BaseUpdateListener mBaseUpdateListener;

    public WiFiReceiver(Context context, boolean isWritable, boolean isRepeatable, BaseUpdateListener baseUpdateListener) {
        mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mAlarm = new AlarmManager(TAG, this);
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mIsWritable = isWritable;
        mIsRepeatable = isRepeatable;
        mBaseUpdateListener = baseUpdateListener;
    }

    public void startReceiver() {
        Logger.d(TAG, "Setting alarm");
        mAlarm.set(POLLING_INTERVAL, mIsRepeatable);
    }

    public void stopReceiver() {
        mAlarm.cancel();
        synchronized (this) {
            if (mRegistered) {
                mContext.getApplicationContext().unregisterReceiver(this);
                mRegistered = false;
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Parse action
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        Logger.d(TAG, "Got action: " + intent.getAction());

        // Parse connected wifi
        WifiInfo wifiInfo = null;
        switch (action) {
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                if (intent.hasExtra(WifiManager.EXTRA_WIFI_INFO)) {
                    wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                } else {
                    wifiInfo = mWifiManager.getConnectionInfo();
                }
                break;
            case LocationManager.PROVIDERS_CHANGED_ACTION:
                wifiInfo = mWifiManager.getConnectionInfo();
                break;
        }
        if (wifiInfo == null) {
            return;
        }
        Logger.d(TAG, "Got wifiInfo: " + wifiInfo.toString());

        // Parse scan result
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        if (scanResults == null) {
            return;
        }
        Logger.d(TAG, "Got scan results: " + scanResults.size());

        // Send result to client
        mBaseUpdateListener.onUpdate(action, wifiInfo, scanResults);

        Location lastKnownLocation = null;
        String buf = "";
        if (mIsWritable) {
            try {
                lastKnownLocation = mLocationManager.getLastKnownLocation(mLocationProvider);
            } catch (SecurityException e) {
                Logger.d(TAG, "Could not get last known location");
            }

            for (ScanResult scanResult : mWifiManager.getScanResults()) {
                boolean connectAbility = WifiSecurity.getScanResultSecurity(scanResult) == WifiSecurity.OPEN;
                if (!connectAbility) {
                    List<WifiConfiguration> networks = mWifiManager.getConfiguredNetworks();

                    if (networks != null) {
                        for (WifiConfiguration config : networks) {
                            if (config.SSID.equals("\"" + scanResult.SSID + "\"")) {
                                connectAbility = true;
                                break;
                            }
                        }
                    }
                }
                Logger.d(TAG, lastKnownLocation.toString() + " " + scanResult.SSID + " " + scanResult.BSSID + " "
                        + scanResult.level + " " + scanResult.capabilities + " " + connectAbility);

                buf += "" + System.currentTimeMillis() + "," + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() +
                        "," + scanResult.SSID + "," + scanResult.BSSID + "," + scanResult.level + "," + scanResult.capabilities + "," +
                        connectAbility + (Build.VERSION.SDK_INT > 18 ? System.lineSeparator() : System.getProperty("line.separator"));
            }
        }

        synchronized (this) {
            if (mRegistered)
                context.unregisterReceiver(this);
            mRegistered = false;
        }

        if (mIsWritable && isExternalStorageWritable()) {
            try {
                File f = getPrivateAlbumStorageDir(mContext, "logs");
                BufferedWriter output = new BufferedWriter(new FileWriter(f.getAbsolutePath() + "//file.txt", true));
                output.write(buf);
                output.close();
            } catch (Exception e) {
                Logger.d(TAG, "Could not write " + e.getMessage());
            }
        }
    }

    public File getPrivateAlbumStorageDir(Context context, String albumName) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), albumName);
        file.mkdirs();
        return file;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onTriggered() {
        Logger.d(TAG, "Alarm triggered");

        // Register for Wifi scan results
        synchronized (this) {
            Logger.d(TAG, "Registering receiver");
            IntentFilter mIntentFilter = new IntentFilter(TAG);
            mIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            if (isSDKAtLeastP()) {
                mIntentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
            }
            mContext.getApplicationContext().registerReceiver(this, mIntentFilter);
            mRegistered = true;
        }

        // Start the scan
        mWifiManager.startScan();
        Logger.d(TAG, "Started scanning");
    }

    private boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    private enum WifiSecurity {
        WEP, PSK, EAP, OPEN;

        public static WifiSecurity getScanResultSecurity(ScanResult scanResult) {
            final String cap = scanResult.capabilities;
            WifiSecurity[] values = WifiSecurity.values();
            for (int i = values.length - 1; i >= 0; i--) {
                if (cap.contains(values[i].name())) {
                    return values[i];
                }
            }
            return OPEN;
        }
    }
}