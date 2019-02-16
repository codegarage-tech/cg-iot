package tech.codegarage.iot.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.reversecoder.library.event.OnSingleClickListener;

import tech.codegarage.iot.R;
import tech.codegarage.iot.adapter.WifiAdapter;
import tech.codegarage.iot.model.WifiScanResult;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class WifiViewHolder extends BaseViewHolder<WifiScanResult> {

    private static String TAG = WifiViewHolder.class.getSimpleName();
    private TextView tvWifiSsid;
    private LinearLayout llWifiContainer, llWifiTick;

    public WifiViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_wifi);

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
            llWifiContainer.setBackgroundResource(R.drawable.shape_round_leftop_round_rightbotttom_border_primary_bg_red);
        } else {
            llWifiContainer.setBackgroundResource(R.drawable.shape_round_leftop_round_rightbotttom_border_primary_bg_blue);
        }

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                WifiAdapter wifiAdapter = (WifiAdapter) getOwnerAdapter();
                for (WifiScanResult wifiScanResult : wifiAdapter.getAllData()) {
                    if (wifiScanResult.getSsid().equalsIgnoreCase(data.getSsid())) {
                        wifiScanResult.setSelected(true);
                    } else {
                        wifiScanResult.setSelected(false);
                    }
                }
                wifiAdapter.notifyDataSetChanged();
            }
        });
    }
}