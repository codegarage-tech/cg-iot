package tech.codegarage.iot.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.labo.kaji.fragmentanimations.PushPullAnimation;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.List;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import tech.codegarage.iot.R;
import tech.codegarage.iot.activity.BarCodeScannerActivity;
import tech.codegarage.iot.activity.HomeActivity;
import tech.codegarage.iot.adapter.WifiAdapter;
import tech.codegarage.iot.base.BaseFragment;
import tech.codegarage.iot.base.BaseUpdateListener;
import tech.codegarage.iot.enumeration.ScreenType;
import tech.codegarage.iot.enumeration.SlideUpType;
import tech.codegarage.iot.model.Device;
import tech.codegarage.iot.model.Product;
import tech.codegarage.iot.model.WifiScanResult;
import tech.codegarage.iot.retrofit.APIResponse;
import tech.codegarage.iot.section.DeviceSection;
import tech.codegarage.iot.stepper.ChooseDeviceStep;
import tech.codegarage.iot.stepper.ConnectDevice;
import tech.codegarage.iot.util.DataUtil;
import tech.codegarage.iot.util.KeyboardManager;
import tech.codegarage.iot.util.Logger;
import tech.codegarage.iot.util.ResponseOfflineDevice;
import tech.codegarage.iot.util.SessionUtil;

import static android.app.Activity.RESULT_OK;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static tech.codegarage.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;
import static tech.codegarage.iot.util.AllConstants.SOFT_KEYBOARD_HIDE_DURATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AddDeviceFragment extends BaseFragment {

    // Slide up view
    private View viewSlideUp, rootView;
    private SlideUpBuilder slideUpBuilder;
    private SlideUp slideUp;
    private TextView tvSlideUpTitle;
    private ImageView ivSlideUpClose, ivSlideUpTick;
    private LinearLayout llSlideUpChooseDevice, llSlideUpConnectDevice;
    private RecyclerView rvWifi;
    private WifiAdapter adapterWifi;

    // Sectioned recycler view
    private RecyclerView rvProduct;
    private SectionedRecyclerViewAdapter adapterProduct;

    // Vertical stepper form
    private ProgressDialog progressDialog;
    private VerticalStepperFormView verticalStepperForm;
    private ChooseDeviceStep stepChooseDevice;
    private ConnectDevice stepConnectDevice;
    public static final String STATE_DEVICE_ADDED = "STATE_DEVICE_ADDED";
    public static final String STATE_CHOOSE_DEVICE = "STATE_CHOOSE_DEVICE";
    public static final String STATE_CONNECT_DEVICE = "STATE_CONNECT_DEVICE";

    public static AddDeviceFragment newInstance() {
        AddDeviceFragment fragment = new AddDeviceFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_add_device;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        rootView = parentView.findViewById(R.id.rootView);
        viewSlideUp = parentView.findViewById(R.id.slider_view_container);
        verticalStepperForm = parentView.findViewById(R.id.stepper_form);
        tvSlideUpTitle = parentView.findViewById(R.id.tv_slide_up_title);
        ivSlideUpClose = parentView.findViewById(R.id.iv_slide_up_close);
        ivSlideUpTick = parentView.findViewById(R.id.iv_slide_up_tick);
        rvProduct = parentView.findViewById(R.id.rv_product);
        rvWifi = parentView.findViewById(R.id.rv_wifi);
        llSlideUpChooseDevice = parentView.findViewById(R.id.ll_slide_up_choose_device);
        llSlideUpConnectDevice = parentView.findViewById(R.id.ll_slide_up_connect_device);

        // Initialize slide up view
        initSlideUpView(SlideUpType.CHOOSE_DEVICE);

        // Initialize Vertical stepper form
        initVerticalStepperForm();

        // Initialize wifi list
        initWifiRecyclerView();
    }

    @Override
    public void initFragmentViewsData() {

    }

    @Override
    public void initFragmentActions() {
        ivSlideUpClose.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                slideUp.hide();
            }
        });

        ivSlideUpTick.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                switch (SlideUpType.getSlideUpType(slideUp.getSlideUpType())) {
                    case CHOOSE_DEVICE:
                        Device mDevice = SessionUtil.getLastSelectedDevice(getActivity());
                        if (mDevice != null) {
                            slideUp.hide();
                            stepChooseDevice.setChoseDevice(mDevice);
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.txt_please_select_one_device), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case CONNECT_DEVICE:
                        WifiScanResult selectedWifi = adapterWifi.getSelectedData();
                        if (selectedWifi != null) {
                            slideUp.hide();
                            stepConnectDevice.setChoseConnection(selectedWifi.getSsid(), selectedWifi.getBssid(), selectedWifi.getFrequency(), true);
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.txt_please_select_one_wifi), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void initFragmentBackPress() {
//        finishIfPossible();

//        if (slideUp.isVisible()) {
//            slideUp.hide();
//        } else {
//            getActivity().getSupportFragmentManager().popBackStack();
//        }
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
                        stepChooseDevice.setChoseDevice(device);
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
            return PushPullAnimation.create(PushPullAnimation.LEFT, enter, FRAGMENT_TRANSITION_DURATION);
        } else {
            return PushPullAnimation.create(PushPullAnimation.RIGHT, enter, FRAGMENT_TRANSITION_DURATION);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        deletedLastSelectedDevice();
        stepConnectDevice.destroyTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setRightMenu(false, null);
        ((HomeActivity) getActivity()).setLockMode(true);
    }

    /*****************
     * Slide up view *
     *****************/
    private void initSlideUpView(SlideUpType slideUpType) {
        slideUpBuilder = new SlideUpBuilder(viewSlideUp)
                .withSlideUpType(slideUpType.getValue())
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {

                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {

                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootView);
        slideUp = slideUpBuilder.build();
    }

    private void showSlideUpView(SlideUpType slideUpType) {
        slideUpBuilder.withSlideUpType(slideUpType.getValue());
        slideUp.setSlideUpType(slideUpType.getValue());
        switch (slideUpType) {
            case CHOOSE_DEVICE:
                tvSlideUpTitle.setText(getString(R.string.txt_choose_device));
                llSlideUpChooseDevice.setVisibility(View.VISIBLE);
                llSlideUpConnectDevice.setVisibility(View.GONE);
                initSectionedRecyclerView();
                break;
            case CONNECT_DEVICE:
                tvSlideUpTitle.setText(getString(R.string.txt_connect_device));
                llSlideUpChooseDevice.setVisibility(View.GONE);
                llSlideUpConnectDevice.setVisibility(View.VISIBLE);
                break;
        }
        slideUp.show();
    }

    /***************************
     * Wifi Recycler View *
     ***************************/
    private void initWifiRecyclerView() {
        adapterWifi = new WifiAdapter(getActivity());
        rvWifi.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvWifi.setAdapter(adapterWifi);
    }

    public void initWifiData(WifiInfo wifiInfo, List<ScanResult> scanResults) {
        if (adapterWifi != null) {
            adapterWifi.setAdapterData(wifiInfo, scanResults);
        }
    }

    /***************************
     * Sectioned Recycler View *
     ***************************/
    private void initSectionedRecyclerView() {
//        deletedLastSelectedDevice();

        adapterProduct = new SectionedRecyclerViewAdapter();
        if (NetworkManager.isConnected(getContext())) {
            Toast.makeText(getContext(), "Need to request from server\nNow uses offline data only", Toast.LENGTH_SHORT).show();
        }
        ResponseOfflineDevice offlineDevice = APIResponse.getResponseObject(DataUtil.DEFAULT_DEVICE_LIST, ResponseOfflineDevice.class);
        if (offlineDevice != null) {
            Logger.d(TAG, "offlineDevice: " + offlineDevice.toString());
            if (offlineDevice.getData().size() > 0) {
                for (Product product : offlineDevice.getData()) {
                    adapterProduct.addSection(product.getName(), new DeviceSection(getActivity(), ScreenType.ADD_DEVICE, product.getName(), product.getDevice(), rvProduct));
                }
            }
        }

        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapterProduct.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        rvProduct.setLayoutManager(glm);
        rvProduct.setAdapter(adapterProduct);

        // Set selection if previously selected
        int lastSelectedItemPosition = SessionUtil.getLastSelectedDevicePosition(getActivity());
        String lastSelectedSectionName = SessionUtil.getLastSelectedDeviceSection(getActivity());
        if (lastSelectedItemPosition != -1 && !AllSettingsManager.isNullOrEmpty(lastSelectedSectionName)) {
            DeviceSection mDeviceSection = (DeviceSection) adapterProduct.getSection(lastSelectedSectionName);
            Device mDevice = mDeviceSection.getListItem().get(lastSelectedItemPosition);
            mDevice.setSelected(true);
            adapterProduct.notifyItemChangedInSection(lastSelectedSectionName, lastSelectedItemPosition);
        }
    }

    /*************************
     * Vertical Stepper Form *
     *************************/
    private void initVerticalStepperForm() {
        String[] stepTitles = getResources().getStringArray(R.array.steps_titles);
        stepChooseDevice = new ChooseDeviceStep(stepTitles[0], new BaseUpdateListener() {
            @Override
            public void onUpdate(Object... update) {
                View view = (View) update[0];
                switch (view.getId()) {
                    case R.id.btn_choose_manually:
                        showSlideUpView(SlideUpType.CHOOSE_DEVICE);
                        break;
                    case R.id.btn_scan_qr_code:
                        new IntentIntegrator(getActivity())
                                .setPrompt(getString(R.string.txt_scan_your_qr_code_or_bar_code))
                                .setOrientationLocked(true)
                                .setCaptureActivity(BarCodeScannerActivity.class)
                                .setRequestCode(REQUEST_CODE).initiateScan();
                        break;
                }
            }
        });//, stepSubtitles[0]);
        stepConnectDevice = new ConnectDevice(stepTitles[1], new BaseUpdateListener() {
            @Override
            public void onUpdate(Object... update) {
                if (update[0] instanceof View) {
                    // Process view related tasks
                    View view = (View) update[0];
                    if (view != null) {
                        switch (view.getId()) {
                            case R.id.ll_edit_connection:
                                // Hide device keyboard
                                KeyboardManager.hideKeyboard(getActivity());

                                // Open slide up
                                view.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showSlideUpView(SlideUpType.CONNECT_DEVICE);
                                    }
                                }, SOFT_KEYBOARD_HIDE_DURATION);
                                break;
                        }
                    }
                } else if (update[0] instanceof WifiInfo) {
                    // Process wifi scan result
                    WifiInfo wifiInfo = (WifiInfo) update[0];
                    List<ScanResult> scanResults = (List<ScanResult>) update[1];
                    if (wifiInfo != null && scanResults != null) {
                        initWifiData(wifiInfo, scanResults);
                    }
                }
            }
        });//, stepSubtitles[1]);
        verticalStepperForm.setup(new StepperFormListener() {
            @Override
            public void onCompletedForm() {
                final Thread dataSavingThread = saveData();

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCancelable(true);
                progressDialog.show();
                progressDialog.setMessage(getString(R.string.txt_form_sending_data_message));
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        try {
                            dataSavingThread.interrupt();
                        } catch (RuntimeException e) {
                            // No need to do anything here
                        } finally {
                            verticalStepperForm.cancelFormCompletionOrCancellationAttempt();
                        }
                    }
                });
            }

            @Override
            public void onCancelledForm() {
//                showCloseConfirmationDialog();
            }
        }, stepChooseDevice, stepConnectDevice).init();
    }

    private Thread saveData() {

        // Fake data saving effect
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent intent = getActivity().getIntent();
                    getActivity().setResult(RESULT_OK, intent);
                    intent.putExtra(STATE_DEVICE_ADDED, true);
                    intent.putExtra(STATE_CHOOSE_DEVICE, stepChooseDevice.getStepDataAsHumanReadableString());
                    intent.putExtra(STATE_CONNECT_DEVICE, stepConnectDevice.getStepData());

//                    getActivity().finish();
                    dismissDialogIfNecessary();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        return thread;
    }

    private void finishIfPossible() {
        if (verticalStepperForm.isAnyStepCompleted()) {
//            showCloseConfirmationDialog();
        } else {
//            getActivity().finish();
        }
    }

//    private void showCloseConfirmationDialog() {
//        new DiscardAlarmConfirmationFragment().show(getActivity().getSupportFragmentManager(), null);
//    }

    private void dismissDialogIfNecessary() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    @Override
    public void onPause() {
        super.onPause();

        dismissDialogIfNecessary();
    }

    @Override
    public void onStop() {
        super.onStop();

        dismissDialogIfNecessary();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_CHOOSE_DEVICE, stepChooseDevice.getStepDataAsHumanReadableString());
        savedInstanceState.putString(STATE_CONNECT_DEVICE, stepConnectDevice.getStepData());
        // IMPORTANT: The call to super method must be here at the end
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_CHOOSE_DEVICE)) {
                String title = savedInstanceState.getString(STATE_CHOOSE_DEVICE);
                stepChooseDevice.restoreStepData(title);
            }

            if (savedInstanceState.containsKey(STATE_CONNECT_DEVICE)) {
                String description = savedInstanceState.getString(STATE_CONNECT_DEVICE);
                stepConnectDevice.restoreStepData(description);
            }
        }
    }

//    public static class DiscardAlarmConfirmationFragment extends DialogFragment {
//
//        @Override
//        public void onAttach(Context context) {
//            super.onAttach(context);
//        }
//
//        @Override
//        @NonNull
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle(R.string.form_discard_question)
//                    .setMessage(R.string.form_info_will_be_lost)
//                    .setPositiveButton(R.string.form_discard, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
////                            initFragmentBackPress();
//                        }
//                    })
//                    .setNegativeButton(R.string.form_discard_cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            verticalStepperForm.cancelFormCompletionOrCancellationAttempt();
//                        }
//                    })
//                    .setCancelable(false);
//            Dialog dialog = builder.create();
//            dialog.setCanceledOnTouchOutside(false);
//
//            return dialog;
//        }
//    }

    private void deletedLastSelectedDevice() {
        // Delete last selected device info
        SessionUtil.setLastSelectedDevicePosition(getActivity(), -1);
        SessionUtil.setLastSelectedDeviceSection(getActivity(), "");
        SessionUtil.setLastSelectedDevice(getActivity(), "");
    }
}