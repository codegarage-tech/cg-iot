package com.meembusoft.iot.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.labo.kaji.fragmentanimations.PushPullAnimation;
import com.meembusoft.iot.R;
import com.meembusoft.iot.activity.HomeActivity;
import com.meembusoft.iot.adapter.FavoriteDeviceAdapter;
import com.meembusoft.iot.adapter.RoomAdapter;
import com.meembusoft.iot.adapter.RoomWiseDeviceAdapter;
import com.meembusoft.iot.base.BaseFragment;
import com.meembusoft.iot.model.Device;
import com.meembusoft.iot.model.Room;
import com.meembusoft.iot.util.AllConstants;
import com.meembusoft.iot.util.DataUtil;
import com.meembusoft.iot.util.Logger;
import com.reversecoder.library.network.NetworkManager;

import org.parceler.Parcels;

import static com.meembusoft.iot.util.AllConstants.BUNDLE_KEY_MESSAGE;
import static com.meembusoft.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DashboardFragment extends BaseFragment {

    private String mNavigationItem;
    private RecyclerView rvFavouriteDevice, rvRoom, rvDevice;
    private FavoriteDeviceAdapter favoriteDeviceAdapter;
    private RoomAdapter roomAdapter;
    private RoomWiseDeviceAdapter roomWiseDeviceAdapter;

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {
        String navigationItem = bundle.getString(BUNDLE_KEY_MESSAGE);
        if (navigationItem != null) {
            mNavigationItem = navigationItem;
        }
    }

    @Override
    public void initFragmentViews(View parentView) {
        rvFavouriteDevice = (RecyclerView) parentView.findViewById(R.id.rc_favourite);
        rvRoom = (RecyclerView) parentView.findViewById(R.id.rc_select_room);
        rvDevice = (RecyclerView) parentView.findViewById(R.id.rc_device_room);

    }

    @Override
    public void initFragmentViewsData() {
        rvFavouriteDevice.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvFavouriteDevice.setHasFixedSize(true);
        rvRoom.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvRoom.setHasFixedSize(true);
        rvDevice.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvDevice.setHasFixedSize(true);
        if (NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), "Need to request from server\nNow uses offline data only", Toast.LENGTH_SHORT).show();
        }

        // Load all favorite data
        favoriteDeviceAdapter = new FavoriteDeviceAdapter(getActivity());
        rvFavouriteDevice.setAdapter(favoriteDeviceAdapter);
        favoriteDeviceAdapter.addAll(DataUtil.getAllFavoriteDevices(getActivity()));
        favoriteDeviceAdapter.notifyDataSetChanged();

        // Load room wise data
        roomAdapter = new RoomAdapter(getActivity());
        rvRoom.setAdapter(roomAdapter);
        roomAdapter.addAll(DataUtil.getAllPersonalRooms(getActivity()));
        roomAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Room room = roomAdapter.getItem(position);
                initRoomWiseDevice(room.getRoom_id(), position);
            }
        });

        // Select first one for the very first time
        initRoomWiseDevice("1", 0);
    }

    @Override
    public void initFragmentActions() {

    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, "requestCode: " + requestCode + " resultCode: " + resultCode);
        switch (requestCode) {
            case AllConstants.INTENT_KEY_REQUEST_CODE_FAVORITE_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    if (favoriteDeviceAdapter != null) {
                        Parcelable parcelable = data.getParcelableExtra(AllConstants.INTENT_KEY_EXTRA_DEVICE);
                        if (parcelable != null) {
                            Device device = Parcels.unwrap(parcelable);
                            Logger.d(TAG, "mDevice: " + device.toString());
                            // remove item from favorite if any device is unfavorited
                            if (device.getIs_favorite() == 1) {
                                favoriteDeviceAdapter.updateItem(device);
                            } else {
                                favoriteDeviceAdapter.removeItem(device);
                            }
                        }
                    }
                }
                break;
            case AllConstants.INTENT_KEY_REQUEST_CODE_ROOM_WISE_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    if (roomWiseDeviceAdapter != null) {
                        Parcelable parcelable = data.getParcelableExtra(AllConstants.INTENT_KEY_EXTRA_DEVICE);
                        if (parcelable != null) {
                            Device device = Parcels.unwrap(parcelable);
                            Logger.d(TAG, "mDevice: " + device.toString());
                            roomWiseDeviceAdapter.updateItem(device);
                        }
                    }
                }
                break;
        }
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
        ((HomeActivity) getActivity()).setRightMenu(false, -1, null);
        ((HomeActivity) getActivity()).setToolBarTitle(mNavigationItem);
        ((HomeActivity) getActivity()).setLeftMenu(true);
        ((HomeActivity) getActivity()).setLockMode(false);
    }

    private void initRoomWiseDevice(String roomId, int position) {
        if (roomWiseDeviceAdapter == null) {
            roomWiseDeviceAdapter = new RoomWiseDeviceAdapter(getActivity());
            rvDevice.setAdapter(roomWiseDeviceAdapter);
        }
        roomWiseDeviceAdapter.clear();
        Room room = DataUtil.getSpecificRoomById(getActivity(), roomId);
        if (room != null) {
            roomWiseDeviceAdapter.addAll(room.getDevices());
        }

        // Select room
        roomAdapter.setSelection(position);
    }
}