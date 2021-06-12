package com.meembusoft.addtocart;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.meembusoft.addtocart.util.Util;
import com.meembusoft.realmmanager.RealmManager;

import java.util.List;

import io.realm.RealmObject;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AddToCartManager {

    private static String TAG = "AddToCartManager";
    private static AddToCartManager mAddToCartManager;
    private static Application mApplication;

    public static AddToCartManager getInstance() {
        if (mAddToCartManager == null || mApplication == null) {
            initialize(mApplication);
        }
        return mAddToCartManager;
    }

    public static void initialize(Application application) {
        if (mAddToCartManager == null || mApplication == null) {
            mApplication = application;
            mAddToCartManager = new AddToCartManager();
            createAddToCartDb(mApplication, false);
        }
    }

    private static void createAddToCartDb(Application application, boolean forceUpdate) {
        // Initialize realm database
        String dbName = Util.getApplicationName(application);
        dbName = (!TextUtils.isEmpty(dbName) ? (dbName + ".realm").replaceAll(" ", "_") : "meembusoft.realm");
        long dbVersion = 1;
        RealmManager.initialize(application, dbName, dbVersion, forceUpdate);
    }

    public <E extends RealmObject> void addOrUpdateCart(E dbObject) {
        try {
            RealmManager.with(mApplication).insertOrUpdate(dbObject);
        } catch (Exception e) {
            Log.d(TAG, "Failed to addOrUpdateCart: " + e.getMessage());
        }
    }

    public <E extends RealmObject> List<E> getAllCartItems(Class<E> modelClass) {
        try {
            return RealmManager.with(mApplication).getAllListData(modelClass);
        } catch (Exception e) {
            Log.d(TAG, "Failed to getAllCartItems: " + e.getMessage());
        }
        return null;
    }

    public <E extends RealmObject> RealmObject getCartItem(Class<E> modelClass, String fieldName, String value) {
        try {
            return RealmManager.with(mApplication).getData(modelClass, fieldName, value);
        } catch (Exception e) {
            Log.d(TAG, "Failed to getCartItem: " + e.getMessage());
        }
        return null;
    }

    public <E extends RealmObject> List<E> getAllSelectedCartItems(Class<E> modelClass, String fieldName, boolean value) {
        try {
            return RealmManager.with(mApplication).getAllSpecificData(modelClass, fieldName, value);
        } catch (Exception e) {
            Log.d(TAG, "Failed to getAllSelectedCartItems: " + e.getMessage());
        }
        return null;
    }


    public <E extends RealmObject> void deleteItem(Class<E> modelClass, String fieldName, String value) {
        try {
            RealmManager.with(mApplication).deleteData(modelClass, fieldName, value);
        } catch (Exception e) {
            Log.d(TAG, "Failed to deleteItem: " + e.getMessage());
        }
    }

    public <E extends RealmObject> void deleteAllCartItems(Class<E> modelClass) {
        try {
            RealmManager.with(mApplication).deleteAllData(modelClass);
        } catch (Exception e) {
            Log.d(TAG, "Failed to deleteData: " + e.getMessage());
        }
    }

    public <E extends RealmObject> boolean isCartItemExist(Class<E> modelClass, String fieldName, String value) {
        try {
            return RealmManager.with(mApplication).isDataExist(modelClass,fieldName,value);
        } catch (Exception e) {
            Log.d(TAG, "Failed to isDataExist: " + e.getMessage());
        }
        return false;
    }

    public <E extends RealmObject> boolean hasCartItem(Class<E> modelClass) {
        try {
            return RealmManager.with(mApplication).hasData(modelClass);
        } catch (Exception e) {
            Log.d(TAG, "Failed to hasData: " + e.getMessage());
        }
        return false;
    }
}