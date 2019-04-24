package tech.codegarage.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.labo.kaji.fragmentanimations.PushPullAnimation;
import com.nex3z.flowlayout.FlowLayout;
import com.nex3z.flowlayout.FlowLayoutManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;
import java.util.List;

import tech.codegarage.iot.R;
import tech.codegarage.iot.base.BaseFragment;
import tech.codegarage.iot.model.Room;
import tech.codegarage.iot.realm.DataBaseManager;
import tech.codegarage.iot.util.Logger;
import tech.codegarage.iot.util.SessionUtil;

import static tech.codegarage.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SelectRoomFragment extends BaseFragment {

    private String TAG = "SelectRoomFragment";
    private FlowLayout flowLayoutRoomName;
    private FlowLayoutManager flowLayoutManagerRoomName;

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
        flowLayoutRoomName = (FlowLayout) parentView.findViewById(R.id.fl_room_name);
    }

    @Override
    public void initFragmentViewsData() {
        // Initialize suggested room data
        if (!DataBaseManager.hasRoomInitialized(getActivity())) {
            Logger.d(TAG, TAG + ">> Room data are not initialized");
            DataBaseManager.initSuggestedRooms(getActivity());
        } else {
            Logger.d(TAG, TAG + ">> Room data has already initialized");
        }
        initSuggestedRoom(DataBaseManager.getAllRooms(getActivity()));
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

    public boolean isAllFieldsVerified() {
//        if (checkoutFoodAdapter.getCount() > 0) {
//            return true;
//        } else {
//            Toast.makeText(getActivity(), getString(R.string.toast_please_add_to_cart_first), Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    /***************************
     * Methods for flow layout *
     ***************************/
    public void addFlowItem(String flowItem) {
        if (DataBaseManager.addOrUpdateRoom(getActivity(), new Room(flowItem, "")) != null) {
            flowLayoutManagerRoomName.addFlowItem(flowItem);
            flowLayoutManagerRoomName.clickFlowView(flowItem);
        }
    }

    public void initSuggestedRoom(List<Room> suggestedRoom) {
        if (suggestedRoom != null && suggestedRoom.size() > 0) {
            //Arrange suggested room's key
            List<String> mSuggestedRoomKeys = new ArrayList<>();
            for (Room mRoom : suggestedRoom) {
                mSuggestedRoomKeys.add(mRoom.getName());
            }

            //Set flow layout with suggested room key
            flowLayoutManagerRoomName = new FlowLayoutManager.FlowViewBuilder(getActivity(), flowLayoutRoomName, mSuggestedRoomKeys, new FlowLayoutManager.onFlowViewClick() {
                @Override
                public void flowViewClick(TextView updatedTextView) {
                    List<TextView> selectedVoiceSearchKeys = flowLayoutManagerRoomName.getSelectedFlowViews();
                    String tempSelectedRoom = (selectedVoiceSearchKeys.size() > 0) ? selectedVoiceSearchKeys.get(0).getText().toString() : "";
                    Logger.d(TAG, "tempSelectedRoom: " + tempSelectedRoom);

                    //Save temp selected room
                    SessionUtil.setTempSelectedRoom(getActivity(), tempSelectedRoom);
                }
            })
                    .setSingleChoice(true)
                    .build();

            //Set last temp selected room key
            String lastTempSelectedRoom = SessionUtil.getTempSelectedRoom(getActivity());
            if (!AllSettingsManager.isNullOrEmpty(lastTempSelectedRoom)) {
                flowLayoutManagerRoomName.clickFlowView(lastTempSelectedRoom);
            }
        }
    }
}