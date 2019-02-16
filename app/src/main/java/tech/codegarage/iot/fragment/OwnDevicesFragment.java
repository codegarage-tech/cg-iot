package tech.codegarage.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import com.labo.kaji.fragmentanimations.PushPullAnimation;

import org.parceler.Parcels;

import io.armcha.ribble.presentation.navigationview.NavigationItem;
import tech.codegarage.iot.R;
import tech.codegarage.iot.activity.HomeActivity;
import tech.codegarage.iot.base.BaseFragment;

import static tech.codegarage.iot.util.AllConstants.BUNDLE_KEY_MESSAGE;
import static tech.codegarage.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class OwnDevicesFragment extends BaseFragment {

    private String mNavigationItem;

    public static OwnDevicesFragment newInstance() {
        OwnDevicesFragment fragment = new OwnDevicesFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_own_devices;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {
        String navigationItem = bundle.getString(BUNDLE_KEY_MESSAGE);
        if(navigationItem !=null){
            mNavigationItem = navigationItem;
        }
    }

    @Override
    public void initFragmentViews(View parentView) {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setRightMenu(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).changeFragment(new AddDeviceFragment(), getString(R.string.title_fragment_add_device));
            }
        });
        ((HomeActivity)getActivity()).setToolBarTitle(mNavigationItem);
        ((HomeActivity)getActivity()).setLeftMenu(true);
        ((HomeActivity)getActivity()).setLockMode(false);
    }
}