package tech.codegarage.iot.section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.reversecoder.library.util.AllSettingsManager;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import tech.codegarage.iot.R;
import tech.codegarage.iot.enumeration.ScreenType;
import tech.codegarage.iot.model.Device;
import tech.codegarage.iot.retrofit.APIResponse;
import tech.codegarage.iot.util.AppUtil;
import tech.codegarage.iot.util.Logger;
import tech.codegarage.iot.util.SessionUtil;
import tech.codegarage.iot.viewholder.HeaderViewHolder;
import tech.codegarage.iot.viewholder.ItemViewHolder;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DeviceSection extends StatelessSection {

    private String TAG = DeviceSection.class.getSimpleName();
    private Context mContext;
    private String mSectionTag;
    private List<Device> mListItem;
    private RecyclerView mSectionedRecyclerView;
    private ScreenType mScreenType;

    public DeviceSection(Context context, ScreenType screenType, String title, List<Device> mListItem, RecyclerView sectionedRecyclerView) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.section_item)
                .headerResourceId(R.layout.section_header)
                .build());
        this.mContext = context;
        this.mScreenType = screenType;
        this.mSectionTag = title;
        this.mListItem = mListItem;
        this.mSectionedRecyclerView = sectionedRecyclerView;
    }

    @Override
    public int getContentItemsTotal() {
        return mListItem.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        final Device device = mListItem.get(position);
        AppUtil.loadImage(mContext, itemHolder.ivItemImage, device.getImage(), false, false, true);
        itemHolder.tvItemName.setText(device.getName());
        itemHolder.tvSubItemName.setText("");
        if (device.isSelected()) {
            itemHolder.ivItemTick.setVisibility(View.VISIBLE);
        } else {
            itemHolder.ivItemTick.setVisibility(View.GONE);
        }

        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "Clicked: "+device.toString());
                Logger.d(TAG, "Clicked: "+device.getConnection_type().size());
                if (mScreenType == ScreenType.ADD_DEVICE) {
                    int lastSelectedItemPosition = SessionUtil.getLastSelectedDevicePosition(mContext);
                    String lastSelectedSectionName = SessionUtil.getLastSelectedDeviceSection(mContext);

                    // Toggle previous selection
                    if (lastSelectedItemPosition != -1 && !AllSettingsManager.isNullOrEmpty(lastSelectedSectionName)) {
                        DeviceSection mDeviceSection = (DeviceSection) ((SectionedRecyclerViewAdapter) mSectionedRecyclerView.getAdapter()).getSection(lastSelectedSectionName);
                        Device mDevice = mDeviceSection.mListItem.get(lastSelectedItemPosition);
                        if (mDevice != null) {
                            mDevice.setSelected(!mDevice.isSelected());
                            ((SectionedRecyclerViewAdapter) mSectionedRecyclerView.getAdapter()).notifyItemChangedInSection(lastSelectedSectionName, lastSelectedItemPosition);
                        }
                    }

                    device.setSelected(!device.isSelected());
                    SessionUtil.setLastSelectedDevice(mContext, APIResponse.getResponseString(device));
                    SessionUtil.setLastSelectedDevicePosition(mContext, ((SectionedRecyclerViewAdapter) mSectionedRecyclerView.getAdapter()).getPositionInSection(itemHolder.getAdapterPosition()));
                    SessionUtil.setLastSelectedDeviceSection(mContext, mSectionTag);
                    ((SectionedRecyclerViewAdapter) mSectionedRecyclerView.getAdapter()).notifyDataSetChanged();
                } else if (mScreenType == ScreenType.PRODUCTS) {
                    Toast.makeText(mContext, "Device detail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List<Device> getListItem() {
        return mListItem;
    }

    public Device getSelectedItem() {
        int lastSelectedItemPosition = SessionUtil.getLastSelectedDevicePosition(mContext);
        String lastSelectedSectionName = SessionUtil.getLastSelectedDeviceSection(mContext);
        Device mDevice = null;

        if (lastSelectedItemPosition != -1 && !AllSettingsManager.isNullOrEmpty(lastSelectedSectionName)) {
            DeviceSection mDeviceSection = (DeviceSection) ((SectionedRecyclerViewAdapter) mSectionedRecyclerView.getAdapter()).getSection(lastSelectedSectionName);
            mDevice = mDeviceSection.mListItem.get(lastSelectedItemPosition);
        }
        return mDevice;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(mSectionTag);

//        headerHolder.btnMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), String.format("Clicked on more button from the header of Section %s",
//                        mSectionTag),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}