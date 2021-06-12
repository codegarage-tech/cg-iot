package com.meembusoft.iot.enumeration;

import android.content.Context;

import androidx.annotation.StringRes;

import com.meembusoft.iot.R;
import com.meembusoft.iot.application.IotApp;
import com.meembusoft.iot.base.BaseEnum;

public enum Language implements BaseEnum {

    SELECT_LANGUAGE(R.string.txt_app_language),
    BENGALI(R.string.txt_language_english),
    ENGLISH(R.string.txt_language_bengali);

    private @StringRes
    int key = -1;

    Language(@StringRes int name) {
        key = name;
    }

    @Override
    public String toString() {
        return IotApp.getGlobalContext().getString(key);
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