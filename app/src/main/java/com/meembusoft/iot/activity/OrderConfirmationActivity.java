package com.meembusoft.iot.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;
import com.sweet.yukun.androidsweetsheet.sweetpick.CustomDelegate;
import com.sweet.yukun.androidsweetsheet.sweetpick.SweetSheet;

import cn.ymex.popup.controller.DialogControllable;
import cn.ymex.popup.dialog.PopupDialog;
import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class OrderConfirmationActivity extends BaseActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;
    private ImageView ivProduct;
    private Button btnPlaceOrder;
    private TextView tvPromo;
    private RelativeLayout relShippingAddress;
    private SweetSheet mSweetSheet;
    private RelativeLayout relPaymentMethod;
    private RelativeLayout relMain;
    CustomDelegate customDelegate;
    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_order_confirmation;
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
        tvPromo= (TextView) findViewById(R.id.tv_order_summary_promo);
        btnPlaceOrder = (Button) findViewById(R.id.btn_place_order);
        relShippingAddress = (RelativeLayout) findViewById(R.id.rel_shipping_address);
        relPaymentMethod = (RelativeLayout) findViewById(R.id.rel_payment_method);
        relMain = (RelativeLayout) findViewById(R.id.rel_main);

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.title_activity_order_confirmation));
        setupCustomView();
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });

        btnPlaceOrder.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }
        });

        relShippingAddress.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentShippingAddress = new Intent(getActivity(), ShippingAddressListActivity.class);
                startActivity(intentShippingAddress);
            }
        });

        relPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSweetSheet.toggle();
            }
        });

        tvPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCouponDialogClick();
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

    private void setupCustomView() {
        mSweetSheet = new SweetSheet(relMain);
         customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangLayoutAnimation);
        View view = LayoutInflater.from(OrderConfirmationActivity.this).inflate(R.layout.bottom_sheet_payment_method, null, false);
        ImageView ivClose = (ImageView)view.findViewById(R.id.iv_close);
        ImageView ivCashSelect = (ImageView)view.findViewById(R.id.iv_cash_selected);
        ImageView ivDigitalSelect = (ImageView)view.findViewById(R.id.iv_digital_selected);
        RelativeLayout relCashPayment = (RelativeLayout)view.findViewById(R.id.rel_cash_payment);
        RelativeLayout relDigitalPayment = (RelativeLayout)view.findViewById(R.id.rel_digital_payment);

        customDelegate.setCustomView(view);
        mSweetSheet.setDelegate(getActivity(),customDelegate);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSweetSheet.dismiss();
            }
        });

        relCashPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCashSelect.setVisibility(View.VISIBLE);
                ivDigitalSelect.setVisibility(View.GONE);
            }
        });

        relDigitalPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCashSelect.setVisibility(View.GONE);
                ivDigitalSelect.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setToolBarTitle(String title) {
        toolbarTitle.setAnimatedText(title, 0L);

        //For marquee address
        AppUtil.applyMarqueeOnTextView(toolbarTitle);
    }


    public void onCouponDialogClick() {
        PopupDialog.create(this)
                .animationIn(R.anim.push_in)
                .animationOut(R.anim.push_out)
                .controller(new CustomDialogController()).show();
    }


    class CustomDialogController implements DialogControllable {
        @Override
        public View createView(Context cotext, ViewGroup parent) {
            return LayoutInflater.from(cotext).inflate(R.layout.dialog_view_coupon, parent, false);
        }

        @Override
        public PopupDialog.OnBindViewListener bindView() {


            return new PopupDialog.OnBindViewListener() {
                @Override
                public void onCreated(final PopupDialog dialog, final View layout) {

                    dialog.backPressedHide(true);
                    dialog.outsideTouchHide(false);

                    dialog.backgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));
                    EditText etCouponCode = layout.findViewById(R.id.et_coupon_dialog_enter_code);
                    etCouponCode.requestFocus();
//                    layout.findViewById(R.id.btn_dialog_submit).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                            EditText etCouponCode = layout.findViewById(R.id.et_coupon_code);
////                            String couponCode = etCouponCode.getText().toString();
////                            if (CommonTask.isNullOrEmpty(couponCode)) {
////                                Toast.makeText(getApplicationContext(), "Please enter your coupon code", Toast.LENGTH_SHORT).show();
////                                return;
////                            }
//                        }
//                    });

                    layout.findViewById(R.id.tv_coupon_dialog_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }
            };
        }
    }
}