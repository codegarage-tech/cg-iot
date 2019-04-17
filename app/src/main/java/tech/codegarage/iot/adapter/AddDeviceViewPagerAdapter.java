package tech.codegarage.iot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tech.codegarage.iot.enumeration.AddDeviceScreenType;
import tech.codegarage.iot.fragment.SelectRoomFragment;
import tech.codegarage.iot.fragment.AttachApplianceFragment;
import tech.codegarage.iot.fragment.ChooseDeviceFragment;
import tech.codegarage.iot.fragment.ConnectDeviceFragment;
import tech.codegarage.iot.fragment.EnsureConnectivityFragment;
import tech.codegarage.iot.fragment.PreviewScreenFragment;

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