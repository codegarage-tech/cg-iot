package tech.codegarage.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;

import com.labo.kaji.fragmentanimations.PushPullAnimation;

import java.util.ArrayList;

import tech.codegarage.iot.R;
import tech.codegarage.iot.adapter.RoomAdapter;
import tech.codegarage.iot.base.BaseFragment;
import tech.codegarage.iot.model.Room;

import static tech.codegarage.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SelectRoomFragment extends BaseFragment {

    private RecyclerView rvRoom;
    private RoomAdapter roomAdapter;

    public static SelectRoomFragment newInstance() {
        SelectRoomFragment fragment = new SelectRoomFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_select_room;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        rvRoom = (RecyclerView) parentView.findViewById(R.id.rv_room);
    }

    @Override
    public void initFragmentViewsData() {
        initRoomRecyclerView();
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

    private void initRoomRecyclerView() {
        roomAdapter = new RoomAdapter(getActivity());
        rvRoom.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRoom.setAdapter(roomAdapter);
        roomAdapter.addAll(new ArrayList<Room>() {{
            add(new Room(1, "Bed Room", ""));
            add(new Room(2, "Leaving Room", ""));
            add(new Room(3, "Drawing Room", ""));
            add(new Room(4, "Store Room", ""));
            add(new Room(5, "Wash Room", ""));
        }});
    }

    public boolean isAllFieldsVerified() {
//        if (checkoutFoodAdapter.getCount() > 0) {
//            return true;
//        } else {
//            Toast.makeText(getActivity(), getString(R.string.toast_please_add_to_cart_first), Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }
}