package com.meembusoft.iot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.github.zagum.switchicon.SwitchIconView;
import com.jaeger.library.StatusBarUtil;
import com.meembusoft.iot.R;
import com.meembusoft.iot.adapter.ImageSliderAdapter;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.base.BaseUpdateListener;
import com.meembusoft.iot.enumeration.ConnectionType;
import com.meembusoft.iot.enumeration.DeviceType;
import com.meembusoft.iot.model.Configuration;
import com.meembusoft.iot.model.Connection;
import com.meembusoft.iot.model.Device;
import com.meembusoft.iot.model.Image;
import com.meembusoft.iot.model.MQTTWifiGangBoardFive;
import com.meembusoft.iot.model.MQTTWifiMotorOne;
import com.meembusoft.iot.model.MQTTWifiMotorTwo;
import com.meembusoft.iot.model.User;
import com.meembusoft.iot.util.AllConstants;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.DataUtil;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.util.SessionUtil;
import com.meembusoft.retrofitmanager.APIResponse;
import com.rxmqtt.enums.ClientType;
import com.rxmqtt.exceptions.RxMqttException;
import com.rxmqtt.implementation.RxMqttClientFactory;
import com.rxmqtt.implementation.RxMqttClientStatus;
import com.rxmqtt.implementation.RxMqttMessage;
import com.rxmqtt.interfaces.IRxMqttClient;
import com.scwang.wave.MultiWaveHeader;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;
import com.skydoves.flourish.Flourish;
import com.skydoves.flourish.FlourishAnimation;
import com.skydoves.flourish.FlourishOrientation;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import net.colindodd.toggleimagebutton.ToggleImageButton;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.dialog.PopupDialog;
import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;
import rx.Observer;
import rx.Subscriber;

import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_EXTRA_DEVICE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DeviceDetailActivity extends BaseActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;

    // Flourish
    private Flourish flourish;
    private ViewGroup parentLayout;

    private ToggleImageButton btnLight1, btnLight2, btnFan, btnSocket;
    private Croller crollerLight1, crollerFan;

    // MQTT
//    private IotMqttModel mqttModel;
    public static final String MQTT_SCHEME = "tcp://";
    public static final String MQTT_HOST = "157.245.88.4";
    public static final int MQTT_PORT = 1883;
    public static final String MQTT_USER = "admin";
    public static final String MQTT_PASSWORD = "admin";
    private String mUserId = "", mDeviceId = "";
    private User mUser;
    private IRxMqttClient rxMqttClient;
    private String TAG_MQTT = ">>IRxMqttClient>>";
    public Device mDevice;
    private DeviceType mDeviceType;
    private boolean isSubscribed = false;
    private LinearLayout llWifiGangBoardFive, llWifiMotorOne, llWifiMotorTwo;
    private NestedScrollView nestedScrollView;

    // Sliderview
    private SliderView sliderViewDevice;
    private ImageSliderAdapter imageSliderAdapterDevice;
    private MultiWaveHeader multiWaveHeaderTank;

    private ImageView ivDeviceDetailFavorite, ivConnectedConnection;
    private TextView tvConnectedConnectionStatus, tvConnectedConnectionName;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_device_detail_new_new;
    }

    @Override
    public void initStatusBarView() {
        StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {
        Parcelable parcelable = intent.getParcelableExtra(INTENT_KEY_EXTRA_DEVICE);
        if (parcelable != null) {
            mDevice = Parcels.unwrap(parcelable);
            Logger.d(TAG, "mDevice: " + mDevice.toString());

            // Set device type
            if (mDevice != null && !TextUtils.isEmpty(mDevice.getProduct().getProduct_type().getProduct_type_name())) {
                mDeviceType = DeviceType.getDeviceType(mDevice.getProduct().getProduct_type().getProduct_type_name());
            }
        }
    }

    @Override
    public void initActivityViews() {
        //toolbar view
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbar_title);
        leftMenu = (AnimatedImageView) findViewById(R.id.left_menu);
        rightMenu = (ImageView) findViewById(R.id.right_menu);

        btnLight1 = (ToggleImageButton) findViewById(R.id.btn_light1);
        btnFan = (ToggleImageButton) findViewById(R.id.btn_fan);
        btnLight2 = (ToggleImageButton) findViewById(R.id.btn_light2);
        crollerLight1 = (Croller) findViewById(R.id.croller_light1);
        crollerFan = (Croller) findViewById(R.id.croller_fan);
        btnSocket = (ToggleImageButton) findViewById(R.id.btn_socket);
        // Detail type
        llWifiGangBoardFive = (LinearLayout) findViewById(R.id.ll_wifi_gang_board_five);
        llWifiMotorOne = (LinearLayout) findViewById(R.id.ll_wifi_motor_one);
        llWifiMotorTwo = (LinearLayout) findViewById(R.id.ll_wifi_motor_two);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedscrollview_product_detail);
        parentLayout = findViewById(R.id.parentLayout);
        sliderViewDevice = (SliderView) findViewById(R.id.sliderview_device);
        multiWaveHeaderTank = (MultiWaveHeader) findViewById(R.id.multiwaveheader_tank);

        ivDeviceDetailFavorite = (ImageView) findViewById(R.id.iv_device_detail_favorite);
        ivConnectedConnection = (ImageView) findViewById(R.id.iv_connected_connection);
        tvConnectedConnectionStatus = (TextView) findViewById(R.id.tv_connected_connection_status);
        tvConnectedConnectionName = (TextView) findViewById(R.id.tv_connected_connection_name);

        initSettings(mDevice);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        setToolBarTitle(toolbarTitle, getString(R.string.title_activity_device_detail));
        initDeviceInfo(mDevice);
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });

        ivDeviceDetailFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDevice.setIs_favorite((mDevice.getIs_favorite() == 1) ? 0 : 1);
                refreshFavoriteView(mDevice);
            }
        });

        btnLight1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                publishToTopic(new BaseUpdateListener() {
                    @Override
                    public void onUpdate(Object... update) {

                    }
                });
            }
        });

        btnLight2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                publishToTopic(new BaseUpdateListener() {
                    @Override
                    public void onUpdate(Object... update) {

                    }
                });
            }
        });

        btnFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                publishToTopic(new BaseUpdateListener() {
                    @Override
                    public void onUpdate(Object... update) {

                    }
                });
            }
        });

        btnSocket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                publishToTopic(new BaseUpdateListener() {
                    @Override
                    public void onUpdate(Object... update) {

                    }
                });
            }
        });

        crollerLight1.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {
            }

            @Override
            public void onStopTrackingTouch(Croller croller) {
//                croller.setLabel(((croller.getProgress() - 1) * 10) + "%");
                Logger.d(TAG, TAG_MQTT + "crollerLight1>> progress: " + ((croller.getProgress() - 1) * 10));
                publishToTopic(new BaseUpdateListener() {
                    @Override
                    public void onUpdate(Object... update) {

                    }
                });
            }
        });

        crollerFan.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {
            }

            @Override
            public void onStopTrackingTouch(Croller croller) {
                Logger.d(TAG, TAG_MQTT + "crollerLight1>> progress: " + ((croller.getProgress() - 1) * 10));
                publishToTopic(new BaseUpdateListener() {
                    @Override
                    public void onUpdate(Object... update) {

                    }
                });
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    Logger.d(TAG, "nestedScrollView>>Scroll DOWN");
                    enableKnobs(false);
                    return;
                }
                if (scrollY < oldScrollY) {
                    Logger.d(TAG, "nestedScrollView>>Scroll UP");
                    enableKnobs(false);
                    return;
                }
                if (scrollY == 0) {
                    Logger.d(TAG, "nestedScrollView>>TOP SCROLL");
                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Logger.d(TAG, "nestedScrollView>>BOTTOM SCROLL");
                }
                enableKnobs(true);
            }
        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        if (flourish.isShowing()) {
            flourish.dismiss();
        } else {
            Intent intent = new Intent();
            intent.putExtra(AllConstants.INTENT_KEY_EXTRA_DEVICE, Parcels.wrap(mDevice));
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissProgressDialog();

//        if (mqttModel != null) {
//            mqttModel.disconnect(getActivity());
//        }
        destroyMQTT();
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setToolBarTitle(AnimatedTextView animatedTextView, String title) {
        animatedTextView.setAnimatedText(title, 0L);

        //For marquee address
        AppUtil.applyMarqueeOnTextView(toolbarTitle);
    }

    /***************
     * MQTT methods *
     ****************/
    @Override
    protected void onStart() {
        super.onStart();
//        mqttModel.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mqttModel.stop();
    }

    private String toPrettyJsonString(String jsonStr) {
        String result = jsonStr;
        try {
            JSONObject json = new JSONObject(jsonStr);
            result = json.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getServerURL() {
        String scheme = getScheme();
        String host = getHost();
        int port = getPort();
        return scheme + host + ":" + port;
    }

    public String getScheme() {
        return MQTT_SCHEME;
    }

    public String getHost() {
        return MQTT_HOST;
    }

    public int getPort() {
        return MQTT_PORT;
    }

    public String getUserName() {
        return MQTT_USER;
    }

    public String getPassword() {
        return MQTT_PASSWORD;
    }

    public String getClientID() {
        return mUserId + "." + mDeviceId;
    }

    public String getTopic() {
        return "/" + mUserId + "/" + mDeviceId;
    }

    private String prepareDeviceStatus() {
        String jsonData = "";
        if (mDeviceType != null) {
            switch (mDeviceType) {
                case WIFI_MOTOR_ONE:
                    // TODO: need to implement like as wifi gang board five
                    MQTTWifiMotorOne mqttWifiMotorOne = new MQTTWifiMotorOne();
                    jsonData = APIResponse.getJSONStringFromObject(mqttWifiMotorOne);
                    break;
                case WIFI_MOTOR_TWO:
                    // TODO: need to implement like as wifi gang board five
                    MQTTWifiMotorTwo mqttWifiMotorTwo = new MQTTWifiMotorTwo();
                    jsonData = APIResponse.getJSONStringFromObject(mqttWifiMotorTwo);
                    break;
                case WIFI_GANG_BOARD_FIVE:
                    MQTTWifiGangBoardFive mqttWifiGangBoardFive = new MQTTWifiGangBoardFive((btnLight1.isChecked() ? 1 : 0),
                            ((crollerLight1.getProgress() - 1) * 10),
                            (btnFan.isChecked() ? 1 : 0),
                            ((crollerFan.getProgress() - 1) * 10),
                            (btnLight2.isChecked() ? 1 : 0),
                            (btnSocket.isChecked() ? 1 : 0)
                    );
                    jsonData = APIResponse.getJSONStringFromObject(mqttWifiGangBoardFive);
                    break;
            }
            Logger.d(TAG, TAG_MQTT + "prepareDeviceStatus()>>jsonData: " + jsonData);
        }
        return jsonData;
    }

    private void initMQTT(String host, int port, String clientId, String topic) {
        try {
            // Create IRxMqttClient
            rxMqttClient = new RxMqttClientFactory().create(
                    host,
                    port,
                    clientId,
                    false,
                    ClientType.Async);

            // Subscribe for Status report
            rxMqttClient.statusReport().subscribe(new Observer<RxMqttClientStatus>() {
                @Override
                public void onCompleted() {
                    Logger.d(TAG, TAG_MQTT + "statusReport()>>onCompleted()");
                }

                @Override
                public void onError(Throwable e) {
                    Logger.d(TAG, TAG_MQTT + "statusReport()>>onError()>> e: " + e.getMessage());
                }

                @Override
                public void onNext(RxMqttClientStatus rxMqttClientStatus) {
                    try {
                        Logger.d(TAG, TAG_MQTT + "statusReport()>>onNext()>>rxMqttClientStatus: " + rxMqttClientStatus.toString());
                    } catch (Exception ex) {
                        Logger.d(TAG, TAG_MQTT + "statusReport()>>onNext()>>Exception: " + ex.getMessage());
                    }
                }
            });

            // Connect to MQTT
            connectToMQTT();
        } catch (RxMqttException ex) {
            ex.printStackTrace();
        }
    }

    private void connectToMQTT() {
        String topic = getTopic();
        Logger.d(TAG, TAG_MQTT + "connect()>>topic: " + topic);

        if (!TextUtils.isEmpty(topic) && rxMqttClient != null) {
            rxMqttClient.connect().subscribe(new Subscriber<IMqttToken>() {
                @Override
                public void onCompleted() {
                    Logger.d(TAG, TAG_MQTT + "connect()>>onCompleted()");
                    Logger.d(TAG, TAG_MQTT + "connect()>>onCompleted()>>isSubscribed: " + isSubscribed);
                    if (!isSubscribed) {
                        subscribeToTopic(topic);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Logger.d(TAG, TAG_MQTT + "connect()>>onError()>> e: " + e.getMessage());
                    showConnectionErrorDialog();
                }

                @Override
                public void onNext(IMqttToken iMqttToken) {
                    try {
                        Logger.d(TAG, TAG_MQTT + "connect()>>onNext()>>iMqttToken: " + new String(iMqttToken.getResponse().getPayload(), "UTF-8"));
                    } catch (Exception ex) {
                        Logger.d(TAG, TAG_MQTT + "connect()>>onNext()>>Exception: " + ex.getMessage());
                    }
                }
            });
        }
    }

    private void publishToTopic(BaseUpdateListener updateListener) {
        String jsonData = prepareDeviceStatus();
        String topic = getTopic();

        if (!TextUtils.isEmpty(getTopic()) && !TextUtils.isEmpty(jsonData) && rxMqttClient != null) {
            rxMqttClient.publish(topic, jsonData.getBytes()).subscribe(new Observer<IMqttToken>() {
                @Override
                public void onCompleted() {
                    Logger.d(TAG, TAG_MQTT + "publish()>>onCompleted()");
                    if (updateListener != null) {
                        updateListener.onUpdate(true);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Logger.d(TAG, TAG_MQTT + "publish()>>onError()>> e: " + e.getMessage());
                    showConnectionErrorDialog();
                    if (updateListener != null) {
                        updateListener.onUpdate(false);
                    }
                }

                @Override
                public void onNext(IMqttToken iMqttToken) {
                    try {
                        Logger.d(TAG, TAG_MQTT + "publish()>>onNext()>>iMqttToken: " + new String(iMqttToken.getResponse().getPayload(), "UTF-8"));
                    } catch (Exception ex) {
                        Logger.d(TAG, TAG_MQTT + "publish()>>onNext()>>Exception: " + ex.getMessage());
                    }
                }
            });
        }
    }

    private void subscribeToTopic(String topic) {
        if (!TextUtils.isEmpty(topic) && rxMqttClient != null) {
            rxMqttClient.subscribeTopic(topic, 1/*QoS*/).subscribe(new Observer<RxMqttMessage>() {
                @Override
                public void onCompleted() {
                    Logger.d(TAG, TAG_MQTT + "subscribeTopic()>>onCompleted()");
                    isSubscribed = true;
                }

                @Override
                public void onError(Throwable e) {
                    Logger.d(TAG, TAG_MQTT + "subscribeTopic()>>onError()>> e: " + e.getMessage());
                    isSubscribed = false;
                }

                @Override
                public void onNext(RxMqttMessage rxMqttMessage) {
                    try {
                        Logger.d(TAG, TAG_MQTT + "subscribeTopic()>>onNext()>>rxMqttMessage: " + rxMqttMessage.getMessage());
                    } catch (Exception ex) {
                        Logger.d(TAG, TAG_MQTT + "subscribeTopic()>>onNext()>>Exception: " + ex.getMessage());
                    }
                }
            });
        }
    }

    private void destroyMQTT() {
        if (rxMqttClient != null) {
            rxMqttClient.disconnect().subscribe(new Observer<IMqttToken>() {
                @Override
                public void onCompleted() {
                    Logger.d(TAG, TAG_MQTT + "disconnect()>>onCompleted()");
                }

                @Override
                public void onError(Throwable e) {
                    Logger.d(TAG, TAG_MQTT + "disconnect()>>onError()>> e: " + e.getMessage());
                }

                @Override
                public void onNext(IMqttToken iMqttToken) {
                    try {
                        Logger.d(TAG, TAG_MQTT + "disconnect()>>onNext()>>iMqttToken: " + new String(iMqttToken.getResponse().getPayload(), "UTF-8"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    /********************
     * Activity methods *
     ********************/
    private void enableKnobs(boolean isKnobEnable){
//        crollerFan.setEnabled(isKnobEnable);
//        crollerLight1.setEnabled(isKnobEnable);
    }

    public void setRightMenu(boolean visibility, int resId, View.OnClickListener onClickListener) {
        rightMenu.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        if (resId > 0) {
            rightMenu.setBackgroundResource(resId);
        }
        rightMenu.setOnClickListener(onClickListener);
    }

    private void initImageSlider(List<Image> productImages) {
        if (productImages != null && productImages.size() > 0) {
            imageSliderAdapterDevice = new ImageSliderAdapter(getActivity());
            imageSliderAdapterDevice.setData(productImages);

            sliderViewDevice.setSliderAdapter(imageSliderAdapterDevice);
            sliderViewDevice.setIndicatorVisibility(false);
            sliderViewDevice.setSliderTransformAnimation(SliderAnimations.getRandomSliderTransformAnimation());
            sliderViewDevice.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderViewDevice.startAutoCycle();
        }
    }

    private void initDeviceInfo(Device device) {
        mUser = SessionUtil.getUser(getActivity());
        if (mUser == null) {
            return;
        }
        if (device == null) {
            return;
        }

        mUserId = mUser.getUser_id();
        mDeviceId = device.getDevice_id();
        setDetailTypeView(device);
        refreshFavoriteView(device);
        refreshConnectedConnectionView(device.getDevice_configuration());
        initMQTT(getHost(), getPort(), getClientID(), getTopic());
    }

    public void setDetailTypeView(Device device) {
        if (device != null && mDeviceType != null) {
            switch (mDeviceType) {
                case WIFI_MOTOR_ONE:
                    multiWaveHeaderTank.setVisibility(View.VISIBLE);
                    sliderViewDevice.setVisibility(View.GONE);
                    llWifiMotorOne.setVisibility(View.VISIBLE);
                    llWifiGangBoardFive.setVisibility(View.GONE);
                    llWifiMotorTwo.setVisibility(View.GONE);
                    break;
                case WIFI_MOTOR_TWO:
                    multiWaveHeaderTank.setVisibility(View.VISIBLE);
                    sliderViewDevice.setVisibility(View.GONE);
                    llWifiGangBoardFive.setVisibility(View.GONE);
                    llWifiMotorOne.setVisibility(View.GONE);
                    llWifiMotorTwo.setVisibility(View.VISIBLE);
                    break;
                case WIFI_GANG_BOARD_FIVE:
                    sliderViewDevice.setVisibility(View.VISIBLE);
                    llWifiGangBoardFive.setVisibility(View.VISIBLE);
                    multiWaveHeaderTank.setVisibility(View.GONE);
                    llWifiMotorOne.setVisibility(View.GONE);
                    llWifiMotorTwo.setVisibility(View.GONE);
                    initImageSlider(device.getProduct().getProduct_images());
                    break;
                case WIFI_2_PIN_SOCKET_ONE:
                    sliderViewDevice.setVisibility(View.VISIBLE);
                    multiWaveHeaderTank.setVisibility(View.GONE);
                    llWifiGangBoardFive.setVisibility(View.GONE);
                    llWifiMotorOne.setVisibility(View.GONE);
                    llWifiMotorTwo.setVisibility(View.GONE);
                    initImageSlider(device.getProduct().getProduct_images());
                    break;
                case WIFI_HOLDER_PIN:
                    sliderViewDevice.setVisibility(View.VISIBLE);
                    multiWaveHeaderTank.setVisibility(View.GONE);
                    llWifiGangBoardFive.setVisibility(View.GONE);
                    llWifiMotorOne.setVisibility(View.GONE);
                    llWifiMotorTwo.setVisibility(View.GONE);
                    initImageSlider(device.getProduct().getProduct_images());
                    break;
                case WIFI_HOLDER_PATCH:
                    sliderViewDevice.setVisibility(View.VISIBLE);
                    multiWaveHeaderTank.setVisibility(View.GONE);
                    llWifiGangBoardFive.setVisibility(View.GONE);
                    llWifiMotorOne.setVisibility(View.GONE);
                    llWifiMotorTwo.setVisibility(View.GONE);
                    initImageSlider(device.getProduct().getProduct_images());
                    break;
            }
        }
    }

    private void refreshFavoriteView(Device device) {
        AppUtil.applyViewTint(ivDeviceDetailFavorite, ((device.getIs_favorite() == 1) ? R.color.colorPrimaryDark : R.color.colorWhite));
    }

    private void refreshConnectedConnectionView(Configuration configuration) {
        if (configuration != null) {
            ConnectionType connectionType = ConnectionType.getConnectionType(configuration.getConnection().getConnection_name());
            if (connectionType != null) {
                switch (connectionType) {
                    case WIFI:
                        ivConnectedConnection.setBackgroundResource(R.drawable.vector_wifi_primary);
                        break;
                    case BLUETOOTH:
                        ivConnectedConnection.setBackgroundResource(R.drawable.vector_bluetooth);
                        break;
                    case MOBILE_DATA:
                        ivConnectedConnection.setBackgroundResource(R.drawable.vector_mobile_data);
                        break;
                }

                AppUtil.applyViewTint(ivConnectedConnection, R.color.colorGreen);
                tvConnectedConnectionName.setText(configuration.getName());
            }
        }
    }

    /********************
     * Settings methods *
     ********************/
    private void initSettings(Device device) {
        // Set menu view
        flourish = new Flourish.Builder(parentLayout)
                .setFlourishLayout(R.layout.layout_device_detail_settings)
                .setFlourishAnimation(FlourishAnimation.BOUNCE)
                .setFlourishOrientation(FlourishOrientation.TOP_RIGHT)
                .setIsShowedOnStart(false)
                .setDuration(800L)
                .build();
        // Initialize view
        // Settings configuration
        LinearLayout llDeviceSettingsConfigurationWifi = (LinearLayout) flourish.flourishView.findViewById(R.id.ll_device_settings_configuration_wifi);
        LinearLayout llDeviceSettingsConfigurationBluetooth = (LinearLayout) flourish.flourishView.findViewById(R.id.ll_device_settings_configuration_bluetooth);
        LinearLayout llDeviceSettingsConfigurationMobileData = (LinearLayout) flourish.flourishView.findViewById(R.id.ll_device_settings_configuration_mobile_data);
        ImageView ivDeviceSettingsConfigurationWifi = (ImageView) flourish.flourishView.findViewById(R.id.iv_device_settings_configuration_wifi);
        ImageView ivDeviceSettingsConfigurationBluetooth = (ImageView) flourish.flourishView.findViewById(R.id.iv_device_settings_configuration_bluetooth);
        ImageView ivDeviceSettingsConfigurationMobileData = (ImageView) flourish.flourishView.findViewById(R.id.iv_device_settings_configuration_mobile_data);
        TextView tvDeviceSettingsConfigurationWifi = (TextView) flourish.flourishView.findViewById(R.id.tv_device_settings_configuration_wifi);
        TextView tvDeviceSettingsConfigurationBluetooth = (TextView) flourish.flourishView.findViewById(R.id.tv_device_settings_configuration_bluetooth);
        TextView tvDeviceSettingsConfigurationMobileData = (TextView) flourish.flourishView.findViewById(R.id.tv_device_settings_configuration_mobile_data);
        // Settings Notification
        LinearLayout llDeviceSettingsNotificationWm1 = (LinearLayout) flourish.flourishView.findViewById(R.id.ll_device_settings_notification_wm1);
        LinearLayout llDeviceSettingsNotificationWm2 = (LinearLayout) flourish.flourishView.findViewById(R.id.ll_device_settings_notification_wm2);
        LinearLayout llDeviceSettingsNotificationWgb5 = (LinearLayout) flourish.flourishView.findViewById(R.id.ll_device_settings_notification_wgb5);
        SwitchIconView switchWgb5FanSpeed = (SwitchIconView) flourish.flourishView.findViewById(R.id.switch_wgb5_fan_speed);
        // Notification wifi motor one
        SwitchIconView switchWm1OnlyNotify = (SwitchIconView) flourish.flourishView.findViewById(R.id.switch_wm1_only_notify);
        SwitchIconView switchWm1NotifyAndStart = (SwitchIconView) flourish.flourishView.findViewById(R.id.switch_wm1_notify_and_start);
        // Notification wifi motor two
        SwitchIconView switchWm2OnlyNotify = (SwitchIconView) flourish.flourishView.findViewById(R.id.switch_wm2_only_notify);
        SwitchIconView switchWm2NotifyAndStart = (SwitchIconView) flourish.flourishView.findViewById(R.id.switch_wm2_notify_and_start);

        if (device != null && mDeviceType != null) {
            switch (mDeviceType) {
                case WIFI_MOTOR_ONE:
                    // Notification
                    llDeviceSettingsNotificationWm1.setVisibility(View.VISIBLE);
                    llDeviceSettingsNotificationWm2.setVisibility(View.GONE);
                    llDeviceSettingsNotificationWgb5.setVisibility(View.GONE);
                    break;
                case WIFI_MOTOR_TWO:
                    // Notification
                    llDeviceSettingsNotificationWm1.setVisibility(View.GONE);
                    llDeviceSettingsNotificationWm2.setVisibility(View.VISIBLE);
                    llDeviceSettingsNotificationWgb5.setVisibility(View.GONE);
                    break;
                case WIFI_GANG_BOARD_FIVE:
                    // Notification
                    llDeviceSettingsNotificationWm1.setVisibility(View.GONE);
                    llDeviceSettingsNotificationWm2.setVisibility(View.GONE);
                    llDeviceSettingsNotificationWgb5.setVisibility(View.VISIBLE);
                    break;
                case WIFI_2_PIN_SOCKET_ONE:
                    break;
                case WIFI_HOLDER_PIN:
                    break;
                case WIFI_HOLDER_PATCH:
                    break;
            }
        }

        // Set previous fan settings
        switchWgb5FanSpeed.setIconEnabled(SessionUtil.isControlFanSpeed(getActivity(), device.getDevice_id()));
        // Set connections views
        // Initially set all connections gone and then visible one by one depending on existence
        llDeviceSettingsConfigurationWifi.setVisibility(View.GONE);
        llDeviceSettingsConfigurationBluetooth.setVisibility(View.GONE);
        llDeviceSettingsConfigurationMobileData.setVisibility(View.GONE);
        for (Connection connection : mDevice.getProduct().getProduct_connections()) {
            if (connection.getConnection_name().equalsIgnoreCase(ConnectionType.WIFI.getValue())) {
                llDeviceSettingsConfigurationWifi.setVisibility(View.VISIBLE);
            } else if (connection.getConnection_name().equalsIgnoreCase(ConnectionType.BLUETOOTH.getValue())) {
                llDeviceSettingsConfigurationBluetooth.setVisibility(View.VISIBLE);
            } else if (connection.getConnection_name().equalsIgnoreCase(ConnectionType.MOBILE_DATA.getValue())) {
                llDeviceSettingsConfigurationMobileData.setVisibility(View.VISIBLE);
            }
        }
        // Select connected connection
        AppUtil.applyViewTint(ivDeviceSettingsConfigurationWifi, R.color.colorBlack);
        tvDeviceSettingsConfigurationWifi.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
        AppUtil.applyViewTint(ivDeviceSettingsConfigurationBluetooth, R.color.colorBlack);
        tvDeviceSettingsConfigurationBluetooth.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
        AppUtil.applyViewTint(ivDeviceSettingsConfigurationMobileData, R.color.colorBlack);
        tvDeviceSettingsConfigurationMobileData.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
        Configuration selectedConfiguration = mDevice.getDevice_configuration();
        if (selectedConfiguration != null) {
            if (selectedConfiguration.getConnection().getConnection_name().equalsIgnoreCase(ConnectionType.WIFI.getValue())) {
                AppUtil.applyViewTint(ivDeviceSettingsConfigurationWifi, R.color.colorGreen);
                tvDeviceSettingsConfigurationWifi.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            } else if (selectedConfiguration.getConnection().getConnection_name().equalsIgnoreCase(ConnectionType.BLUETOOTH.getValue())) {
                AppUtil.applyViewTint(ivDeviceSettingsConfigurationBluetooth, R.color.colorGreen);
                tvDeviceSettingsConfigurationBluetooth.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            } else if (selectedConfiguration.getConnection().getConnection_name().equalsIgnoreCase(ConnectionType.MOBILE_DATA.getValue())) {
                AppUtil.applyViewTint(ivDeviceSettingsConfigurationMobileData, R.color.colorGreen);
                tvDeviceSettingsConfigurationMobileData.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            }
        }

        // Settings actions
        setToolBarTitle(((AnimatedTextView) flourish.flourishView.findViewById(R.id.toolbar_title)), getString(R.string.title_activity_device_detail_settings));
        setRightMenu(true, R.drawable.vector_settings_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flourish.show();
            }
        });
        flourish.flourishView.findViewById(R.id.left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flourish.dismiss();
            }
        });
        llDeviceSettingsConfigurationWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataUtil.hasConfiguration(mDevice, ConnectionType.WIFI)) {
                    Configuration configuration = DataUtil.getConfiguration(mDevice, ConnectionType.WIFI);
                    if (configuration != null) {
                        AppUtil.applyViewTint(ivDeviceSettingsConfigurationWifi, R.color.colorGreen);
                        tvDeviceSettingsConfigurationWifi.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                        AppUtil.applyViewTint(ivDeviceSettingsConfigurationBluetooth, R.color.colorBlack);
                        tvDeviceSettingsConfigurationBluetooth.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                        AppUtil.applyViewTint(ivDeviceSettingsConfigurationMobileData, R.color.colorBlack);
                        tvDeviceSettingsConfigurationMobileData.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                        mDevice.setDevice_configuration(configuration);
                        Logger.d(TAG, "selected configuration: " + mDevice.getDevice_configuration().toString());
                        refreshConnectedConnectionView(configuration);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please configure your connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        llDeviceSettingsConfigurationBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataUtil.hasConfiguration(mDevice, ConnectionType.BLUETOOTH)) {
                    Configuration configuration = DataUtil.getConfiguration(mDevice, ConnectionType.BLUETOOTH);
                    if (configuration != null) {
                        AppUtil.applyViewTint(ivDeviceSettingsConfigurationWifi, R.color.colorBlack);
                        tvDeviceSettingsConfigurationWifi.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                        AppUtil.applyViewTint(ivDeviceSettingsConfigurationBluetooth, R.color.colorGreen);
                        tvDeviceSettingsConfigurationBluetooth.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                        AppUtil.applyViewTint(ivDeviceSettingsConfigurationMobileData, R.color.colorBlack);
                        tvDeviceSettingsConfigurationMobileData.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                        mDevice.setDevice_configuration(configuration);
                        Logger.d(TAG, "selected configuration: " + mDevice.getDevice_configuration().toString());
                        refreshConnectedConnectionView(configuration);
                    }
                } else {
                    showConfigurationDialog(ConnectionType.BLUETOOTH);
                }
            }
        });
        llDeviceSettingsConfigurationMobileData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataUtil.hasConfiguration(mDevice, ConnectionType.MOBILE_DATA)) {
                    Configuration configuration = DataUtil.getConfiguration(mDevice, ConnectionType.MOBILE_DATA);
                    if (configuration != null) {
                        AppUtil.applyViewTint(ivDeviceSettingsConfigurationWifi, R.color.colorBlack);
                        tvDeviceSettingsConfigurationWifi.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                        AppUtil.applyViewTint(ivDeviceSettingsConfigurationBluetooth, R.color.colorBlack);
                        tvDeviceSettingsConfigurationBluetooth.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                        AppUtil.applyViewTint(ivDeviceSettingsConfigurationMobileData, R.color.colorGreen);
                        tvDeviceSettingsConfigurationMobileData.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                        mDevice.setDevice_configuration(configuration);
                        Logger.d(TAG, "selected configuration: " + mDevice.getDevice_configuration().toString());
                        refreshConnectedConnectionView(configuration);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please configure your connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Wifi Gang Board Five actions
        flourish.flourishView.findViewById(R.id.rl_wgb5_fan_speed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isControlFanSpeed = !SessionUtil.isControlFanSpeed(getActivity(), device.getDevice_id());
                switchWgb5FanSpeed.setIconEnabled(isControlFanSpeed);
                SessionUtil.setControlFanSpeed(getActivity(), device.getDevice_id(), isControlFanSpeed);
            }
        });
        flourish.flourishView.findViewById(R.id.rl_wgb5_timer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSetTimer = new Intent(getActivity(), SetTimerActivity.class);
                intentSetTimer.putExtra(AllConstants.INTENT_KEY_EXTRA_DEVICE, Parcels.wrap(mDevice));
                startActivity(intentSetTimer);
            }
        });
        // Wifi Motor One actions
        flourish.flourishView.findViewById(R.id.rl_wm1_only_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean isControlFanSpeed = !SessionUtil.isControlFanSpeed(getActivity(), device.getDevice_id());
//                switchWgb5FanSpeed.t asetIconEnabled(isControlFanSpeed);
//                SessionUtil.setControlFanSpeed(getActivity(), device.getDevice_id(), isControlFanSpeed);
            }
        });
        flourish.flourishView.findViewById(R.id.rl_wm1_notify_and_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        flourish.flourishView.findViewById(R.id.rl_wm1_min_water_level).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        // Wifi Motor two actions
        flourish.flourishView.findViewById(R.id.rl_wm2_only_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        flourish.flourishView.findViewById(R.id.rl_wm2_notify_and_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        flourish.flourishView.findViewById(R.id.rl_wm2_min_water_level).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        // Caution settings actions
        flourish.flourishView.findViewById(R.id.rl_device_settings_reboot_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        flourish.flourishView.findViewById(R.id.rl_device_settings_clear_configuration_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        flourish.flourishView.findViewById(R.id.rl_device_settings_restore_to_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        flourish.flourishView.findViewById(R.id.rl_device_settings_remove_this_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void showConnectionErrorDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PopupDialog.create(getActivity())
                        .outsideTouchHide(false)
                        .dismissTime(1000 * 5)
                        .controller(AlertController.build()
                                .title(getString(R.string.txt_connection_lost))
                                .message(getString(R.string.txt_connection_between_device_and_mobile_is_lost))
                                .clickDismiss(true)
                                .negativeButton(getString(R.string.dialog_cancel), null)
                                .positiveButton(getString(R.string.dialog_ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        connectToMQTT();
                                    }
                                }))
                        .show();
            }
        });
    }

    private void showConfigurationDialog(ConnectionType connectionType) {
        PopupDialog.create(getActivity())
                .outsideTouchHide(false)
                .dismissTime(1000 * 5)
                .controller(AlertController.build()
                        .title(getString(R.string.txt_need_configuration))
                        .message(String.format(getString(R.string.txt_you_dont_have_any_configuration), connectionType.getValue()))
                        .clickDismiss(true)
                        .negativeButton(getString(R.string.dialog_cancel), null)
                        .positiveButton(getString(R.string.dialog_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (connectionType) {
                                    case WIFI:
                                        break;
                                    case BLUETOOTH:
                                        Intent intentBluetooth = new Intent(getActivity(), BluetoothConfigurationActivity.class);
                                        startActivity(intentBluetooth);
                                        break;
                                    case MOBILE_DATA:
                                        break;
                                }
                            }
                        }))
                .show();
    }
}