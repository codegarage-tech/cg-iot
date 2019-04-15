package tech.codegarage.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tech.codegarage.iot.R;
import tech.codegarage.iot.activity.AddDeviceActivity;
import tech.codegarage.iot.base.BaseFragment;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AttachApplianceFragment extends BaseFragment {

    public static AttachApplianceFragment newInstance() {
        AttachApplianceFragment fragment = new AttachApplianceFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_attach_appliance;
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

//    public boolean isAllFieldsVerified() {
//        if (checkoutFoodAdapter.getCount() > 0) {
//            return true;
//        } else {
//            Toast.makeText(getActivity(), getString(R.string.toast_please_add_to_cart_first), Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    }
}