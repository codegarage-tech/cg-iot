package com.meembusoft.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.labo.kaji.fragmentanimations.PushPullAnimation;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.meembusoft.fcmmanager.util.SpacingItemDecoration;
import com.meembusoft.iot.model.Product;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.util.AllSettingsManager;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import com.meembusoft.iot.R;
import com.meembusoft.iot.activity.BarCodeScannerActivity;
import com.meembusoft.iot.base.BaseFragment;
import com.meembusoft.iot.enumeration.ScreenType;
import com.meembusoft.iot.model.Category;
import com.meembusoft.retrofitmanager.APIResponse;
import com.meembusoft.iot.section.ProductSection;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.DataUtil;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.util.SessionUtil;

import java.util.List;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static com.meembusoft.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ChooseDeviceFragment extends BaseFragment {

    private LinearLayout llQrCode, llManually;
    private TextView tvChoseDevice;
    private CardView cvChoseDevice;

    // Slide up view
    private View viewSlideUp, rootView;
    private SlideUpBuilder slideUpBuilder;
    private SlideUp slideUp;
    private TextView tvSlideUpTitle;
    private ImageView ivSlideUpTick;

    // Sectioned recycler view
    private RecyclerView rvProduct;
    private SectionedRecyclerViewAdapter adapterProduct;

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

        // Slide up view
        rootView = parentView.findViewById(R.id.rootView);
        viewSlideUp = parentView.findViewById(R.id.slider_view_container);
        rvProduct = parentView.findViewById(R.id.rv_product);
        tvSlideUpTitle = parentView.findViewById(R.id.tv_slide_up_title);
        ivSlideUpTick = parentView.findViewById(R.id.iv_slide_up_tick);
    }

    @Override
    public void initFragmentViewsData() {
        //Set last selected device
        Product lastTempChosenProduct = SessionUtil.getTempChosenDevice(getActivity());
        if (lastTempChosenProduct != null) {
            cvChoseDevice.setVisibility(View.VISIBLE);
            tvChoseDevice.setText(lastTempChosenProduct.getProduct_name());
            AppUtil.applyMarqueeOnTextView(tvChoseDevice);
        }

        // Initialize slide up view
        initSlideUpView();
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
                showSlideUpView();
            }
        });

        ivSlideUpTick.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // Show selected product
                Product product = SessionUtil.getLastSelectedDevice(getActivity());
                if (product != null) {
                    cvChoseDevice.setVisibility(View.VISIBLE);
                    tvChoseDevice.setText(product.getProduct_name());
                    AppUtil.applyMarqueeOnTextView(tvChoseDevice);
                    SessionUtil.setTempChosenDevice(getContext(), APIResponse.getJSONStringFromObject(product));
                }

                // Close slide up panel
                slideUp.hide();
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
                        cvChoseDevice.setVisibility(View.VISIBLE);
                        tvChoseDevice.setText(product.getProduct_name());
                        AppUtil.applyMarqueeOnTextView(tvChoseDevice);
                        SessionUtil.setTempChosenDevice(getContext(), APIResponse.getJSONStringFromObject(product));
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

    /*****************
     * Slide up view *
     *****************/
    private void initSlideUpView() {
        slideUpBuilder = new SlideUpBuilder(viewSlideUp)
                .withSlideUpType(1)
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
                .withGesturesEnabled(false)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootView);
        slideUp = slideUpBuilder.build();
    }

    private void showSlideUpView() {
        slideUpBuilder.withSlideUpType(1);
        slideUp.setSlideUpType(1);
        tvSlideUpTitle.setText(getString(R.string.txt_select_your_device));
        initSectionedRecyclerView();
        slideUp.show();
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

        List<Category> offlineCategories = DataUtil.getAllCategories(getActivity());
            Logger.d(TAG, "offlineCategories: " + offlineCategories.toString());
            if (offlineCategories.size() > 0) {
                for (Category category : offlineCategories) {
                    adapterProduct.addSection(category.getCategory_name(), new ProductSection(getActivity(), ScreenType.ADD_DEVICE, category.getCategory_name(), category.getProducts(), rvProduct));
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

        int horizontal = getResources().getDimensionPixelSize(R.dimen.dp_5);
        int vertical = getResources().getDimensionPixelSize(R.dimen.dp_0);
        rvProduct.addItemDecoration(new SpacingItemDecoration(horizontal, vertical));
        rvProduct.setLayoutManager(glm);
        rvProduct.setAdapter(adapterProduct);

        // Set selection if previously selected
        int lastSelectedItemPosition = SessionUtil.getLastSelectedDevicePosition(getActivity());
        String lastSelectedSectionName = SessionUtil.getLastSelectedDeviceSection(getActivity());
        if (lastSelectedItemPosition != -1 && !AllSettingsManager.isNullOrEmpty(lastSelectedSectionName)) {
            ProductSection mProductSection = (ProductSection) adapterProduct.getSection(lastSelectedSectionName);
            Product mProduct = mProductSection.getListItem().get(lastSelectedItemPosition);
            mProduct.setSelected(true);
            adapterProduct.notifyItemChangedInSection(lastSelectedSectionName, lastSelectedItemPosition);
        }
    }

    public boolean isAllFieldsVerified() {
        Product tempChosenProduct = SessionUtil.getTempChosenDevice(getActivity());
        if (tempChosenProduct != null) {
            return true;
        } else {
            Toast.makeText(getActivity(), getString(R.string.txt_please_choose_one_device), Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}