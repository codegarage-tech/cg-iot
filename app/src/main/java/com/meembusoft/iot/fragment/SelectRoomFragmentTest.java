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
import androidx.cardview.widget.CardView;
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
import com.meembusoft.iot.activity.LoginActivity;
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
import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_REQUEST_CODE_LOGIN;
import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_REQUEST_CODE_SELECT_DEVICE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SelectRoomFragmentTest extends BaseFragment {

    private String TAG = "SelectRoomFragment";
    private FlowLayout flowLayoutRoomName;
    private FlowLayoutManager flowLayoutManagerRoomName;
    private LinearLayout llAddRoom,llSelectRoomAccepted,llChooseDeviceAccepted;
    private List<Room> mSuggestedRoom = new ArrayList<>();
    private YouTubePlayerView youTubePlayerView;

    private LinearLayout llQrCode, llManually,llChoseDevice;
    private TextView tvChoseDevice;

    // Slide up view
    private View viewSlideUp, rootView;
    private SlideUpBuilder slideUpBuilder;
    private SlideUp slideUp;
    private TextView tvSlideUpTitle;
    private ImageView ivSlideUpTick;

    // Sectioned recycler view
    private RecyclerView rvProduct;
    private SectionedRecyclerViewAdapter adapterProduct;
    public static SelectRoomFragmentTest newInstance() {
        SelectRoomFragmentTest fragment = new SelectRoomFragmentTest();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_select_room_test;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        flowLayoutRoomName = (FlowLayout) parentView.findViewById(R.id.fl_room_name);
        llAddRoom = (LinearLayout) parentView.findViewById(R.id.ll_add_room);
        llSelectRoomAccepted = (LinearLayout) parentView.findViewById(R.id.ll_selected_room_accepted);
        llChooseDeviceAccepted = (LinearLayout) parentView.findViewById(R.id.ll_choose_device_accepted);
        youTubePlayerView = parentView.findViewById(R.id.youtube_player_view);

        llQrCode = (LinearLayout) parentView.findViewById(R.id.ll_qr_code);
        llManually = (LinearLayout) parentView.findViewById(R.id.ll_manually);
        llChoseDevice = (LinearLayout) parentView.findViewById(R.id.ll_choose_device);
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
        initSuggestedRoom(DataUtil.getAllRooms(getActivity()));
        // Set select room check
        String lastTempSelectedRoom = SessionUtil.getTempSelectedRoom(getActivity());
        if (lastTempSelectedRoom != null) {
            llSelectRoomAccepted.setVisibility(View.VISIBLE);
        }
        //Set last selected device
        Product lastTempChosenProduct = SessionUtil.getTempChosenDevice(getActivity());
        if (lastTempChosenProduct != null) {
            llChoseDevice.setVisibility(View.VISIBLE);
            llChooseDeviceAccepted.setVisibility(View.VISIBLE);
            tvChoseDevice.setText(lastTempChosenProduct.getProduct_name());
            AppUtil.applyMarqueeOnTextView(tvChoseDevice);
        }

        // Initialize slide up view
        initSlideUpView();

        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "r8kE7rSzfQs";

                youTubePlayer.loadVideo(videoId, 0);
                youTubePlayer.pause();
            }
        });
    }

    @Override
    public void initFragmentActions() {
        llAddRoom.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
//                showInputRoomDialog();
            }
        });

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

        ivSlideUpTick.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // Show selected product
                Product product = SessionUtil.getLastSelectedDevice(getActivity());
                if (product != null) {
                    llChoseDevice.setVisibility(View.VISIBLE);
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
        String lastTempSelectedRoom = SessionUtil.getTempSelectedRoom(getActivity());
        if (AllSettingsManager.isNullOrEmpty(lastTempSelectedRoom)) {
            Toast.makeText(getActivity(), getString(R.string.txt_please_select_one_room), Toast.LENGTH_SHORT).show();
            llSelectRoomAccepted.setVisibility(View.GONE);
            return false;
        } else {
            llSelectRoomAccepted.setVisibility(View.VISIBLE);
        }

        return true;
    }

    /***************************
     * Methods for flow layout *
     ***************************/
    public void addFlowItem(String flowItem) {
        int id = RandomManager.getRandom(100);
        mSuggestedRoom.add(new Room(id + "", flowItem));
        flowLayoutManagerRoomName.addFlowItem(flowItem);
        flowLayoutManagerRoomName.clickFlowView(flowItem);
    }

    public void initSuggestedRoom(List<Room> suggestedRoom) {
        if (suggestedRoom != null && suggestedRoom.size() > 0) {
            mSuggestedRoom.clear();
            mSuggestedRoom.addAll(suggestedRoom);

            //Arrange suggested room's key
            List<String> mSuggestedRoomKeys = new ArrayList<>();
            for (Room mRoom : suggestedRoom) {
                mSuggestedRoomKeys.add(mRoom.getRoom_name());
            }

            //Set flow layout with suggested room key
            flowLayoutManagerRoomName = new FlowLayoutManager.FlowViewBuilder(getActivity(), flowLayoutRoomName, mSuggestedRoomKeys, new FlowLayoutManager.onFlowViewClick() {
                @Override
                public void flowViewClick(TextView updatedTextView) {
                    List<TextView> selectedVoiceSearchKeys = flowLayoutManagerRoomName.getSelectedFlowViews();
                    String tempSelectedRoom = (selectedVoiceSearchKeys.size() > 0) ? selectedVoiceSearchKeys.get(0).getText().toString() : "";
                    Logger.d(TAG, "tempSelectedRoom: " + tempSelectedRoom);

                    //Save temp selected room
                    SessionUtil.setTempSelectedRoom(getActivity(), tempSelectedRoom);
                }
            })
                    .setSingleChoice(true)
                    .build();

            //Set last temp selected room key
            String lastTempSelectedRoom = SessionUtil.getTempSelectedRoom(getActivity());
            if (!AllSettingsManager.isNullOrEmpty(lastTempSelectedRoom)) {
                flowLayoutManagerRoomName.clickFlowView(lastTempSelectedRoom);
            }
        }
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

                // Add the room name into the flow layout
                addFlowItem(roomName);

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


}