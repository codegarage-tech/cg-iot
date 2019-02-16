package tech.codegarage.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.reversecoder.library.storage.SessionManager;

import tech.codegarage.iot.R;
import tech.codegarage.iot.activity.HomeActivity;
import tech.codegarage.iot.base.BaseFragment;
import tech.codegarage.iot.model.User;
import tech.codegarage.iot.retrofit.APIResponse;
import tech.codegarage.iot.util.AllConstants;
import tech.codegarage.iot.util.SessionUtil;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class LoginFragment extends BaseFragment {

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {

    }

    @Override
    public void initFragmentViewsData() {
        //Set dummy json user
        User user = new User("1", "Md. Rashadul Alam", "01794620787", "rashed.droid@gmail.com", "Ka-13, South Kuril", "");
        SessionUtil.setUser(getActivity(),APIResponse.getResponseString(user));
        ((HomeActivity)getActivity()).initNavigationDrawer();
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
        ((HomeActivity)getActivity()).setRightMenu(false, null);
        ((HomeActivity)getActivity()).setLockMode(false);
    }
}