package tech.codegarage.iot.activity;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;

import tech.codegarage.iot.R;
import tech.codegarage.iot.base.BaseLocationActivity;
import tech.codegarage.iot.geocoding.ReverseGeocoderTask;
import tech.codegarage.iot.geocoding.UserLocationAddress;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AddDeviceActivity extends BaseLocationActivity {

    //Background task
    private ReverseGeocoderTask currentLocationTask;
    private UserLocationAddress mLocationAddress;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public void onLocationFound(Location location) {
//        if (location != null) {
//
//            //Check internet connection
//            if (NetworkManager.isConnected(getActivity())) {
//                if (FragmentUtilsManager.getVisibleSupportFragment(HomeActivity.this, lastSelectedItem.getNavigationId().name()) instanceof ProductsFragment) {
//                    //Request reverse geocoding for address
//                    if ((currentLocationTask != null) && (currentLocationTask.getStatus() == AsyncTask.Status.RUNNING)) {
//                        currentLocationTask.cancel(true);
//                    }
//
//                    currentLocationTask = new ReverseGeocoderTask(getActivity(), new LocationAddressListener() {
//                        @Override
//                        public void getLocationAddress(UserLocationAddress locationAddress) {
//                            if (locationAddress != null) {
//                                mLocationAddress = locationAddress;
//                                Logger.d(TAG, "UserLocationAddress: " + mLocationAddress.toString());
////                                        String addressText = String.format("%s, %s, %s, %s", locationAddress.getStreetAddress(), locationAddress.getCity(), locationAddress.getState(), locationAddress.getCountry());
//
//                                //Set address to the toolbar
//                                setToolBarTitle(mLocationAddress.getAddressLine());
//                            }
//                        }
//                    });
//                    currentLocationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, location);
//                }
//            }
//        }
    }

    @Override
    public LOCATION_UPDATE_FREQUENCY initLocationUpdateFrequency() {
        return LOCATION_UPDATE_FREQUENCY.ONCE;
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_add_device;
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

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }else{
            finish();
        }
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissProgressDialog();

        if (currentLocationTask != null && currentLocationTask.getStatus() == AsyncTask.Status.RUNNING) {
            currentLocationTask.cancel(true);
        }
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
}
