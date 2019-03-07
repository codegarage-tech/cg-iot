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
public class EnsureConnectivityStep extends Step<String> {

    private SwitchIconView switchEnsureWifi;
    private ImageView ivEnsureWifi;
    private TextView tvEnsureWifi;
    private Device mChosenDevice;
    private boolean mIsWifiConnected = false;

    public EnsureConnectivityStep(String title) {
        this(title, "");
    }

    public EnsureConnectivityStep(String title, Device device) {
        this(title, "");
        this.mChosenDevice = device;
    }

    public EnsureConnectivityStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @NonNull
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View plugDeviceStepContent = inflater.inflate(R.layout.layout_step_ensure_connectivity, null, false);

        switchEnsureWifi = (SwitchIconView) plugDeviceStepContent.findViewById(R.id.switch_ensure_wifi);
        switchEnsureWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchEnsureWifi.setIconEnabled(!switchEnsureWifi.isIconEnabled());
                setEnsureWifi(switchEnsureWifi.isIconEnabled());
            }
        });
        ivEnsureWifi = (ImageView) plugDeviceStepContent.findViewById(R.id.iv_ensure_wifi);
        AppUtil.loadImage(getContext(), ivEnsureWifi, R.drawable.ensure_wifi, true, false, false);
        tvEnsureWifi = (TextView) plugDeviceStepContent.findViewById(R.id.tv_ensure_wifi);
        AppUtil.applyMarqueeOnTextView(tvEnsureWifi);

        return plugDeviceStepContent;
    }

    public void setEnsureWifi(boolean isWifiConnected) {
        mIsWifiConnected = isWifiConnected;
        markAsCompletedOrUncompleted(mIsWifiConnected);
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

        return mIsWifiConnected ? getContext().getString(R.string.txt_connected) : getContext().getString(R.string.txt_disconnected);
    }

    @Override
    public String getStepDataAsHumanReadableString() {
//        String name = getStepData();
//        return name == null || name.isEmpty()
//                ? getContext().getString(R.string.txt_form_empty_field)
//                : name;
        return mIsWifiConnected ? getContext().getString(R.string.txt_connected) : getContext().getString(R.string.txt_disconnected);
    }

    @Override
    public void restoreStepData(String data) {
//        if (alarmNameEditText != null) {
//            alarmNameEditText.setText(data);
//        }

        setEnsureWifi(getContext().getString(R.string.txt_connected).equalsIgnoreCase(data));
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return new IsDataValid(mIsWifiConnected);
    }
}