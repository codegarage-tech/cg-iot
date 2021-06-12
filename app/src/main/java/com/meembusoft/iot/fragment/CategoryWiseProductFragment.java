package com.meembusoft.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.fcmmanager.util.SpacingItemDecoration;
import com.meembusoft.iot.R;
import com.meembusoft.iot.activity.CartActivity;
import com.meembusoft.iot.activity.HomeActivity;
import com.meembusoft.iot.base.BaseFragment;
import com.meembusoft.iot.enumeration.ScreenType;
import com.meembusoft.iot.model.Category;
import com.meembusoft.iot.model.ResponseOfflineCategory;
import com.meembusoft.iot.model.User;
import com.meembusoft.iot.section.ProductSection;
import com.meembusoft.iot.util.AndroidAssetManager;
import com.meembusoft.iot.util.DataUtil;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.util.SessionUtil;
import com.meembusoft.retrofitmanager.APIResponse;
import com.reversecoder.library.event.OnSingleClickListener;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CategoryWiseProductFragment extends BaseFragment {

    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    public static CategoryWiseProductFragment newInstance() {
        CategoryWiseProductFragment fragment = new CategoryWiseProductFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_category_wise_products;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        RecyclerView sectionedRecyclerView = (RecyclerView) parentView.findViewById(R.id.rc_products);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        String jsonResponse = AndroidAssetManager.readTextFileFromAsset(getActivity(), DataUtil.ASSET_FILE_PATH_CATEGORY_WISE_PRODUCT_LIST);
        Logger.d(TAG, TAG + ">>categorywiseproduct>> " + jsonResponse);
        ResponseOfflineCategory offlineDevice = APIResponse.getObjectFromJSONString(jsonResponse, ResponseOfflineCategory.class);
        if (offlineDevice != null) {
            Logger.d(TAG, "offlineDevice: " + offlineDevice.toString());
            if (offlineDevice.getData().size() > 0) {
                for (Category category : offlineDevice.getData()) {
                    sectionedRecyclerViewAdapter.addSection(category.getCategory_name(), new ProductSection(getActivity(), ScreenType.PRODUCT, category.getCategory_name(), category.getProducts(), sectionedRecyclerView));
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

        int horizontal = getResources().getDimensionPixelSize(R.dimen.dp_3);
        int vertical = getResources().getDimensionPixelSize(R.dimen.dp_0);
        sectionedRecyclerView.addItemDecoration(new SpacingItemDecoration(horizontal, vertical));
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
        final User user = SessionUtil.getUser(getActivity());
//        ((HomeActivity) getActivity()).setRightMenu(true, (user != null ? R.drawable.vector_user_tick : R.drawable.vector_user), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (user == null) {
//                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
//                    startActivityForResult(intentLogin, AllConstants.INTENT_KEY_REQUEST_CODE_LOGIN);
//                } else {
//                    // User detail
//                }
//            }
//        });
        ((HomeActivity) getActivity()).setLockMode(false);
        ((HomeActivity) getActivity()).setRightMenu(true, R.drawable.vector_bag_empty_white, new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentCart = new Intent(getActivity(), CartActivity.class);
                getActivity().startActivity(intentCart);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        switchitch (requestCode) {
//            case AllConstants.INTENT_KEY_REQUEST_CODE_LOGIN:
//                if (resultCode == RESULT_OK) {
//                    ((HomeActivity) getActivity()).initNavigationDrawer();
//                }
//                break;
//        }
    }
}