package tech.codegarage.iot.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import tech.codegarage.iot.model.WifiScanResult;
import tech.codegarage.iot.util.Logger;
import tech.codegarage.iot.viewholder.WifiViewHolder;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class WifiAdapter extends RecyclerArrayAdapter<WifiScanResult> {

    private static final int VIEW_TYPE_REGULAR = 1;
    private static final String TAG = WifiAdapter.class.getSimpleName();

    public WifiAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new WifiViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    public void setAdapterData(WifiInfo wifiInfo, List<ScanResult> scanResults) {
        WifiScanResult wifiScanResult = getSelectedData();
        List<WifiScanResult> wifiScanResults = new ArrayList<>();

        for (ScanResult scanResult : scanResults) {
            WifiScanResult mWifiScanResult = new WifiScanResult();
            mWifiScanResult.setSsid("\"" + scanResult.SSID + "\"");
            mWifiScanResult.setBssid(scanResult.BSSID);
            mWifiScanResult.setFrequency(scanResult.frequency);
            if (wifiInfo.getSSID().equalsIgnoreCase("\"" + scanResult.SSID + "\"")) {
                Logger.d(TAG, "Connected(same): " + wifiInfo.getSSID() + "=" + "\"" + scanResult.SSID + "\"");
                mWifiScanResult.setConnected(true);
            } else {
                Logger.d(TAG, "Connected(not same): " + wifiInfo.getSSID() + "!=" + "\"" + scanResult.SSID + "\"");
                mWifiScanResult.setConnected(false);
            }

            if (wifiScanResult != null) {
                if (wifiScanResult.getSsid().equalsIgnoreCase("\"" + scanResult.SSID + "\"")) {
                    mWifiScanResult.setSelected(true);
                } else {
                    mWifiScanResult.setSelected(false);
                }
            }

            if (mWifiScanResult.isConnected()) {
                wifiScanResults.add(0, mWifiScanResult);
            } else {
                wifiScanResults.add(mWifiScanResult);
            }
        }

        removeAll();
        addAll(wifiScanResults);
        notifyDataSetChanged();
    }

    public WifiScanResult getSelectedData() {
        for (WifiScanResult mWifiScanResult : getAllData()) {
            if (mWifiScanResult.isSelected()) {
                return mWifiScanResult;
            }
        }
        return null;
    }
}