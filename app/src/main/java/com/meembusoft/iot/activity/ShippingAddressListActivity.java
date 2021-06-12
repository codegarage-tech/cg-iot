package com.meembusoft.iot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;

import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ShippingAddressListActivity extends BaseActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;
    private ImageView ivProduct;
    private Button btnPlaceOrder;
    private TextView tvShippingEdit;
    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_shipping_address;
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
        ivProduct = (ImageView) findViewById(R.id.iv_product);
        btnPlaceOrder = (Button) findViewById(R.id.btn_place_order);
        tvShippingEdit = (TextView) findViewById(R.id.tv_shipping_address_edit);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.txt_shipping_address_title));
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });

        tvShippingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAddShipping = new Intent(getActivity(), AddShippingAddressActivity.class);
                startActivity(iAddShipping);
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
}