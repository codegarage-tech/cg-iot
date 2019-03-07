package tech.codegarage.iot.stepper;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
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
public class ChooseDeviceStep extends Step<String> {

    private LinearLayout llChoseDevice, llScanQrCode, llChoseManually;
    private TextView tvChoseDevice;
    private BaseUpdateListener mBaseUpdateListener;
    private Device mChosenDevice = null;

    public ChooseDeviceStep(String title) {
        this(title, "");
    }

    public ChooseDeviceStep(String title, BaseUpdateListener baseUpdateListener) {
        this(title, "");
        this.mBaseUpdateListener = baseUpdateListener;
    }

    public ChooseDeviceStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @NonNull
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View chooseDeviceStepContent = inflater.inflate(R.layout.layout_step_choose_device, null, false);

        llScanQrCode = chooseDeviceStepContent.findViewById(R.id.ll_scan_qr_code);
        llChoseManually = chooseDeviceStepContent.findViewById(R.id.ll_choose_manually);
        llChoseDevice = chooseDeviceStepContent.findViewById(R.id.ll_chose_device);
        llChoseDevice.setVisibility(View.GONE);
        tvChoseDevice = chooseDeviceStepContent.findViewById(R.id.tv_chose_device);

        llScanQrCode.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (mBaseUpdateListener != null) {
                    mBaseUpdateListener.onUpdate(view);
                }
            }
        });

        llChoseManually.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (mBaseUpdateListener != null) {
                    mBaseUpdateListener.onUpdate(view);
                }
            }
        });

        return chooseDeviceStepContent;
    }

    public void setChoseDevice(Device device) {
        mChosenDevice = device;
        llChoseDevice.setVisibility(View.VISIBLE);
        tvChoseDevice.setText(device.getName());
        AppUtil.applyMarqueeOnTextView(tvChoseDevice);
        SessionUtil.setChosenDevice(getContext(), APIResponse.getResponseString(device));

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

        return mChosenDevice != null ? mChosenDevice.getName() : getContext().getString(R.string.txt_empty);
    }

    @Override
    public String getStepDataAsHumanReadableString() {
//        String name = getStepData();
//        return name == null || name.isEmpty()
//                ? getContext().getString(R.string.txt_form_empty_field)
//                : name;
        return mChosenDevice != null ? mChosenDevice.getName() : getContext().getString(R.string.txt_empty);
    }

    @Override
    public void restoreStepData(String data) {
//        if (alarmNameEditText != null) {
//            alarmNameEditText.setText(data);
//        }
        setChoseDevice(DataUtil.getSpecificDeviceByName(data));
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        if (mChosenDevice != null) {
            return new IsDataValid(true);
        } else {
//            return new IsDataValid(false, getContext().getString(R.string.txt_select_at_least_one_device));
            return new IsDataValid(false);
        }
    }
}