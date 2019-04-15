package tech.codegarage.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import com.labo.kaji.fragmentanimations.PushPullAnimation;

import tech.codegarage.iot.R;
import tech.codegarage.iot.activity.AddDeviceActivity;
import tech.codegarage.iot.base.BaseFragment;

import static tech.codegarage.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ChooseDeviceFragment extends BaseFragment {

    public static ChooseDeviceFragment newInstance() {
        ChooseDeviceFragment fragment = new ChooseDeviceFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_choose_device;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
//        rvCheckoutFood = (RecyclerView) parentView.findViewById(R.id.rv_checkout_food);
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
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return PushPullAnimation.create(PushPullAnimation.RIGHT, enter, FRAGMENT_TRANSITION_DURATION);
        } else {
            return PushPullAnimation.create(PushPullAnimation.LEFT, enter, FRAGMENT_TRANSITION_DURATION);
        }
    }

//    public boolean isAllFieldsVerified() {
//        if (checkoutFoodAdapter.getCount() > 0) {
//            return true;
//        } else {
//            Toast.makeText(getActivity(), getString(R.string.toast_please_add_to_cart_first), Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    }
}