package com.meembusoft.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.meembusoft.iot.activity.SelectProductActivity;
import com.meembusoft.iot.activity.WifiActivity;
import com.meembusoft.iot.model.Product;
import com.meembusoft.iot.model.WifiScanResult;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.DataUtil;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.retrofitmanager.APIResponse;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseFragment;
import com.meembusoft.iot.enumeration.ConnectionType;
import com.meembusoft.iot.util.SessionUtil;

import org.parceler.Parcels;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_EXTRA_DEVICE;
import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_EXTRA_WIFI;
import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_REQUEST_CODE_SELECT_DEVICE;
import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_REQUEST_CODE_SELECT_WIFI;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class EnsureConnectivityFragment extends BaseFragment {

//    private FlowLayout flowLayoutConnectionType;
//    private FlowLayoutManager flowLayoutManagerConnectionType;
    private EditText edtSsid, edtPassword;
    private String mSSID = "", mBssid = "";

    private LinearLayout llWifi, llBluetooth, llMobileData,llWifiIsConnected;
    private YouTubePlayerView youTubePlayerView;
    private WifiScanResult mSelectWifiInfo;

    public static EnsureConnectivityFragment newInstance() {
        EnsureConnectivityFragment fragment = new EnsureConnectivityFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_ensure_connectivity;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {
    }

    @Override
    public void initFragmentViews(View parentView) {
//        flowLayoutConnectionType = (FlowLayout) parentView.findViewById(R.id.fl_connection_type);

        llWifi = (LinearLayout) parentView.findViewById(R.id.ll_wifi);
        llBluetooth = (LinearLayout) parentView.findViewById(R.id.ll_bluetooth);
        llMobileData = (LinearLayout) parentView.findViewById(R.id.ll_mobile_data);
        llWifiIsConnected = (LinearLayout) parentView.findViewById(R.id.ll_wifi_is_connected);
        youTubePlayerView = (YouTubePlayerView) parentView.findViewById(R.id.youtube_player_view);
        edtSsid = (EditText) parentView.findViewById(R.id.edt_ssid);
        edtPassword = (EditText) parentView.findViewById(R.id.edt_password);

    }

    @Override
    public void initFragmentViewsData() {
//        Product tempChosenDevice = SessionUtil.getTempChosenDevice(getActivity());
//        if (tempChosenDevice != null) {
//            initConnectionType(tempChosenDevice);
//        }

        initAddVideoYoutubePlayer();

    }

    @Override
    public void initFragmentActions() {
        llWifi.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iWifi = new Intent(getActivity(), WifiActivity.class);
                getActivity().startActivityForResult(iWifi, INTENT_KEY_REQUEST_CODE_SELECT_WIFI);
            }
        });
    }

    @Override
    public void initFragmentBackPress() {
        SessionUtil.setTempSelectedWifi(getActivity(), "");

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, "onActivityResult found in Add device fragment");
        switch (requestCode) {
            case INTENT_KEY_REQUEST_CODE_SELECT_WIFI:
                Logger.d(TAG, "<<<INTENT_KEY_REQUEST_CODE_SELECT_WIFI: " );
              //  Product product = SessionUtil.getLastSelectedDevice(getActivity());
                if (data != null) {
                    Parcelable parcelable = data.getParcelableExtra(INTENT_KEY_EXTRA_WIFI);
                    if (parcelable != null) {
                        mSelectWifiInfo = Parcels.unwrap(parcelable);
                        llWifiIsConnected.setVisibility(View.VISIBLE);
                        mSSID = mSelectWifiInfo.getSsid();
                        if (mSSID.startsWith("\"") && mSSID.endsWith("\"")) {
                            mSSID = mSSID.substring(1, mSSID.length() - 1);
                        }
                        edtSsid.setText(mSSID);

                        Logger.d(TAG, "mSelectWifiInfo: " + mSelectWifiInfo.toString());
                    }
                } else {
                    //Set last temp selected wifi key
                    String lastTempSelectedWifi = SessionUtil.getTempSelectedWifi(getActivity());
                    if (!AllSettingsManager.isNullOrEmpty(lastTempSelectedWifi)) {
                        llWifiIsConnected.setVisibility(View.VISIBLE);
                        mSSID = lastTempSelectedWifi;
                        if (mSSID.startsWith("\"") && mSSID.endsWith("\"")) {
                            mSSID = mSSID.substring(1, mSSID.length() - 1);
                        }
                        edtSsid.setText(mSSID);
                    }
                }

                break;
        }
    }

    @Override
    public void initFragmentUpdate(Object object) {

    }

    // Video Youtube Player
    private void initAddVideoYoutubePlayer() {
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "2iJCga5Svuk";
                    if (!TextUtils.isEmpty(videoId)) {
                        youTubePlayer.loadVideo(videoId, 0);
                        youTubePlayer.pause();
                    }

            }
        });
    }

    /***************************
     * Methods for flow layout *
     ***************************/
//    public void initConnectionType(final Product device) {
//        if (device != null) {
//            //Set flow layout with connection key
//            flowLayoutManagerConnectionType = new FlowLayoutManager.FlowViewBuilder(getActivity(), flowLayoutConnectionType, device.getConnection(), new FlowLayoutManager.onFlowViewClick() {
//                @Override
//                public void flowViewClick(TextView updatedTextView) {
//                    List<TextView> selectedConnectionTypeKeys = flowLayoutManagerConnectionType.getSelectedFlowViews();
//                    String tempSelectedConnection = (selectedConnectionTypeKeys.size() > 0) ? selectedConnectionTypeKeys.get(0).getText().toString() : "";
//                    Logger.d(TAG, "tempSelectedConnection: " + tempSelectedConnection);
//
//                    //Save temp selected connection type
//                    device.setSelected_connection(tempSelectedConnection);
//                    SessionUtil.setTempSelectedConnectionType(getActivity(), tempSelectedConnection);
//                }
//            })
//                    .setSingleChoice(true)
//                    .build();
//
//            //Set last temp selected connection type key
//            String lastTempSelectedRoom = SessionUtil.getTempSelectedConnectionType(getActivity());
//            if (!AllSettingsManager.isNullOrEmpty(lastTempSelectedRoom)) {
//                flowLayoutManagerConnectionType.clickFlowView(lastTempSelectedRoom);
//            }
//        }
//    }

    private void selectConnectionType(ConnectionType connectionType){
        if(connectionType !=null){
            switch (connectionType){
                case WIFI:
                    break;
                case BLUETOOTH:
                    break;
                case MOBILE_DATA:
                    break;
            }
        }
    }

    public boolean isAllFieldsVerified() {
        String lastTempSelectedConnectionType = SessionUtil.getTempSelectedConnectionType(getActivity());
        if (!AllSettingsManager.isNullOrEmpty(lastTempSelectedConnectionType)) {
            return true;
        } else {
            Toast.makeText(getActivity(), getString(R.string.txt_please_select_your_connection_type), Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}