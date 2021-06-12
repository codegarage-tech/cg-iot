package com.meembusoft.iot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.addtocart.AddToCartManager;
import com.meembusoft.addtocart.model.CartItem;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.util.AppUtil;
import com.meembusoft.iot.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;

import java.util.List;

import io.armcha.ribble.presentation.widget.AnimatedImageView;
import io.armcha.ribble.presentation.widget.AnimatedTextView;
import io.realm.RealmObject;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CartActivity extends BaseActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;

    private Button btnAdd, btnUpdate, btnRemove, btnRemoveAll, btnSelected, btnAll;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_cart;
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

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnRemove = (Button) findViewById(R.id.btn_remove);
        btnRemoveAll = (Button) findViewById(R.id.btn_remove_all);
        btnSelected = (Button) findViewById(R.id.btn_selected);
        btnAll = (Button) findViewById(R.id.btn_all);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.title_activity_cart));
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });

        btnAdd.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                CartItem cartItem = new CartItem("1", 50, "smart home");
                cartItem.setDiscountPercentage(5);
                AddToCartManager.getInstance().addOrUpdateCart(cartItem);
                Logger.d(TAG, "AddToCart>>btnAdd>>: added items: " + AddToCartManager.getInstance().getAllCartItems(CartItem.class));
            }
        });

        btnUpdate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // Find specific item
                CartItem cartItem = (CartItem) AddToCartManager.getInstance().getCartItem(CartItem.class, "name", "smart home");
                Logger.d(TAG, "AddToCart>>btnUpdate>>: target item: " + cartItem.toString());

                // Prepare update item
                CartItem updatedCartItem = new CartItem(cartItem.getId(), cartItem.getPrice(), cartItem.getName());
                updatedCartItem.setDiscountPercentage(cartItem.getDiscountPercentage());
                updatedCartItem.setQuantity(cartItem.getQuantity() + 1);
                Logger.d(TAG, "AddToCart>>btnUpdate>>: set update:" + updatedCartItem.toString());

                // Call update
                AddToCartManager.getInstance().addOrUpdateCart(updatedCartItem);
                Logger.d(TAG, "AddToCart>>btnUpdate>>: added items: " + AddToCartManager.getInstance().getAllCartItems(CartItem.class));
            }
        });

        btnRemove.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // Delete item
                AddToCartManager.getInstance().deleteItem(CartItem.class, "name", "smart home");

                // Find the same item, but it is not found
                RealmObject cartItem = AddToCartManager.getInstance().getCartItem(CartItem.class, "name", "smart home");
                if (cartItem == null) {
                    Logger.d(TAG, "AddToCart>>btnRemove>>: item is deleted successfully");
                }
            }
        });

        btnRemoveAll.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // Delete items
                AddToCartManager.getInstance().deleteAllCartItems(CartItem.class);

                // Check all items
                List<CartItem> cartItemList = AddToCartManager.getInstance().getAllCartItems(CartItem.class);
                Logger.d(TAG, "AddToCart>>btnUpdate>>: all added items count is: " + cartItemList.size());
            }
        });

        btnSelected.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // Find specific item
                CartItem cartItem = (CartItem) AddToCartManager.getInstance().getCartItem(CartItem.class, "name", "smart home");
                Logger.d(TAG, "AddToCart>>btnSelected>>: target item: " + cartItem.toString());

                // Prepare update item
                CartItem updatedCartItem = new CartItem(cartItem.getId(), cartItem.getPrice(), cartItem.getName());
                updatedCartItem.setSelected(true);
                Logger.d(TAG, "AddToCart>>btnSelected>>: set update:" + updatedCartItem.toString());

                // Call update
                AddToCartManager.getInstance().addOrUpdateCart(updatedCartItem);
                Logger.d(TAG, "AddToCart>>btnSelected>>: added items: " + AddToCartManager.getInstance().getAllCartItems(CartItem.class));

                // Find selected
                List<CartItem> cartItemList = AddToCartManager.getInstance().getAllSelectedCartItems(CartItem.class, "isSelected", true);
                Logger.d(TAG, "AddToCart>>btnSelected>>: selected items(" + cartItemList.size() + "): " + cartItemList.toString());
            }
        });

        btnAll.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Logger.d(TAG, "AddToCart>>btnUpdate>>: all added items: " + AddToCartManager.getInstance().getAllCartItems(CartItem.class));
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