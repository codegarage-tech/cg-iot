package com.meembusoft.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.reversecoder.library.event.OnSingleClickListener;

import com.meembusoft.iot.R;
import com.meembusoft.iot.activity.HomeActivity;
import com.meembusoft.iot.base.BaseFragment;
import com.meembusoft.iot.model.User;
import com.meembusoft.retrofitmanager.APIResponse;
import com.meembusoft.iot.util.SessionUtil;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RegistrationFragment extends BaseFragment {

    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
//        parentView.findViewById(R.id.lin_login_next).setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                //Set dummy json user
//                User user = new User("1", "Md. Rashadul Alam", "01794620787", "rashed.droid@gmail.com", "Ka-13, South Kuril", "");
//                SessionUtil.setUser(getActivity(), APIResponse.getJSONStringFromObject(user));
//                ((HomeActivity) getActivity()).initNavigationDrawer();
//            }
//        });
    }

    @Override
    public void initFragmentViewsData() {

    }

    @Override
    public void initFragmentActions() {

    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initFragmentUpdate(Object object) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setRightMenu(false,-1, null);
        ((HomeActivity) getActivity()).setLockMode(false);
    }
}