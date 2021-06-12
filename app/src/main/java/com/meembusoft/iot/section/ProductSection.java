package com.meembusoft.iot.section;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.iot.R;
import com.meembusoft.iot.activity.ProductDetailActivity;
import com.meembusoft.iot.activity.SelectProductActivity;
import com.meembusoft.iot.enumeration.ScreenType;
import com.meembusoft.iot.model.Product;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.util.SessionUtil;
import com.meembusoft.iot.viewholder.HeaderViewHolder;
import com.meembusoft.iot.viewholder.ItemViewHolder;
import com.meembusoft.retrofitmanager.APIResponse;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_EXTRA_PRODUCT;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ProductSection extends StatelessSection {

    private String TAG = ProductSection.class.getSimpleName();
    private Context mContext;
    private String mSectionTag;
    private List<Product> mListItem;
    private RecyclerView mSectionedRecyclerView;
    private ScreenType mScreenType;

    public ProductSection(Context context, ScreenType screenType, String title, List<Product> mListItem, RecyclerView sectionedRecyclerView) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.section_item)
                .headerResourceId(R.layout.section_header)
                .build());
        this.mContext = context;
        this.mScreenType = screenType;
        this.mSectionTag = title;
        this.mListItem = mListItem;
        this.mSectionedRecyclerView = sectionedRecyclerView;
    }

    @Override
    public int getContentItemsTotal() {
        return mListItem.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        final Product product = mListItem.get(position);
        AppUtil.loadImage(mContext, itemHolder.ivItemImage, product.getProduct_images().get(0).getUrl(), false, false, true);
        itemHolder.tvItemName.setText(product.getProduct_name());
        itemHolder.tvItemPrice.setText(product.getProduct_price() + " TK");
        if (product.isSelected()) {
            itemHolder.llItemTick.setVisibility(View.VISIBLE);
        } else {
            itemHolder.llItemTick.setVisibility(View.GONE);
        }

        if (((position + 1) % 2) == 0) {
            itemHolder.llContainer.setBackgroundResource(R.drawable.shape_item_container_right);
            AppUtil.applyViewTint(itemHolder.llContainer, R.color.white);

            itemHolder.llShadow.setBackgroundResource(R.drawable.shape_item_shadow_right_top_to_bttom);
            itemHolder.rlShadowRight.setVisibility(View.VISIBLE);
            itemHolder.rlShadowLeft.setVisibility(View.GONE);
        } else {
            itemHolder.llContainer.setBackgroundResource(R.drawable.shape_item_container_left);
            AppUtil.applyViewTint(itemHolder.llContainer, R.color.white);

            itemHolder.llShadow.setBackgroundResource(R.drawable.shape_item_shadow_left_bottom_to_top);
            itemHolder.rlShadowLeft.setVisibility(View.VISIBLE);
            itemHolder.rlShadowRight.setVisibility(View.GONE);
        }

        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "Clicked: " + product.toString());
                Logger.d(TAG, "Clicked: " + product.getProduct_connections().toString());
                if (mScreenType == ScreenType.ADD_DEVICE) {
                    int lastSelectedItemPosition = SessionUtil.getLastSelectedDevicePosition(mContext);
                    String lastSelectedSectionName = SessionUtil.getLastSelectedDeviceSection(mContext);

                    // Toggle previous selection
                    if (lastSelectedItemPosition != -1 && !AllSettingsManager.isNullOrEmpty(lastSelectedSectionName)) {
                        ProductSection mProductSection = (ProductSection) ((SectionedRecyclerViewAdapter) mSectionedRecyclerView.getAdapter()).getSection(lastSelectedSectionName);
                        Product mProduct = mProductSection.mListItem.get(lastSelectedItemPosition);
                        if (mProduct != null) {
                            mProduct.setSelected(!mProduct.isSelected());
                            ((SectionedRecyclerViewAdapter) mSectionedRecyclerView.getAdapter()).notifyItemChangedInSection(lastSelectedSectionName, lastSelectedItemPosition);
                        }
                    }

                    product.setSelected(!product.isSelected());
                    SessionUtil.setLastSelectedDevice(mContext, APIResponse.getJSONStringFromObject(product));
                    SessionUtil.setLastSelectedDevicePosition(mContext, ((SectionedRecyclerViewAdapter) mSectionedRecyclerView.getAdapter()).getPositionInSection(itemHolder.getAdapterPosition()));
                    SessionUtil.setLastSelectedDeviceSection(mContext, mSectionTag);
                    ((SectionedRecyclerViewAdapter) mSectionedRecyclerView.getAdapter()).notifyDataSetChanged();

                    SelectProductActivity mActivity = ((SelectProductActivity) mContext);
                    mActivity.setToolBarSelectRightImage(product);

                } else if (mScreenType == ScreenType.PRODUCT) {
                    Intent intentProductDetail = new Intent(mContext, ProductDetailActivity.class);
                    intentProductDetail.putExtra(INTENT_KEY_EXTRA_PRODUCT, Parcels.wrap(product));
                    mContext.startActivity(intentProductDetail);
                }
            }
        });
    }

    public List<Product> getListItem() {
        return mListItem;
    }

    public Product getSelectedItem() {
        int lastSelectedItemPosition = SessionUtil.getLastSelectedDevicePosition(mContext);
        String lastSelectedSectionName = SessionUtil.getLastSelectedDeviceSection(mContext);
        Product mProduct = null;

        if (lastSelectedItemPosition != -1 && !AllSettingsManager.isNullOrEmpty(lastSelectedSectionName)) {
            ProductSection mProductSection = (ProductSection) ((SectionedRecyclerViewAdapter) mSectionedRecyclerView.getAdapter()).getSection(lastSelectedSectionName);
            mProduct = mProductSection.mListItem.get(lastSelectedItemPosition);
        }
        return mProduct;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(mSectionTag);

//        headerHolder.btnMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), String.format("Clicked on more button from the header of Section %s",
//                        mSectionTag),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}