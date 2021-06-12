package com.meembusoft.iot.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AndroidAssetManager {

    /*
     * Example: "api_responses/category_list.json"
     * */
    public static String readTextFileFromAsset(Context context, String fileFullPathWithExtension) {
        String line = "";
        String result = "";
        BufferedReader reader = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open(fileFullPathWithExtension);
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder e = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                e.append(line + "\n");
            }
            result = e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                return result;
            }
        }
        Log.d("File :", result);
        return result;
    }

    public static String readTextFileFromRaw(Context context, int resourceID) {
        String line = "";
        String result = "";
        BufferedReader reader = null;
        try {
            InputStream is = context.getResources().openRawResource(resourceID);
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder e = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                e.append(line + "\n");
            }
            result = e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                return result;
            }
        }
        Log.d("File :", result);
        return result;
    }
}
