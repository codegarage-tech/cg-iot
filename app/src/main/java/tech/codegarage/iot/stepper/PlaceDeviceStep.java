package tech.codegarage.iot.stepper;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reversecoder.library.event.OnSingleClickListener;

import ernestoyaquello.com.verticalstepperform.Step;
import tech.codegarage.iot.R;
import tech.codegarage.iot.base.BaseUpdateListener;
import tech.codegarage.iot.model.Device;
import tech.codegarage.iot.retrofit.APIResponse;
import tech.codegarage.iot.util.AppUtil;
import tech.codegarage.iot.util.DataUtil;
import tech.codegarage.iot.util.SessionUtil;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PlaceDeviceStep extends Step<String> {

    private LinearLayout llChoseDevice;
    private Button btnScanQrCode, btnChoseManually;
    private TextView tvChoseDevice;
    private Device mChosenDevice ;
    private boolean mIsPlaced = false;

    public PlaceDeviceStep(String title) {
        this(title, "");
    }

    public PlaceDeviceStep(String title, Device device) {
        this(title, "");
        this.mChosenDevice = device;
    }

    public PlaceDeviceStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @NonNull
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View chooseDeviceStepContent = inflater.inflate(R.layout.layout_step_place_device, null, false);

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

    public void setPlaceDevice(boolean isPlaced) {
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

        return mIsPlaced ? getContext().getString(R.string.txt_placed) : getContext().getString(R.string.txt_empty);
    }

    @Override
    public String getStepDataAsHumanReadableString() {
//        String name = getStepData();
//        return name == null || name.isEmpty()
//                ? getContext().getString(R.string.txt_form_empty_field)
//                : name;
        return mIsPlaced ? getContext().getString(R.string.txt_placed) : getContext().getString(R.string.txt_empty);
    }

    @Override
    public void restoreStepData(String data) {
//        if (alarmNameEditText != null) {
//            alarmNameEditText.setText(data);
//        }

        setPlaceDevice(getContext().getString(R.string.txt_placed).equalsIgnoreCase(data));
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return new IsDataValid(mIsPlaced);
    }
}