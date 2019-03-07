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
public class AttachedDeviceWithApplianceStep extends Step<String> {

    private SwitchIconView switchAttachDevice;
    private ImageView ivAttachDevice;
    private TextView tvAttachDevice;
    private Device mChosenDevice;
    private boolean mIsAttached = false;

    public AttachedDeviceWithApplianceStep(String title) {
        this(title, "");
    }

    public AttachedDeviceWithApplianceStep(String title, Device device) {
        this(title, "");
        this.mChosenDevice = device;
    }

    public AttachedDeviceWithApplianceStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @NonNull
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View plugDeviceStepContent = inflater.inflate(R.layout.layout_step_attached_device_with_appliance, null, false);

        switchAttachDevice = (SwitchIconView) plugDeviceStepContent.findViewById(R.id.switch_attach_device);
        switchAttachDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchAttachDevice.setIconEnabled(!switchAttachDevice.isIconEnabled());
                setPlaceDevice(switchAttachDevice.isIconEnabled());
            }
        });
        ivAttachDevice = (ImageView) plugDeviceStepContent.findViewById(R.id.iv_attach_device);
        AppUtil.loadImage(getContext(), ivAttachDevice, R.drawable.plug_device, true, false, false);
        tvAttachDevice = (TextView) plugDeviceStepContent.findViewById(R.id.tv_attach_device);
        AppUtil.applyMarqueeOnTextView(tvAttachDevice);

        return plugDeviceStepContent;
    }

    public void setPlaceDevice(boolean isPlaced) {
        mIsAttached = isPlaced;
        markAsCompletedOrUncompleted(mIsAttached);
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

        return mIsAttached ? getContext().getString(R.string.txt_attached) : getContext().getString(R.string.txt_detached);
    }

    @Override
    public String getStepDataAsHumanReadableString() {
//        String name = getStepData();
//        return name == null || name.isEmpty()
//                ? getContext().getString(R.string.txt_form_empty_field)
//                : name;
        return mIsAttached ? getContext().getString(R.string.txt_attached) : getContext().getString(R.string.txt_detached);
    }

    @Override
    public void restoreStepData(String data) {
//        if (alarmNameEditText != null) {
//            alarmNameEditText.setText(data);
//        }

        setPlaceDevice(getContext().getString(R.string.txt_attached).equalsIgnoreCase(data));
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return new IsDataValid(mIsAttached);
    }
}