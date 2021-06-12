package com.meembusoft.iot.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.meembusoft.iot.enumeration.AddDeviceScreenType;
import com.meembusoft.iot.fragment.SelectRoomFragment;
import com.meembusoft.iot.fragment.AttachApplianceFragment;
import com.meembusoft.iot.fragment.ChooseDeviceFragment;
import com.meembusoft.iot.fragment.ConnectDeviceFragment;
import com.meembusoft.iot.fragment.EnsureConnectivityFragment;
import com.meembusoft.iot.fragment.PreviewScreenFragment;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AddDeviceViewPagerAdapter extends SmartFragmentStatePagerAdapter {

    private List<AddDeviceScreenType> mFragmentsPages = new ArrayList<AddDeviceScreenType>();

    public AddDeviceViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragmentsPages.clear();
        mFragmentsPages.addAll(Arrays.asList(AddDeviceScreenType.values()));
    }

    @Override
    public Fragment getItem(int position) {
        switch (mFragmentsPages.get(position)) {
            case SELECT_ROOM:
                return SelectRoomFragment.newInstance();
            case CHOOSE_DEVICE:
                return ChooseDeviceFragment.newInstance();
            case ATTACH_APPLIANCE:
                return AttachApplianceFragment.newInstance();
            case ENSURE_CONNECTIVITY:
                return EnsureConnectivityFragment.newInstance();
            case CONNECT_DEVICE:
                return ConnectDeviceFragment.newInstance();
            case PREVIEW_SCREEN:
                return PreviewScreenFragment.newInstance();
            default:
                return SelectRoomFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mFragmentsPages.size();
    }

    public AddDeviceScreenType getScreenType(int position) {
        return mFragmentsPages.get(position);
    }
}