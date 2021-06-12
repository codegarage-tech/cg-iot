package com.meembusoft.iot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.reversecoder.library.event.OnSingleClickListener;

import java.util.ArrayList;
import java.util.List;

import io.armcha.ribble.presentation.widget.AnimatedTextView;
import com.meembusoft.iot.R;
import com.meembusoft.iot.adapter.StaggeredListAdapter;
import com.meembusoft.iot.base.BaseActivity;
import com.meembusoft.iot.model.StaggeredItem;
import com.meembusoft.iot.util.AppUtil;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StaggeredListActivity extends BaseActivity {

    //Toolbar
    private LinearLayout llLeftMenu, llRightMenu;
    private ImageView ivRightMenu;
    private AnimatedTextView toolbarTitle;
    private RecyclerView rvStaggeredList;
    private Button btnAdd;
    private StaggeredListAdapter staggeredListAdapter;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_staggered_list;
    }

    @Override
    public void initStatusBarView() {
        StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void initActivityViews() {
        llLeftMenu = (LinearLayout) findViewById(R.id.ll_left_menu);
        llRightMenu = (LinearLayout) findViewById(R.id.ll_right_menu);
        ivRightMenu = (ImageView) findViewById(R.id.right_menu);
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbar_title);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        rvStaggeredList = (RecyclerView) findViewById(R.id.rv_staggered_list);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        toolbarTitle.setAnimatedText("CUSTOM STAGGERED LIST", 0L);
        AppUtil.applyMarqueeOnTextView(toolbarTitle);

        staggeredListAdapter = new StaggeredListAdapter(getActivity());
        rvStaggeredList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvStaggeredList.setAdapter(staggeredListAdapter);
        staggeredListAdapter.setAllData(initStaggeredData(), 6);
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        llLeftMenu.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                initActivityBackPress();
            }
        });

        btnAdd.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                staggeredListAdapter.addData(addStaggeredData(),6);
            }
        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        finish();
    }

    @Override
    public void initActivityDestroyTasks() {

    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private List<StaggeredItem> initStaggeredData() {
        List<StaggeredItem> staggeredListItems = new ArrayList<>();
        staggeredListItems.add(new StaggeredItem("1", "Rashed", ""));
        staggeredListItems.add(new StaggeredItem("2", "Alam", ""));
        staggeredListItems.add(new StaggeredItem("3", "Horzot", ""));
        staggeredListItems.add(new StaggeredItem("4", "Belal", ""));
        staggeredListItems.add(new StaggeredItem("5", "Shohel", ""));
        staggeredListItems.add(new StaggeredItem("6", "Rasel", ""));
        staggeredListItems.add(new StaggeredItem("7", "Shihab", ""));
        staggeredListItems.add(new StaggeredItem("8", "Maruf", ""));
        staggeredListItems.add(new StaggeredItem("9", "Gobinda", ""));
        staggeredListItems.add(new StaggeredItem("10", "Sadikul", ""));
        staggeredListItems.add(new StaggeredItem("11", "Abir", ""));
        staggeredListItems.add(new StaggeredItem("12", "Aqib", ""));
        staggeredListItems.add(new StaggeredItem("13", "Helal", ""));
        staggeredListItems.add(new StaggeredItem("14", "Nazmul", ""));

        return staggeredListItems;
    }

    private List<StaggeredItem> addStaggeredData() {
        List<StaggeredItem> staggeredListItems = new ArrayList<>();
        staggeredListItems.add(new StaggeredItem("1", "Rahim", ""));
        staggeredListItems.add(new StaggeredItem("2", "Kolim", ""));
        staggeredListItems.add(new StaggeredItem("3", "Solim", ""));
        staggeredListItems.add(new StaggeredItem("4", "Junayed", ""));
        staggeredListItems.add(new StaggeredItem("5", "Sefali", ""));

        return staggeredListItems;
    }
}