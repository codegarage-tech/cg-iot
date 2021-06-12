package io.armcha.ribble.presentation.navigationview;

import android.content.Context;

import androidx.annotation.StringRes;

import io.armcha.ribble.R;
import io.armcha.ribble.RibbleMenu;

public enum NavigationId implements BaseNavigationId {

    DASHBOARD(R.string.menu_dashboard),
    PRODUCTS(R.string.menu_products),
    ADD_DEVICE(R.string.menu_add_device),
    SETTINGS(R.string.menu_settings),
    LOGOUT(R.string.menu_logout);

    private @StringRes
    int key = -1;

    NavigationId(@StringRes int name) {
        key = name;
    }

    @Override
    public String toString() {
        return RibbleMenu.getRibbleContext().getString(key);
    }

    @Override
    public String getLabel(Context context) {
        return context.getString(key);
    }

    @Override
    public int getKey() {
        return key;
    }
}