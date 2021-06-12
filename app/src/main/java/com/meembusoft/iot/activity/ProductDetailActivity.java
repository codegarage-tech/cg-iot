package com.meembusoft.iot.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.addtocart.AddToCartManager;
import com.meembusoft.addtocart.model.CartItem;
import com.meembusoft.iot.R;
import com.meembusoft.iot.adapter.ColorAdapter;
import com.meembusoft.iot.adapter.PackageAdapter;
import com.meembusoft.iot.adapter.ImageSliderAdapter;
import com.meembusoft.iot.adapter.SizeAdapter;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.model.Image;
import com.meembusoft.iot.model.Product;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.BulletManager;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.view.viewfactory.TextViewFactory;
import com.smarteist.autoimageslider.CircularSliderHandle;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;
import io.realm.RealmObject;
import me.wangyuwei.shoppoing.ShoppingView;

import static com.meembusoft.iot.util.AllConstants.INTENT_KEY_EXTRA_PRODUCT;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ProductDetailActivity extends BaseActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;
    private TextView tvDiscount;
    private TextSwitcher tsDescriptionTitle, tsDescriptionDetail;
    private RelativeLayout rlCart;
    private ImageView ivCart;
    private TextView tvCart;

    // Image slider
    private SliderView sliderViewProduct;
    private ImageSliderAdapter imageSliderAdapterProduct;
    public Product mProduct;
    CartItem cartItem;
    private int lastPagePosition = 0;
    private ShoppingView svAddToCart;

    //
    private RecyclerView rvPackage, rvColor, rvSize;
    private SizeAdapter sizeAdapter;
    private ColorAdapter colorAdapter;
    private PackageAdapter packageAdapter;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_product_detail_new;
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
        Parcelable parcelable = intent.getParcelableExtra(INTENT_KEY_EXTRA_PRODUCT);
        if (parcelable != null) {
            mProduct = Parcels.unwrap(parcelable);
            Logger.d(TAG, "mProduct: " + mProduct.toString());
        }
    }

    @Override
    public void initActivityViews() {
        //toolbar view
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbar_title);
        leftMenu = (AnimatedImageView) findViewById(R.id.left_menu);
        rightMenu = (ImageView) findViewById(R.id.right_menu);
        rightMenu.setVisibility(View.GONE);
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlCart.setVisibility(View.VISIBLE);
        ivCart = (ImageView) findViewById(R.id.iv_cart);
        tvCart = (TextView) findViewById(R.id.tv_cart);
        svAddToCart = (ShoppingView) findViewById(R.id.sv_add_to_cart);

        tsDescriptionTitle = (TextSwitcher) findViewById(R.id.ts_description_title);
        tsDescriptionDetail = (TextSwitcher) findViewById(R.id.ts_description_detail);
        tsDescriptionTitle.setFactory(new TextViewFactory(getActivity(), R.style.TextSwitcherTitle, false));
        tsDescriptionDetail.setFactory(new TextViewFactory(getActivity(), R.style.TextSwitcherDescription, false));
        tvDiscount = (TextView) findViewById(R.id.tv_discount);
        sliderViewProduct = findViewById(R.id.sliderview_product);
        //
        rvSize = (RecyclerView) findViewById(R.id.rv_size);
        rvColor = (RecyclerView) findViewById(R.id.rv_color);
        rvPackage = (RecyclerView) findViewById(R.id.rv_package);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.title_activity_product_detail));

        // Reset Counter View
        //resetCounterView();

        // Initialize slider
        initImageSlider((mProduct != null) ? mProduct.getProduct_images() : new ArrayList<>());

        // Set discount value
        if (mProduct.getProduct_discount_percentage() > 0) {
            tvDiscount.setText(mProduct.getProduct_discount_percentage() + getString(R.string.txt_percentage_discount_is_ongoing));
            AppUtil.applyMarqueeOnTextView(tvDiscount);
            tvDiscount.setVisibility(View.VISIBLE);
        } else {
            tvDiscount.setVisibility(View.GONE);
        }

        // Load all sizes data
        rvSize.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        sizeAdapter = new SizeAdapter(getActivity());
        rvSize.setAdapter(sizeAdapter);
        sizeAdapter.addAll(mProduct.getProduct_sizes());

        // Load all colors data
        rvColor.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        colorAdapter = new ColorAdapter(getActivity());
        rvColor.setAdapter(colorAdapter);
        colorAdapter.addAll(mProduct.getProduct_colors());

        // Load all package data
        rvPackage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        packageAdapter = new PackageAdapter(getActivity());
        rvPackage.setAdapter(packageAdapter);
        packageAdapter.addAll(mProduct.getProduct_subscriptions());
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });
        if (mProduct != null) {
            cartItem = new CartItem(mProduct.getProduct_id(), mProduct.getProduct_description(), 50, mProduct.getProduct_name(), "smart home");
        }
        svAddToCart.setOnShoppingClickListener(new ShoppingView.ShoppingClickListener() {
            @Override
            public void onAddClick(int num) {
                Logger.d(TAG, "@=> " + "add.......num=> " + num);
                if (cartItem != null ) {
                    cartItem.setDiscountPercentage(5);
                    cartItem.setQuantity(num);
                    onOrderNowClick(cartItem, svAddToCart);
                }

            }

            @Override
            public void onMinusClick(int num) {
                Logger.d(TAG, "@=> " + "minus.......num=> " + num);
                if (cartItem != null ) {
                    cartItem.setDiscountPercentage(5);
                    cartItem.setQuantity(num);
                    onOrderNowClick(cartItem, svAddToCart);
                }
            }
        });

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCarts = new Intent(getActivity(), CartsActivity.class);
                 startActivity(intentCarts);
                // Delete items
//                AddToCartManager.getInstance().deleteAllCartItems(CartItem.class);
//
//                // Check all items
//                List<CartItem> cartItemList = AddToCartManager.getInstance().getAllCartItems(CartItem.class);
//                Logger.d(TAG, "AddToCart>>btnUpdate>>: all added items count is: " + cartItemList.size());
//                resetCounterView();
//                // Delete item
//                AddToCartManager.getInstance().deleteItem(CartItem.class, cartItem.getName(), "smart home");
//
//                // Find the same item, but it is not found
//                RealmObject mCartItem = AddToCartManager.getInstance().getCartItem(CartItem.class, cartItem.getName(), "smart home");
//                if (mCartItem == null) {
//                    Logger.d(TAG, "AddToCart>>btnRemove>>: item is deleted successfully");
//                }
            }
        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        finish();
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

    public void onOrderNowClick(final CartItem cartItem, ShoppingView shoppingView) {
        Logger.d(TAG, "onOrderNowClick: " + "count: " + cartItem.getQuantity());

        if (cartItem.getQuantity() == 0) {
            if (AddToCartManager.getInstance().hasCartItem(CartItem.class)) {
                //Delete the CartItem from database
                AddToCartManager.getInstance().deleteItem(CartItem.class, cartItem.getName(), "smart home");
            }
        } else if (cartItem.getQuantity() == 1) {
            if (AddToCartManager.getInstance().hasCartItem(CartItem.class)) {
                //Update data into database
                AddToCartManager.getInstance().addOrUpdateCart(cartItem);
                Logger.d(TAG, "<<<onOrderNowClick>>>: " + "Update>>>: " + cartItem.toString());
            } else {

                //Add item into database
                Logger.d(TAG, "onOrderNowClick: " + "<<Update>>>: " + cartItem.getQuantity());

                AddToCartManager.getInstance().addOrUpdateCart(cartItem);

                //make fly animation for adding item
                AppUtil.makeFlyAnimation(getActivity(), shoppingView, shoppingView.getAddIcon(), ivCart, 1000, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resetCounterView();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        } else {
            //Update data into database
            Logger.d(TAG, "onOrderNowClick: " + "Update: " + cartItem.getQuantity());
            Logger.d(TAG, "onOrderNowClick: " + "Update: " + cartItem.toString());

            AddToCartManager.getInstance().addOrUpdateCart(cartItem);

        }
//
//        //Reset counter view into toolbar
        resetCounterView();
    }


    public void resetCounterView() {
        List<CartItem> data = AddToCartManager.getInstance().getAllCartItems(CartItem.class);

        if (data.size() > 0 && data != null ) {
            Logger.d(TAG, "data: " + "count: " + data.toString());
            tvCart.setText(data.size() + "");
            tvCart.setVisibility(View.VISIBLE);
        } else {
            tvCart.setVisibility(View.GONE);
        }
    }

    public void setToolBarTitle(String title) {
        toolbarTitle.setAnimatedText(title, 0L);

        //For marquee address
        AppUtil.applyMarqueeOnTextView(toolbarTitle);
    }

    private void switchDetail(String title, String description, int currentPosition) {
        int animH[] = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = currentPosition < lastPagePosition;

        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }
        tsDescriptionTitle.setInAnimation(getActivity(), animH[0]);
        tsDescriptionTitle.setOutAnimation(getActivity(), animH[1]);
        tsDescriptionTitle.setText(title);

        tsDescriptionDetail.setInAnimation(getActivity(), animV[0]);
        tsDescriptionDetail.setOutAnimation(getActivity(), animV[1]);
        tsDescriptionDetail.setText(BulletManager.buildBulletText(getActivity(), "<bullet>", description));

        lastPagePosition = currentPosition;
    }

    private void initImageSlider(List<Image> productImages) {
        if (productImages != null && productImages.size() > 0) {
            imageSliderAdapterProduct = new ImageSliderAdapter(getActivity());
            imageSliderAdapterProduct.setData(productImages);

            sliderViewProduct.setSliderAdapter(imageSliderAdapterProduct);
            sliderViewProduct.setIndicatorAnimation(IndicatorAnimations.getRandomIndicatorAnimation()); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderViewProduct.setSliderTransformAnimation(SliderAnimations.getRandomSliderTransformAnimation());
            sliderViewProduct.setIndicatorGravity(Gravity.BOTTOM | Gravity.RIGHT);
            sliderViewProduct.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderViewProduct.setIndicatorSelectedColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            sliderViewProduct.setIndicatorUnselectedColor(Color.GRAY);
            sliderViewProduct.setCurrentPageListener(new CircularSliderHandle.CurrentPageListener() {
                @Override
                public void onCurrentPageChanged(int currentPosition) {
                    Image mImage = productImages.get(currentPosition);
                    switchDetail(mImage.getTitle(), mImage.getDescription(), currentPosition);
                }
            });
            sliderViewProduct.setOnIndicatorClickListener(new DrawController.ClickListener() {
                @Override
                public void onIndicatorClicked(int position) {
                    sliderViewProduct.setCurrentPagePosition(position);
                    Image mImage = productImages.get(position);
                    switchDetail(mImage.getTitle(), mImage.getDescription(), position);
                }
            });
            // For the very first time show first item as selected
            sliderViewProduct.setCurrentPagePosition(0);
            Image mImage = productImages.get(0);
            switchDetail(mImage.getTitle(), mImage.getDescription(), 0);
        }
    }
}