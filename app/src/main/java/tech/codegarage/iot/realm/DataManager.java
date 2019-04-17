package tech.codegarage.iot.realm;

import android.app.Activity;

import tech.codegarage.iot.model.Room;
import tech.codegarage.iot.util.Logger;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DataManager {

    private static final String TAG = "DataManager";

    //Database table key
    public static final String TABLE_ROOM_KEY_ID = "id";

    public static void addOrUpdateRoom(Activity activity, Room room) {
        RealmManager mRealmManager = RealmManager.with(activity);
        mRealmManager.insertOrUpdate(room);
        Logger.d(TAG, TAG + ">>> addOrUpdateRoom: " + ((Room) mRealmManager.getData(Room.class, TABLE_ROOM_KEY_ID, room.getId()+"")).toString());
    }

    public static boolean hasRoomAdded(Activity activity) {
        RealmManager mRealmManager = RealmManager.with(activity);
        boolean isExist = mRealmManager.hasData(Room.class);
        Logger.d(TAG, TAG + ">>> hasRoomAdded: " + isExist);
        return isExist;
    }

//    public static void storeSelectedFoodItem(Activity activity, FoodItem foodItem) {
//        RealmManager mRealmManager = RealmManager.with(activity);
//        mRealmManager.insertOrUpdate(foodItem);
//        Log.d(AppUtil.class.getSimpleName(), "onOrderNowClick>>> storeSelectedFoodItem: " + ((FoodItem) mRealmManager.getData(FoodItem.class, AllConstants.TABLE_KEY_FOOD_ITEM, foodItem.getProduct_id())).toString());
//    }
//
//    public static void deleteSelectedFoodItem(Activity activity, FoodItem foodItem) {
//        RealmManager mRealmManager = RealmManager.with(activity);
//        mRealmManager.deleteData(FoodItem.class, AllConstants.TABLE_KEY_FOOD_ITEM, foodItem.getProduct_id());
//    }
//
//    public static void deleteAllStoredFoodItems(Activity activity) {
//        RealmManager mRealmManager = RealmManager.with(activity);
//        mRealmManager.deleteAllData(FoodItem.class);
//    }
//
//    public static FoodItem getStoredFoodItem(Activity activity, FoodItem foodItem) {
//        RealmManager mRealmManager = RealmManager.with(activity);
//        FoodItem mFoodItem = ((FoodItem) mRealmManager.getData(FoodItem.class, AllConstants.TABLE_KEY_FOOD_ITEM, foodItem.getProduct_id()));
//        Log.d(AppUtil.class.getSimpleName(), "onOrderNowClick>>> getStoredFoodItem: " + mFoodItem.toString());
//        return mFoodItem;
//    }
//
//    public static List<FoodItem> getAllStoredFoodItems(Activity activity) {
//        RealmManager mRealmManager = RealmManager.with(activity);
//        List<FoodItem> data = mRealmManager.getAllListData(FoodItem.class);
//        return (data != null ? data : new ArrayList<FoodItem>());
//    }
//
//    public static boolean isFoodItemStored(Activity activity, FoodItem foodItem) {
//        RealmManager mRealmManager = RealmManager.with(activity);
//        boolean isExist = mRealmManager.isDataExist(FoodItem.class, AllConstants.TABLE_KEY_FOOD_ITEM, foodItem.getProduct_id());
//        Log.d(AppUtil.class.getSimpleName(), "onOrderNowClick>>> isFoodItemStored: " + isExist);
//        return isExist;
//    }
//
//    public static boolean hasStoredFoodItem(Activity activity) {
//        RealmManager mRealmManager = RealmManager.with(activity);
//        boolean isExist = mRealmManager.hasData(FoodItem.class);
//        Log.d(AppUtil.class.getSimpleName(), "onOrderNowClick>>> hasStoredFoodItem: " + isExist);
//        return isExist;
//    }
}
