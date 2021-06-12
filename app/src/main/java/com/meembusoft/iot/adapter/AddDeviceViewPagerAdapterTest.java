package com.meembusoft.iot.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.meembusoft.iot.enumeration.AddDeviceScreenType;
import com.meembusoft.iot.enumeration.AddDeviceScreenTypeTest;
import com.meembusoft.iot.fragment.AttachApplianceFragment;
import com.meembusoft.iot.fragment.ChooseDeviceFragment;
import com.meembusoft.iot.fragment.ConnectDeviceFragment;
import com.meembusoft.iot.fragment.EnsureConnectivityFragment;
import com.meembusoft.iot.fragment.PreviewScreenFragment;
import com.meembusoft.iot.fragment.SelectProductFragmentTest;
import com.meembusoft.iot.fragment.SelectRoomFragment;
import com.meembusoft.iot.fragment.SelectRoomFragmentTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AddDeviceViewPagerAdapterTest extends SmartFragmentStatePagerAdapter {

    private List<AddDeviceScreenTypeTest> mFragmentsPages = new ArrayList<AddDeviceScreenTypeTest>();

    public AddDeviceViewPagerAdapterTest(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragmentsPages.clear();
        mFragmentsPages.addAll(Arrays.asList(AddDeviceScreenTypeTest.values()));
    }

    @Override
    public Fragment getItem(int position) {
        switch (mFragmentsPages.get(position)) {
            case SELECT_ROOM:
                return SelectRoomFragmentTest.newInstance();
            case CONNECT_DEVICE:
                return SelectProductFragmentTest.newInstance();
            case ENSURE_CONNECTIVITY:
                return EnsureConnectivityFragment.newInstance();
            default:
                return SelectRoomFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mFragmentsPages.size();
    }

    public AddDeviceScreenTypeTest getScreenType(int position) {
        return mFragmentsPages.get(position);
    }
}