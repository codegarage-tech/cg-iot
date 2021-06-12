package com.meembusoft.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kannan.glazy.GlazyCard;
import com.kannan.glazy.Utils;
import com.kannan.glazy.views.GlazyImageView;
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
public class LoginFragment extends BaseFragment {

    private GlazyImageView imgView;
    private GlazyCard card;

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

        card = new GlazyCard()
                .withTitle("Md. Rashadul Alam")
                .withSubTitle("Senior Software Engineer")
                .withImageRes(R.drawable.ic_logo)
                .withImageCutType(GlazyImageView.ImageCutType.WAVE)
                .withImageCutHeightDP(40);

        imgView = (GlazyImageView) parentView.findViewById(com.kannan.glazy.R.id.glazy_image_view);
        imgView.setImageRes(card.getImageRes());
        imgView.setTitleText(card.getTitle());
        imgView.setTitleTextColor(card.getTitleColor());
        imgView.setTitleTextSize(Utils.dpToPx(getActivity(), card.getTitleSizeDP()));
        imgView.setSubTitleText(card.getSubTitle());
        imgView.setSubTitleTextColor(card.getSubTitleColor());
        imgView.setSubTitleTextSize(Utils.dpToPx(getActivity(), card.getSubTitleSizeDP()));
        imgView.setTextMargin(Utils.dpToPx(getActivity(), card.getTextmatginDP()));
        imgView.setLineSpacing(Utils.dpToPx(getActivity(), card.getLineSpacingDP()));
        imgView.setAutoTint(card.isAutoTint());
        imgView.setTintColor(card.getTintColor());
        imgView.setTintAlpha(card.getTintAlpha());
        imgView.setCutType(card.getImageCutType());
        imgView.setCutCount(card.getImageCutCount());
        imgView.setCutHeight(Utils.dpToPx(getActivity(),card.getImageCutHeightDP()));
        imgView.post(new Runnable() {
            @Override
            public void run() {
                imgView.update(.99f);
            }
        });

//        parentView.findViewById(R.id.lin_login_next).setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                FragmentUtilsManager.changeSupportFragment(((HomeActivity)getActivity()), new RegistrationFragment());
//            }
//        });
    }

    @Override
    public void initFragmentViewsData() {
        //Set dummy json user
//        User user = new User("1", "Md. Rashadul Alam", "01794620787", "rashed.droid@gmail.com", "Ka-13, South Kuril", "");
//        SessionUtil.setUser(getActivity(),APIResponse.getJSONStringFromObject(user));
//        ((HomeActivity)getActivity()).initNavigationDrawer();
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
        ((HomeActivity)getActivity()).setRightMenu(false, -1, null);
        ((HomeActivity)getActivity()).setLockMode(false);
    }
}