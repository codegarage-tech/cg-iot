package com.meembusoft.iot.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.labo.kaji.fragmentanimations.PushPullAnimation;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.meembusoft.fcmmanager.util.SpacingItemDecoration;
import com.meembusoft.iot.R;
import com.meembusoft.iot.activity.BarCodeScannerActivity;
import com.meembusoft.iot.activity.SelectProductActivity;
import com.meembusoft.iot.base.BaseFragment;
import com.meembusoft.iot.enumeration.ScreenType;
import com.meembusoft.iot.model.Category;
import com.meembusoft.iot.model.Product;
import com.meembusoft.iot.model.Room;
import com.meembusoft.iot.section.ProductSection;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.DataUtil;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.util.SessionUtil;
import com.meembusoft.retrofitmanager.APIResponse;
import com.nex3z.flowlayout.FlowLayout;
import com.nex3z.flowlayout.FlowLayoutManager;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.random.RandomManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static com.meembusoft.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;
import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_REQUEST_CODE_SELECT_DEVICE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SelectProductFragmentTest extends BaseFragment {

    private String TAG = "SelectDeviceFragment";
    private LinearLayout llChooseDeviceAccepted;

    private LinearLayout llQrCode, llManually,llChoseDevice;
    private TextView tvChoseDevice;

    // Slide up view
    private View rootView;

    public static SelectProductFragmentTest newInstance() {
        SelectProductFragmentTest fragment = new SelectProductFragmentTest();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_select_product_test;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        llChooseDeviceAccepted = (LinearLayout) parentView.findViewById(R.id.ll_choose_device_accepted);

        llQrCode = (LinearLayout) parentView.findViewById(R.id.ll_qr_code);
        llManually = (LinearLayout) parentView.findViewById(R.id.ll_manually);
        llChoseDevice = (LinearLayout) parentView.findViewById(R.id.ll_choose_device);
        tvChoseDevice = (TextView) parentView.findViewById(R.id.tv_chose_device);

        // Slide up view
        rootView = parentView.findViewById(R.id.rootView);


    }

    @Override
    public void initFragmentViewsData() {

        //Set last selected device
        Product lastTempChosenProduct = SessionUtil.getTempChosenDevice(getActivity());
        if (lastTempChosenProduct != null) {
            llChoseDevice.setVisibility(View.VISIBLE);
            llChooseDeviceAccepted.setVisibility(View.VISIBLE);
            tvChoseDevice.setText(lastTempChosenProduct.getProduct_name());
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

        llManually.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iSelectProduct = new Intent(getActivity(), SelectProductActivity.class);
                getActivity().startActivityForResult(iSelectProduct, INTENT_KEY_REQUEST_CODE_SELECT_DEVICE);
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
                    Product product = DataUtil.getSpecificProductById(getActivity(),"69");
                    if (product != null) {
                        llChoseDevice.setVisibility(View.VISIBLE);
                        tvChoseDevice.setText(product.getProduct_name());
                        AppUtil.applyMarqueeOnTextView(tvChoseDevice);
                        SessionUtil.setTempChosenDevice(getContext(), APIResponse.getJSONStringFromObject(product));
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.txt_sorry_we_could_not_detect_your_device), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case INTENT_KEY_REQUEST_CODE_SELECT_DEVICE:
                Logger.d(TAG, "<<<INTENT_KEY_REQUEST_CODE_SELECT_DEVICE: " );
                Product product = SessionUtil.getLastSelectedDevice(getActivity());
                if (product != null) {
                    llChoseDevice.setVisibility(View.VISIBLE);
                    tvChoseDevice.setText(product.getProduct_name());
                    AppUtil.applyMarqueeOnTextView(tvChoseDevice);
                    SessionUtil.setTempChosenDevice(getContext(), APIResponse.getJSONStringFromObject(product));
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

    public boolean isAllFieldsVerified() {
        Product tempChosenProduct = SessionUtil.getTempChosenDevice(getActivity());
        if (tempChosenProduct == null) {
            Toast.makeText(getActivity(), getString(R.string.txt_please_choose_one_device), Toast.LENGTH_SHORT).show();
            llChooseDeviceAccepted.setVisibility(View.GONE);
            return false;
        } else {
            llChooseDeviceAccepted.setVisibility(View.VISIBLE);
        }

        return true;
    }



    private void showInputRoomDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_input_room, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(false)
                .setTitle("")
                .setMessage(R.string.txt_please_input_desired_room_name)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the room name
                String roomName = ((EditText) view.findViewById(R.id.edt_room_name)).getText().toString();
                if (TextUtils.isEmpty(roomName)) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.txt_please_input_desired_room_name), Toast.LENGTH_SHORT).show();
                    return;
                }


                dialog.dismiss();
            }
        });
        Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


}