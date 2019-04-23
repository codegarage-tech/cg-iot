package tech.codegarage.iot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.labo.kaji.fragmentanimations.PushPullAnimation;
import com.nex3z.flowlayout.FlowLayout;
import com.nex3z.flowlayout.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

import tech.codegarage.iot.R;
import tech.codegarage.iot.base.BaseFragment;
import tech.codegarage.iot.model.Room;

import static tech.codegarage.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SelectRoomFragment extends BaseFragment {

//    private RecyclerView rvRoom;
//    private RoomAdapter roomAdapter;

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
//        rvRoom = (RecyclerView) parentView.findViewById(R.id.rv_room);
        flowLayoutRoomName = (FlowLayout) parentView.findViewById(R.id.fl_room_name);
    }

    @Override
    public void initFragmentViewsData() {
//        initRoomRecyclerView();

        List<Room> mSuggestedRoom = new ArrayList<Room>() {{
            add(new Room(1, "Bed Room", ""));
            add(new Room(2, "Leaving Room", ""));
            add(new Room(3, "Drawing Room", ""));
            add(new Room(4, "Store Room", ""));
            add(new Room(5, "Wash Room", ""));
            add(new Room(1, "Bed Room", ""));
            add(new Room(2, "Leaving Room", ""));
            add(new Room(3, "Drawing Room", ""));
            add(new Room(4, "Store Room", ""));
            add(new Room(5, "Wash Room", ""));
        }};
        initSuggestedRoom(mSuggestedRoom);
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
//        roomAdapter = new RoomAdapter(getActivity());
//        rvRoom.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvRoom.setAdapter(roomAdapter);
//        roomAdapter.addAll(new ArrayList<Room>() {{
//            add(new Room(1, "Bed Room", ""));
//            add(new Room(2, "Leaving Room", ""));
//            add(new Room(3, "Drawing Room", ""));
//            add(new Room(4, "Store Room", ""));
//            add(new Room(5, "Wash Room", ""));
//        }});
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
    private void initSuggestedRoom(List<Room> roomList) {
        if (roomList != null && roomList.size() > 0) {
            //Arrange suggested room's key
            List<String> mSuggestedRoomKeys = new ArrayList<>();
            for (Room room : roomList) {
                mSuggestedRoomKeys.add(room.getName());
            }

            //Set flow layout with suggested room key
            flowLayoutManagerRoomName = new FlowLayoutManager.FlowViewBuilder(getActivity(), flowLayoutRoomName, mSuggestedRoomKeys, new FlowLayoutManager.onFlowViewClick() {
                @Override
                public void flowViewClick(TextView updatedTextView) {
                    List<TextView> selectedVoiceSearchKeys = flowLayoutManagerRoomName.getSelectedFlowViews();
                    String mSelectedVoiceSearchKey = (selectedVoiceSearchKeys.size() > 0) ? selectedVoiceSearchKeys.get(0).getText().toString() : "";

//                    //Select selected voice search key
//                    if (AllSettingsManager.isNullOrEmpty(mSelectedVoiceSearchKey)) {
//                        tvVoiceSearchCategory.setText(getString(R.string.view_voice_search_category));
//                        mVoiceSearchCategory = KitchenType.NONE;
//                    } else {
//                        tvVoiceSearchCategory.setText(mSelectedVoiceSearchKey);
//                        mVoiceSearchCategory = KitchenType.getKitchenType(mSelectedVoiceSearchKey);
//
//                        //Close the flow layout
//                        expansionLayout.toggle(true);
//                    }
//                    //Save selected voice search key
//                    SessionManager.setStringSetting(getActivity(), SESSION_KEY_SELECTED_VOICE_SEARCH, mVoiceSearchCategory.toString());
//
//                    //Set translated default text
//                    tvTranslatedText.setText(getString(R.string.view_tap_mic_icon_from_below_and_say_whatever_you_want_to_search));
                }
            })
                    .setSingleChoice(true)
                    .build();

            //Set last selected voice search key
//            String mLastSelectedVoiceSearchKey = SessionManager.getStringSetting(getActivity(), SESSION_KEY_SELECTED_VOICE_SEARCH);
//            if (!AllSettingsManager.isNullOrEmpty(mLastSelectedVoiceSearchKey)) {
//                flowLayoutManagerVoiceSearchCategory.clickFlowView(mLastSelectedVoiceSearchKey);
//            } else {
//                mVoiceSearchCategory = KitchenType.NONE;
//                tvVoiceSearchCategory.setText(getString(R.string.view_voice_search_category));
//            }
        }
    }
}