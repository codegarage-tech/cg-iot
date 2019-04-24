package tech.codegarage.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.labo.kaji.fragmentanimations.PushPullAnimation;
import com.reversecoder.library.event.OnSingleClickListener;

import tech.codegarage.iot.R;
import tech.codegarage.iot.activity.BarCodeScannerActivity;
import tech.codegarage.iot.base.BaseFragment;
import tech.codegarage.iot.model.Device;
import tech.codegarage.iot.retrofit.APIResponse;
import tech.codegarage.iot.util.AppUtil;
import tech.codegarage.iot.util.DataUtil;
import tech.codegarage.iot.util.Logger;
import tech.codegarage.iot.util.SessionUtil;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static tech.codegarage.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ChooseDeviceFragment extends BaseFragment {

    private LinearLayout llQrCode, llManually;
    private TextView tvChoseDevice;
    private CardView cvChoseDevice;

    public static ChooseDeviceFragment newInstance() {
        ChooseDeviceFragment fragment = new ChooseDeviceFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_choose_device;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        llQrCode = (LinearLayout) parentView.findViewById(R.id.ll_qr_code);
        llManually = (LinearLayout) parentView.findViewById(R.id.ll_manually);
        cvChoseDevice = (CardView) parentView.findViewById(R.id.cv_chose_device);
        tvChoseDevice = (TextView) parentView.findViewById(R.id.tv_chose_device);
    }

    @Override
    public void initFragmentViewsData() {
        //Set last selected device
        Device lastTempChosenDevice = SessionUtil.getTempChosenDevice(getActivity());
        if (lastTempChosenDevice != null) {
            cvChoseDevice.setVisibility(View.VISIBLE);
            tvChoseDevice.setText(lastTempChosenDevice.getName());
            AppUtil.applyMarqueeOnTextView(tvChoseDevice);
        }
    }

    @Override
    public void initFragmentActions() {
        llQrCode.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                new IntentIntegrator(getActivity())
                        .setPrompt(getString(R.string.txt_scan_your_qr_code_or_bar_code))
                        .setOrientationLocked(true)
                        .setCaptureActivity(BarCodeScannerActivity.class)
                        .setRequestCode(REQUEST_CODE).initiateScan();
            }
        });
    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, "onActivityResult found in Add device fragment");
        switch (requestCode) {
            case REQUEST_CODE:
                IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
                Logger.d(TAG, "Scanned: " + result.getContents());
                if (result.getContents() != null) {
                    Device device = DataUtil.getSpecificDeviceById("69");
                    if (device != null) {
                        cvChoseDevice.setVisibility(View.VISIBLE);
                        tvChoseDevice.setText(device.getName());
                        AppUtil.applyMarqueeOnTextView(tvChoseDevice);
                        SessionUtil.setTempChosenDevice(getContext(), APIResponse.getResponseString(device));
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.txt_sorry_we_could_not_detect_your_device), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void initFragmentUpdate(Object object) {

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return PushPullAnimation.create(PushPullAnimation.RIGHT, enter, FRAGMENT_TRANSITION_DURATION);
        } else {
            return PushPullAnimation.create(PushPullAnimation.LEFT, enter, FRAGMENT_TRANSITION_DURATION);
        }
    }

//    public boolean isAllFieldsVerified() {
//        if (checkoutFoodAdapter.getCount() > 0) {
//            return true;
//        } else {
//            Toast.makeText(getActivity(), getString(R.string.toast_please_add_to_cart_first), Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    }
}