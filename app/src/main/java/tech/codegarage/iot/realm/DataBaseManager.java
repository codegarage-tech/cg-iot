package tech.codegarage.iot.realm;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import tech.codegarage.iot.model.Room;
import tech.codegarage.iot.util.Logger;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class DataBaseManager {

    private static final String TAG = "DataBaseManager";

    //Database table key
    public static final String TABLE_ROOM_KEY_NAME = "name";
    public static final String TABLE_ROOM_KEY_IS_SELECTED = "isSelected";

    public static void initSuggestedRooms(Activity activity) {
        List<Room> suggestedRoom = new ArrayList<Room>() {{
            add(new Room("Bed Room", ""));
            add(new Room("Balcony", ""));
            add(new Room("Kitchen", ""));
            add(new Room("Garden", ""));
            add(new Room("Outdoor", ""));
            add(new Room("Leaving Room", ""));
            add(new Room("Drawing Room", ""));
            add(new Room("Store Room", ""));
            add(new Room("Wash Room", ""));
            add(new Room("Security light", ""));
        }};
        for (Room room : suggestedRoom) {
            addOrUpdateRoom(activity, room);
        }
    }

    public static List<Room> getAllRooms(Activity activity) {
        RealmManager mRealmManager = RealmManager.with(activity);
        List<Room> data = mRealmManager.getAllListData(Room.class);
        return (data != null ? data : new ArrayList<Room>());
    }

    public static List<Room> getAllSelectedRooms(Activity activity) {
        RealmManager mRealmManager = RealmManager.with(activity);
        List<Room> data = mRealmManager.getAllSpecificData(Room.class, TABLE_ROOM_KEY_IS_SELECTED, true);
        return (data != null ? data : new ArrayList<Room>());
    }

    public static Room addOrUpdateRoom(Activity activity, Room room) {
        RealmManager mRealmManager = RealmManager.with(activity);
        mRealmManager.insertOrUpdate(room);

        RealmObject insertedRoom = mRealmManager.getData(Room.class, TABLE_ROOM_KEY_NAME, room.getName());
        Logger.d(TAG, TAG + ">>> addOrUpdateRoom: " + insertedRoom);
        return (insertedRoom != null ? (Room) insertedRoom : null);
    }

    public static boolean hasRoomInitialized(Activity activity) {
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
