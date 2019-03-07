package tech.codegarage.iot.stepper;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.zagum.switchicon.SwitchIconView;

import ernestoyaquello.com.verticalstepperform.Step;
import tech.codegarage.iot.R;
import tech.codegarage.iot.model.Device;
import tech.codegarage.iot.util.AppUtil;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PlugDeviceStep extends Step<String> {

    private SwitchIconView switchPlugDevice;
    private ImageView ivPlugDevice;
    private TextView tvPlugDevice;
    private Device mChosenDevice;
    private boolean mIsPlugged = false;

    public PlugDeviceStep(String title) {
        this(title, "");
    }

    public PlugDeviceStep(String title, Device device) {
        this(title, "");
        this.mChosenDevice = device;
    }

    public PlugDeviceStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @NonNull
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View plugDeviceStepContent = inflater.inflate(R.layout.layout_step_plug_device, null, false);

        switchPlugDevice = (SwitchIconView) plugDeviceStepContent.findViewById(R.id.switch_plug_device);
        switchPlugDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPlugDevice.setIconEnabled(!switchPlugDevice.isIconEnabled());
                setPlaceDevice(switchPlugDevice.isIconEnabled());
            }
        });
        ivPlugDevice = (ImageView) plugDeviceStepContent.findViewById(R.id.iv_plug_device);
        AppUtil.loadImage(getContext(), ivPlugDevice, R.drawable.plug_device, true, false, false);
        tvPlugDevice = (TextView) plugDeviceStepContent.findViewById(R.id.tv_plug_device);
        AppUtil.applyMarqueeOnTextView(tvPlugDevice);

        return plugDeviceStepContent;
    }

    public void setPlaceDevice(boolean isPlaced) {
        mIsPlugged = isPlaced;
        markAsCompletedOrUncompleted(mIsPlugged);
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

        return mIsPlugged ? getContext().getString(R.string.txt_plugged) : getContext().getString(R.string.txt_unplugged);
    }

    @Override
    public String getStepDataAsHumanReadableString() {
//        String name = getStepData();
//        return name == null || name.isEmpty()
//                ? getContext().getString(R.string.txt_form_empty_field)
//                : name;
        return mIsPlugged ? getContext().getString(R.string.txt_plugged) : getContext().getString(R.string.txt_unplugged);
    }

    @Override
    public void restoreStepData(String data) {
//        if (alarmNameEditText != null) {
//            alarmNameEditText.setText(data);
//        }

        setPlaceDevice(getContext().getString(R.string.txt_plugged).equalsIgnoreCase(data));
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return new IsDataValid(mIsPlugged);
    }
}