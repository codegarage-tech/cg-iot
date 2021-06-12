package com.meembusoft.iot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.fcmmanager.util.SpacingItemDecoration;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.enumeration.ScreenType;
import com.meembusoft.iot.model.Category;
import com.meembusoft.iot.model.Product;
import com.meembusoft.iot.section.ProductSection;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.DataUtil;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.util.SessionUtil;
import com.meembusoft.retrofitmanager.APIResponse;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.List;

import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SelectProductActivity extends BaseActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;

    //Recycler view
    private RecyclerView rvProduct;
    private SectionedRecyclerViewAdapter adapterProduct;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_select_device;
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

    }

    @Override
    public void initActivityViews() {
        //toolbar view
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbar_title);
        leftMenu = (AnimatedImageView) findViewById(R.id.left_menu);
        rightMenu = (ImageView) findViewById(R.id.right_menu);
        rightMenu.setVisibility(View.GONE);
        rvProduct = (RecyclerView) findViewById(R.id.rv_product);

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.title_fragment_choose_device));

        initSectionedRecyclerView();

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });

        rightMenu.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                Intent iSelectedDevice = new Intent();
                setResult(RESULT_OK, iSelectedDevice);
                finish();
            }
        });

    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissProgressDialog();


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

    public void setToolBarTitle(String title) {
        toolbarTitle.setAnimatedText(title, 0L);

        //For marquee address
        AppUtil.applyMarqueeOnTextView(toolbarTitle);
    }

    public void setToolBarSelectRightImage(Product selectProduct){

        if (selectProduct != null ) {
            if (selectProduct.isSelected()) {
                rightMenu.setVisibility(View.VISIBLE);
                rightMenu.setImageResource(R.drawable.vector_accepted_white);
            } else {
                rightMenu.setVisibility(View.GONE);
            }
        }

    }

    /***************************
     * Sectioned Recycler View *
     ***************************/
    private void initSectionedRecyclerView() {
//        deletedLastSelectedDevice();

        adapterProduct = new SectionedRecyclerViewAdapter();
        if (NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), "Need to request from server\nNow uses offline data only", Toast.LENGTH_SHORT).show();
        }

        List<Category> offlineCategories = DataUtil.getAllCategories(getActivity());
        Logger.d(TAG, "offlineCategories: " + offlineCategories.toString());
        if (offlineCategories.size() > 0) {
            for (Category category : offlineCategories) {
                adapterProduct.addSection(category.getCategory_name(), new ProductSection(getActivity(), ScreenType.ADD_DEVICE, category.getCategory_name(), category.getProducts(), rvProduct));
            }
        }


        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
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
            rightMenu.setVisibility(View.VISIBLE);
            rightMenu.setImageResource(R.drawable.vector_accepted_white);
        } else {
            rightMenu.setVisibility(View.GONE);
        }
    }

}