package com.meembusoft.iot.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.iot.activity.WifiActivity;
import com.meembusoft.iot.util.SessionUtil;
import com.reversecoder.library.event.OnSingleClickListener;

import com.meembusoft.iot.R;
import com.meembusoft.iot.adapter.WifiAdapter;
import com.meembusoft.iot.model.WifiScanResult;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class WifiViewHolder extends BaseViewHolder<WifiScanResult> {

    private static String TAG = WifiViewHolder.class.getSimpleName();
    private TextView tvWifiSsid;
    private LinearLayout llWifiContainer, llWifiTick;

    public WifiViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_wifi2);

        tvWifiSsid = (TextView) $(R.id.tv_wifi_ssid);
        llWifiContainer = (LinearLayout) $(R.id.ll_wifi_container);
        llWifiTick = (LinearLayout) $(R.id.ll_wifi_tick);
    }

    @Override
    public void setData(final WifiScanResult data) {
        tvWifiSsid.setText(data.getSsid());
        if (data.isSelected()) {
            llWifiTick.setVisibility(View.VISIBLE);
        } else {
            llWifiTick.setVisibility(View.GONE);
        }
        if (data.isConnected()) {
            llWifiContainer.setBackgroundResource(R.color.colorShadeRed);
        }


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiActivity mActivity = ((WifiActivity) getContext());

                WifiAdapter wifiAdapter = (WifiAdapter) getOwnerAdapter();

                for (WifiScanResult wifiScanResult : wifiAdapter.getAllData()) {
                    if (wifiScanResult.getSsid().equalsIgnoreCase(data.getSsid())) {
                        wifiScanResult.setSelected(true);
                        SessionUtil.setTempSelectedWifi(getContext(), wifiScanResult.getSsid());

                        mActivity.setToolBarSelectRightImage(wifiScanResult);
                    } else {
                        wifiScanResult.setSelected(false);
                    }
                }
                wifiAdapter.notifyDataSetChanged();
            }
        });

    }
}