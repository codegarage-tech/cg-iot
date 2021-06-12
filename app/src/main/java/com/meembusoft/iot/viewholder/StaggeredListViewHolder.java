package com.meembusoft.iot.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import com.meembusoft.iot.R;
import com.meembusoft.iot.model.StaggeredListItem;

public class StaggeredListViewHolder extends BaseViewHolder<StaggeredListItem> {

    private TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6;
    private LinearLayout ll_1, ll_2, ll_3, ll_4, ll_5, ll_6;

    public StaggeredListViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.row_staggered_item);

        tv_1 = $(R.id.tv_1);
        tv_2 = $(R.id.tv_2);
        tv_3 = $(R.id.tv_3);
        tv_4 = $(R.id.tv_4);
        tv_5 = $(R.id.tv_5);
        tv_6 = $(R.id.tv_6);

        ll_1 = $(R.id.ll_1);
        ll_2 = $(R.id.ll_2);
        ll_3 = $(R.id.ll_3);
        ll_4 = $(R.id.ll_4);
        ll_5 = $(R.id.ll_5);
        ll_6 = $(R.id.ll_6);

        ll_1.setVisibility(View.GONE);
        ll_2.setVisibility(View.GONE);
        ll_3.setVisibility(View.GONE);
        ll_4.setVisibility(View.GONE);
        ll_5.setVisibility(View.GONE);
        ll_6.setVisibility(View.GONE);
    }

    @Override
    public void setData(final StaggeredListItem data) {
        if (data.getItems().size() > 0) {
            for (int i = 0; i < data.getItems().size(); i++) {
                if (i == 0) {
                    tv_1.setText(data.getItems().get(i).getName());
                    ll_1.setVisibility(View.VISIBLE);
                } else if (i == 1) {
                    tv_2.setText(data.getItems().get(i).getName());
                    ll_2.setVisibility(View.VISIBLE);
                } else if (i == 2) {
                    tv_3.setText(data.getItems().get(i).getName());
                    ll_3.setVisibility(View.VISIBLE);
                } else if (i == 3) {
                    tv_4.setText(data.getItems().get(i).getName());
                    ll_4.setVisibility(View.VISIBLE);
                } else if (i == 4) {
                    tv_5.setText(data.getItems().get(i).getName());
                    ll_5.setVisibility(View.VISIBLE);
                } else if (i == 5) {
                    tv_6.setText(data.getItems().get(i).getName());
                    ll_6.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}