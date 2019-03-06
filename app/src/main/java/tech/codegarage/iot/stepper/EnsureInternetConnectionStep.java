package tech.codegarage.iot.stepper;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ernestoyaquello.com.verticalstepperform.Step;
import tech.codegarage.iot.R;
import tech.codegarage.iot.model.Device;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class EnsureInternetConnectionStep extends Step<String> {

    private LinearLayout llChoseDevice;
    private Button btnScanQrCode, btnChoseManually;
    private TextView tvChoseDevice;
    private Device mChosenDevice ;
    private boolean mIsInternetConnected = false;

    public EnsureInternetConnectionStep(String title) {
        this(title, "");
    }

    public EnsureInternetConnectionStep(String title, Device device) {
        this(title, "");
        this.mChosenDevice = device;
    }

    public EnsureInternetConnectionStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @NonNull
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View chooseDeviceStepContent = inflater.inflate(R.layout.layout_step_ensure_internet_connection, null, false);

//        btnScanQrCode = chooseDeviceStepContent.findViewById(R.id.btn_scan_qr_code);
//        btnChoseManually = chooseDeviceStepContent.findViewById(R.id.btn_choose_manually);
//        llChoseDevice = chooseDeviceStepContent.findViewById(R.id.ll_chose_device);
//        llChoseDevice.setVisibility(View.GONE);
//        tvChoseDevice = chooseDeviceStepContent.findViewById(R.id.tv_chose_device);
//
//        btnScanQrCode.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                if (mBaseUpdateListener != null) {
//                    mBaseUpdateListener.onUpdate(view);
//                }
//            }
//        });
//
//        btnChoseManually.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                if (mBaseUpdateListener != null) {
//                    mBaseUpdateListener.onUpdate(view);
//                }
//            }
//        });

        return chooseDeviceStepContent;
    }

    public void setEnsureInternetConnection(boolean isPlaced) {
//        mChosenDevice = device;
//        llChoseDevice.setVisibility(View.VISIBLE);
//        tvChoseDevice.setText(device.getName());
//        AppUtil.doMarqueeTextView(tvChoseDevice);
//        SessionUtil.setChosenDevice(getContext(), APIResponse.getResponseString(device));
//
        markAsCompletedOrUncompleted(true);
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
//        Editable text = alarmNameEditText.getText();
//        if (text != null) {
//            return text.toString();
//        }

        return mIsInternetConnected ? getContext().getString(R.string.txt_connected) : getContext().getString(R.string.txt_empty);
    }

    @Override
    public String getStepDataAsHumanReadableString() {
//        String name = getStepData();
//        return name == null || name.isEmpty()
//                ? getContext().getString(R.string.txt_form_empty_field)
//                : name;
        return mIsInternetConnected ? getContext().getString(R.string.txt_connected) : getContext().getString(R.string.txt_empty);
    }

    @Override
    public void restoreStepData(String data) {
//        if (alarmNameEditText != null) {
//            alarmNameEditText.setText(data);
//        }

        setEnsureInternetConnection(getContext().getString(R.string.txt_connected).equalsIgnoreCase(data));
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return new IsDataValid(mIsInternetConnected);
    }
}