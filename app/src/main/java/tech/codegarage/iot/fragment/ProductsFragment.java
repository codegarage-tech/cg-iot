package tech.codegarage.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import tech.codegarage.iot.R;
import tech.codegarage.iot.activity.HomeActivity;
import tech.codegarage.iot.base.BaseFragment;
import tech.codegarage.iot.enumeration.ScreenType;
import tech.codegarage.iot.model.Product;
import tech.codegarage.iot.retrofit.APIResponse;
import tech.codegarage.iot.section.DeviceSection;
import tech.codegarage.iot.util.DataUtil;
import tech.codegarage.iot.util.Logger;
import tech.codegarage.iot.util.ResponseOfflineDevice;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ProductsFragment extends BaseFragment {

    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    public static ProductsFragment newInstance() {
        ProductsFragment fragment = new ProductsFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_products;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        RecyclerView sectionedRecyclerView = (RecyclerView) parentView.findViewById(R.id.rc_products);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        ResponseOfflineDevice offlineDevice = APIResponse.getResponseObject(DataUtil.DEFAULT_DEVICE_LIST, ResponseOfflineDevice.class);
        if (offlineDevice != null) {
            Logger.d(TAG, "offlineDevice: " + offlineDevice.toString());
            if (offlineDevice.getData().size() > 0) {
                for (Product product : offlineDevice.getData()) {
                    sectionedRecyclerViewAdapter.addSection(product.getName(), new DeviceSection(getActivity(), ScreenType.PRODUCTS, product.getName(), product.getDevice(), sectionedRecyclerView));
                }
            }
        }

        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (sectionedRecyclerViewAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        sectionedRecyclerView.setLayoutManager(glm);
        sectionedRecyclerView.setAdapter(sectionedRecyclerViewAdapter);
    }

    @Override
    public void initFragmentViewsData() {

    }

    @Override
    public void initFragmentActions() {

    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initFragmentUpdate(Object object) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setRightMenu(false, null);
        ((HomeActivity) getActivity()).setLockMode(false);
    }
}