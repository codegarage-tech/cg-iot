package tech.codegarage.iot.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.armcha.ribble.presentation.navigationview.NavigationId;
import io.armcha.ribble.presentation.navigationview.NavigationItem;
import tech.codegarage.iot.R;
import tech.codegarage.iot.model.Device;
import tech.codegarage.iot.model.Product;
import tech.codegarage.iot.model.ResponseOfflineDevice;
import tech.codegarage.iot.retrofit.APIResponse;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DataUtil {

    private static String TAG = DataUtil.class.getSimpleName();

    public static Device getSpecificDeviceById(String deviceId) {
        Device device = null;
        ResponseOfflineDevice offlineDevice = APIResponse.getResponseObject(DEFAULT_DEVICE_LIST, ResponseOfflineDevice.class);
        if (offlineDevice != null) {
            Logger.d(TAG, "offlineDevice: " + offlineDevice.toString());
            if (offlineDevice.getData().size() > 0) {
                for (Product product : offlineDevice.getData()) {
                    if (product.getDevice().size() > 0) {
                        for (Device mDevice : product.getDevice()) {
                            if (mDevice.getId().equalsIgnoreCase(deviceId)) {
                                device = mDevice;
                            }
                        }
                    }
                }
            }
        }
        return device;
    }

    public static Device getSpecificDeviceByName(String deviceName) {
        Device device = null;
        ResponseOfflineDevice offlineDevice = APIResponse.getResponseObject(DEFAULT_DEVICE_LIST, ResponseOfflineDevice.class);
        if (offlineDevice != null) {
            Logger.d(TAG, "offlineDevice: " + offlineDevice.toString());
            if (offlineDevice.getData().size() > 0) {
                for (Product product : offlineDevice.getData()) {
                    if (product.getDevice().size() > 0) {
                        for (Device mDevice : product.getDevice()) {
                            if (mDevice.getImage().equalsIgnoreCase(deviceName)) {
                                device = mDevice;
                            }
                        }
                    }
                }
            }
        }
        return device;
    }

    public static List<NavigationItem> getUserMenu(Context context) {
        List<NavigationItem> navigationItems = new ArrayList<>();
        if (SessionUtil.getUser(context) != null) {
            navigationItems.add(new NavigationItem(NavigationId.OWN_DEVICES, R.drawable.ic_menu_device, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.PRODUCTS, R.drawable.ic_menu_order, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.ADD_DEVICE, R.drawable.ic_menu_about, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.SETTINGS, R.drawable.ic_menu_settings, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.LOGOUT, R.drawable.ic_menu_logout, false, R.color.bg_white));
        } else {
            navigationItems.add(new NavigationItem(NavigationId.PRODUCTS, R.drawable.ic_menu_order, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.ADD_DEVICE, R.drawable.ic_menu_about, false, R.color.bg_white));
            navigationItems.add(new NavigationItem(NavigationId.LOGIN, R.drawable.ic_menu_login, false, R.color.bg_white));
        }
        return navigationItems;
    }

    // Default response
    public static final String DEFAULT_DEVICE_LIST = "{\n" +
            "  \"status\": \"1\",\n" +
            "  \"message\": \"Response successful\",\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"id\": \"1\",\n" +
            "      \"name\": \"Socket\",\n" +
            "      \"image\": \"https://d2hpkjovixmy47.cloudfront.net/697-large_default/porcelain-single-2-pin-socket-white.jpg\",\n" +
            "      \"device\": [\n" +
            "        {\n" +
            "          \"id\": \"69\",\n" +
            "          \"name\": \"Single 2 Pin Socket\",\n" +
            "          \"image\": \"https://i.ebayimg.com/images/g/IPEAAOSwIsdcJfeq/s-l300.jpg\",\n" +
            "          \"connection_type\": [\n" +
            "            \"Bluetooth\",\n" +
            "            \"Wifi\",\n" +
            "            \"Mobile Data\"\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"70\",\n" +
            "          \"name\": \"Double 2 Pin Socket\",\n" +
            "          \"image\": \"https://images-na.ssl-images-amazon.com/images/I/41osJl4rz9L.jpg\",\n" +
            "          \"connection_type\": [\n" +
            "            \"Bluetooth\",\n" +
            "            \"Wifi\",\n" +
            "            \"Mobile Data\"\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"5\",\n" +
            "      \"name\": \"Holder\",\n" +
            "      \"image\": \"https://media.screwfix.com/is/image//ae235?src=ae235/93835_P&$prodImageMedium$\",\n" +
            "      \"device\": [\n" +
            "        {\n" +
            "          \"id\": \"75\",\n" +
            "          \"name\": \"Pin Holder\",\n" +
            "          \"image\": \"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQv0M8kTPLP5oJ4y2A0KhSrdGMoJ-iAtkhZtCsHrnsv26lhKQ8T\",\n" +
            "          \"connection_type\": [\n" +
            "            \"Bluetooth\",\n" +
            "            \"Wifi\",\n" +
            "            \"Mobile Data\"\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"76\",\n" +
            "          \"name\": \"Patch Holder\",\n" +
            "          \"image\": \"https://img.dxcdn.com/productimages/sku_245535_1.jpg\",\n" +
            "          \"connection_type\": [\n" +
            "            \"Bluetooth\",\n" +
            "            \"Wifi\",\n" +
            "            \"Mobile Data\"\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}