package com.meembusoft.iot.dialog;

import android.app.Activity;

import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseAlertDialog;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CloseAppDialog extends BaseAlertDialog {

    private OnClickListener mOnClickListener;

    public CloseAppDialog(Activity activity, OnClickListener onClickListener) {
        super(activity);
        mOnClickListener = onClickListener;
    }

    @Override
    public Builder initView() {
        Builder builder = prepareView("", getActivity().getString(R.string.dialog_do_you_want_to_close_the_app), getActivity().getString(R.string.dialog_cancel), getActivity().getString(R.string.dialog_ok), "", mOnClickListener);

        return builder;
    }
}