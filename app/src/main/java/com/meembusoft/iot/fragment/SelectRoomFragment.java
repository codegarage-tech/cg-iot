package com.meembusoft.iot.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.labo.kaji.fragmentanimations.PushPullAnimation;
import com.meembusoft.iot.R;
import com.meembusoft.iot.base.BaseFragment;
import com.meembusoft.iot.model.Room;
import com.meembusoft.iot.util.DataUtil;
import com.meembusoft.iot.util.Logger;
import com.meembusoft.iot.util.SessionUtil;
import com.nex3z.flowlayout.FlowLayout;
import com.nex3z.flowlayout.FlowLayoutManager;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.random.RandomManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;
import java.util.List;

import static com.meembusoft.iot.util.AllConstants.FRAGMENT_TRANSITION_DURATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SelectRoomFragment extends BaseFragment {

    private String TAG = "SelectRoomFragment";
    private FlowLayout flowLayoutRoomName;
    private FlowLayoutManager flowLayoutManagerRoomName;
    private LinearLayout llAddRoom;
    private List<Room> mSuggestedRoom = new ArrayList<>();

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
        llAddRoom = (LinearLayout) parentView.findViewById(R.id.ll_add_room);
    }

    @Override
    public void initFragmentViewsData() {
        initSuggestedRoom(DataUtil.getAllRooms(getActivity()));
    }

    @Override
    public void initFragmentActions() {
        llAddRoom.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
//                showInputRoomDialog();
            }
        });
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
        String lastTempSelectedRoom = SessionUtil.getTempSelectedRoom(getActivity());
        if (!AllSettingsManager.isNullOrEmpty(lastTempSelectedRoom)) {
            return true;
        } else {
            Toast.makeText(getActivity(), getString(R.string.txt_please_select_one_room), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    /***************************
     * Methods for flow layout *
     ***************************/
    public void addFlowItem(String flowItem) {
        int id = RandomManager.getRandom(100);
        mSuggestedRoom.add(new Room(id + "", flowItem));
        flowLayoutManagerRoomName.addFlowItem(flowItem);
        flowLayoutManagerRoomName.clickFlowView(flowItem);
    }

    public void initSuggestedRoom(List<Room> suggestedRoom) {
        if (suggestedRoom != null && suggestedRoom.size() > 0) {
            mSuggestedRoom.clear();
            mSuggestedRoom.addAll(suggestedRoom);

            //Arrange suggested room's key
            List<String> mSuggestedRoomKeys = new ArrayList<>();
            for (Room mRoom : suggestedRoom) {
                mSuggestedRoomKeys.add(mRoom.getRoom_name());
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

    private void showInputRoomDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_input_room, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(false)
                .setTitle("")
                .setMessage(R.string.txt_please_input_desired_room_name)
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the room name
                String roomName = ((EditText) view.findViewById(R.id.edt_room_name)).getText().toString();
                if (TextUtils.isEmpty(roomName)) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.txt_please_input_desired_room_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add the room name into the flow layout
                addFlowItem(roomName);

                dialog.dismiss();
            }
        });
        Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}