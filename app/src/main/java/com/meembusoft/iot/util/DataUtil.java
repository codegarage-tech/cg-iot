package com.meembusoft.iot.util;

import android.content.Context;

import com.meembusoft.iot.R;
import com.meembusoft.iot.enumeration.ConnectionType;
import com.meembusoft.iot.model.Category;
import com.meembusoft.iot.model.Configuration;
import com.meembusoft.iot.model.Connection;
import com.meembusoft.iot.model.Device;
import com.meembusoft.iot.model.Product;
import com.meembusoft.iot.model.ResponseOfflineCategory;
import com.meembusoft.iot.model.ResponseOfflineRoom;
import com.meembusoft.iot.model.Room;
import com.meembusoft.retrofitmanager.APIResponse;

import java.util.ArrayList;
import java.util.List;

import io.armcha.ribble.presentation.navigationview.NavigationId;
import io.armcha.ribble.presentation.navigationview.NavigationItem;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DataUtil {

    private static String TAG = DataUtil.class.getSimpleName();

    // Asset file path
    public static final String ASSET_FILE_RESPONSE_BASE_PATH = "api_responses/";
    public static final String ASSET_FILE_NAME_ROOM_LIST = "room_list.json";
    public static final String ASSET_FILE_PATH_ROOM_LIST = ASSET_FILE_RESPONSE_BASE_PATH + ASSET_FILE_NAME_ROOM_LIST;
    public static final String ASSET_FILE_NAME_CATEGORY_WISE_PRODUCT_LIST = "category_wise_product_list.json";
    public static final String ASSET_FILE_PATH_CATEGORY_WISE_PRODUCT_LIST = ASSET_FILE_RESPONSE_BASE_PATH + ASSET_FILE_NAME_CATEGORY_WISE_PRODUCT_LIST;
    public static final String ASSET_FILE_NAME_ROOM_WISE_DEVICE_LIST = "room_wise_device_list.json";
    public static final String ASSET_FILE_PATH_ROOM_WISE_DEVICE_LIST = ASSET_FILE_RESPONSE_BASE_PATH + ASSET_FILE_NAME_ROOM_WISE_DEVICE_LIST;
    public static final String ASSET_FILE_NAME_ROOM_WISE_FAVORITE_DEVICE_LIST = "room_wise_favorite_device_list.json";
    public static final String ASSET_FILE_PATH_ROOM_WISE_FAVORITE_DEVICE_LIST = ASSET_FILE_RESPONSE_BASE_PATH + ASSET_FILE_NAME_ROOM_WISE_FAVORITE_DEVICE_LIST;

    public static List<Category> getAllCategories(Context context) {
        List<Category> categories = new ArrayList<>();
        String jsonResponse = AndroidAssetManager.readTextFileFromAsset(context, ASSET_FILE_PATH_CATEGORY_WISE_PRODUCT_LIST);
        Logger.d(TAG, TAG + ">>getAllCategories>>jsonResponse: " + jsonResponse);
        ResponseOfflineCategory offlineCategory = APIResponse.getObjectFromJSONString(jsonResponse, ResponseOfflineCategory.class);
        if (offlineCategory != null) {
            Logger.d(TAG, "offlineCategory: " + offlineCategory.toString());
            categories.addAll(offlineCategory.getData());
        }
        return categories;
    }

    public static Product getSpecificProductById(Context context, String productId) {
        Product mProduct = null;
        String jsonResponse = AndroidAssetManager.readTextFileFromAsset(context, ASSET_FILE_PATH_CATEGORY_WISE_PRODUCT_LIST);
        Logger.d(TAG, TAG + ">>getSpecificProductById>>jsonResponse: " + jsonResponse);
        ResponseOfflineCategory offlineCategory = APIResponse.getObjectFromJSONString(jsonResponse, ResponseOfflineCategory.class);
        if (offlineCategory != null) {
            Logger.d(TAG, "offlineCategory: " + offlineCategory.toString());
            for (Category category : offlineCategory.getData()) {
                for (Product product : category.getProducts()) {
                    if (product.getProduct_id().equalsIgnoreCase(productId)) {
                        mProduct = product;
                    }
                }
            }
        }
        return mProduct;
    }

    public static List<Room> getAllPersonalRooms(Context context) {
        List<Room> rooms = new ArrayList<>();
        String jsonResponse = AndroidAssetManager.readTextFileFromAsset(context, ASSET_FILE_PATH_ROOM_WISE_DEVICE_LIST);
        Logger.d(TAG, TAG + ">>getAllPersonalRooms>>jsonResponse: " + jsonResponse);
        ResponseOfflineRoom offlineRoom = APIResponse.getObjectFromJSONString(jsonResponse, ResponseOfflineRoom.class);
        if (offlineRoom != null) {
            Logger.d(TAG, "offlineRooms: " + offlineRoom.toString());
            rooms.addAll(offlineRoom.getData());
        }
        return rooms;
    }

    public static List<Room> getAllRooms(Context context) {
        List<Room> rooms = new ArrayList<>();
        String jsonResponse = AndroidAssetManager.readTextFileFromAsset(context, ASSET_FILE_PATH_ROOM_LIST);
        Logger.d(TAG, TAG + ">>getAllPersonalRooms>>jsonResponse: " + jsonResponse);
        ResponseOfflineRoom offlineRoom = APIResponse.getObjectFromJSONString(jsonResponse, ResponseOfflineRoom.class);
        if (offlineRoom != null) {
            Logger.d(TAG, "offlineRooms: " + offlineRoom.toString());
            rooms.addAll(offlineRoom.getData());
        }
        return rooms;
    }

    public static List<Device> getAllFavoriteDevices(Context context) {
        List<Device> favoriteDevice = new ArrayList<>();
        String jsonResponse = AndroidAssetManager.readTextFileFromAsset(context, ASSET_FILE_PATH_ROOM_WISE_FAVORITE_DEVICE_LIST);
        Logger.d(TAG, TAG + ">>getAllFavoriteDevices>>jsonResponse: " + jsonResponse);
        ResponseOfflineRoom offlineRoom = APIResponse.getObjectFromJSONString(jsonResponse, ResponseOfflineRoom.class);
        if (offlineRoom != null) {
            Logger.d(TAG, "offlineRooms: " + offlineRoom.toString());
            for (Room room : offlineRoom.getData()) {
                for (Device device : room.getDevices()) {
                    if (device.getIs_favorite() == 1) {
                        favoriteDevice.add(device);
                    }
                }
            }
        }
        return favoriteDevice;
    }

    public static Room getSpecificRoomById(Context context, String roomId) {
        Room mRoom = null;
        String jsonResponse = AndroidAssetManager.readTextFileFromAsset(context, ASSET_FILE_PATH_ROOM_WISE_DEVICE_LIST);
        Logger.d(TAG, TAG + ">>getSpecificRoomById>>jsonResponse: " + jsonResponse);
        ResponseOfflineRoom offlineRoom = APIResponse.getObjectFromJSONString(jsonResponse, ResponseOfflineRoom.class);
        if (offlineRoom != null) {
            Logger.d(TAG, "offlineRooms: " + offlineRoom.toString());
            if (offlineRoom.getData().size() > 0) {
                for (Room room : offlineRoom.getData()) {
                    if (room.getRoom_id().equalsIgnoreCase(roomId)) {
                        mRoom = room;
                    }
                }
            }
        }
        return mRoom;
    }

    public static List<NavigationItem> getUserMenu(Context context) {
        List<NavigationItem> navigationItems = new ArrayList<>();
        if (SessionUtil.getUser(context) != null) {
            navigationItems.add(new NavigationItem(NavigationId.DASHBOARD, R.drawable.ic_menu_device, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.PRODUCTS, R.drawable.ic_menu_order, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.ADD_DEVICE, R.drawable.ic_menu_about, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.SETTINGS, R.drawable.ic_menu_settings, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.LOGOUT, R.drawable.ic_menu_logout, false, R.color.bg_white));
        } else {
            navigationItems.add(new NavigationItem(NavigationId.PRODUCTS, R.drawable.ic_menu_order, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.SETTINGS, R.drawable.ic_menu_settings, false, R.color.bg_white));
//            navigationItems.add(new NavigationItem(NavigationId.ADD_DEVICE, R.drawable.ic_menu_about, false, R.color.bg_white));
//            navigationItems.add(new NavigationItem(NavigationId.LOGIN, R.drawable.ic_menu_login, false, R.color.bg_white));
        }
        return navigationItems;
    }

    public static boolean hasConfiguration(Device device, ConnectionType connectionType) {
        boolean hasConfiguration = false;
        if (device != null && (device.getDevice_configurations() != null && device.getDevice_configurations().size() > 0)) {
            for (Configuration configuration : device.getDevice_configurations()) {
                if (configuration.getConnection().getConnection_name().equalsIgnoreCase(connectionType.getValue())) {
                    return true;
                }
            }
        }
        return hasConfiguration;
    }

    public static Connection getConnection(Device device, ConnectionType connectionType) {
        if (device != null && (device.getProduct().getProduct_connections() != null && device.getProduct().getProduct_connections().size() > 0)) {
            for (Connection connection : device.getProduct().getProduct_connections()) {
                if (connection.getConnection_name().equalsIgnoreCase(connectionType.getValue())) {
                    return connection;
                }
            }
        }
        return null;
    }

    public static Configuration getConfiguration(Device device, ConnectionType connectionType) {
        if (device != null && (device.getDevice_configurations() != null && device.getDevice_configurations().size() > 0)) {
            for (Configuration configuration : device.getDevice_configurations()) {
                if (configuration.getConnection().getConnection_name().equalsIgnoreCase(connectionType.getValue())) {
                    return configuration;
                }
            }
        }
        return null;
    }
}