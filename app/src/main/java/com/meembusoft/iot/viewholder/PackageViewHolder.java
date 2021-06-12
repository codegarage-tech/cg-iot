package com.meembusoft.iot.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.iot.R;
import com.meembusoft.iot.activity.ProductDetailActivity;
import com.meembusoft.iot.adapter.PackageAdapter;
import com.meembusoft.iot.model.Subscription;
import com.meembusoft.iot.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;

public class PackageViewHolder extends BaseViewHolder<Subscription> {

    private TextView tvPackageName, tvProductPrice, tvProductPriceDescription, tvSubscriptionPrice, tvSubscriptionPriceDescription, tvTotalPrice;
    private LinearLayout llContainerPackage;
    private Context mContext;

    public PackageViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.row_product_detail_package);
        mContext = context;

        tvPackageName = (TextView) itemView.findViewById(R.id.tv_package_name);
        tvProductPrice = (TextView) itemView.findViewById(R.id.tv_product_price);
        tvProductPriceDescription = (TextView) itemView.findViewById(R.id.tv_product_price_description);
        tvSubscriptionPrice = (TextView) itemView.findViewById(R.id.tv_subscription_price);
        tvSubscriptionPriceDescription = (TextView) itemView.findViewById(R.id.tv_subscription_price_description);
        tvTotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
        llContainerPackage = (LinearLayout) itemView.findViewById(R.id.ll_container_package);
    }

    @Override
    public void setData(final Subscription data) {
        if (data.isSelected()) {
            AppUtil.applyViewTint(llContainerPackage, R.color.colorPrimary);
        } else {
            AppUtil.applyViewTint(llContainerPackage, R.color.white);
        }

        tvPackageName.setText(data.getSubscription_name());
        tvProductPrice.setText(((ProductDetailActivity) getContext()).mProduct.getProduct_price() + " " + getContext().getString(R.string.txt_taka_symbol));
        tvProductPriceDescription.setText(getContext().getString(R.string.txt_one_time));
        tvSubscriptionPrice.setText(data.getSubscription_price() + " " + getContext().getString(R.string.txt_taka_symbol));
        tvSubscriptionPriceDescription.setText(
                getContext().getString(R.string.txt_first_bracket_left)
                        + data.getSubscription_description()
                        + getContext().getString(R.string.txt_first_bracket_right)
        );
        tvTotalPrice.setText((((ProductDetailActivity) getContext()).mProduct.getProduct_price() + data.getSubscription_price()) + " " + getContext().getString(R.string.txt_taka_symbol));
        llContainerPackage.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                PackageAdapter packageAdapter = (PackageAdapter) getOwnerAdapter();
                for (Subscription subscription : packageAdapter.getAllData()) {
                    if (subscription.getSubscription_name().equalsIgnoreCase(data.getSubscription_name())) {
                        subscription.setSelected(true);
                    } else {
                        subscription.setSelected(false);
                    }
                }
                packageAdapter.notifyDataSetChanged();
            }
        });
    }
}