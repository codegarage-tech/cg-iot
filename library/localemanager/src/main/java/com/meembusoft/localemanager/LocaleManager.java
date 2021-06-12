package com.meembusoft.localemanager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.meembusoft.localemanager.languagesupport.LanguagesSupport.Language;
import com.meembusoft.localemanager.utils.Constants;
import com.meembusoft.localemanager.utils.Utils;

import java.util.Locale;

public class LocaleManager {

    public static void initialize(Context context) {
        if (TextUtils.isEmpty(getSelectedLanguageId(context))) {
            setSelectedLanguageId(context, Language.ENGLISH);
        }
        updateResources(context, getSelectedLanguageId(context));
    }

    public static void initialize(Context context, @Language String language) {
        setSelectedLanguageId(context, language);
        updateResources(context, language);
    }

    public static void setLocale(Context context, @Language String language) {
        setSelectedLanguageId(context, language);
        if (updateResources(context, language)) {
            Utils.restartActivity((Activity) context);
        }
    }

    private static boolean updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if (Utils.isAtLeastAndroidVersion(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            context.createConfigurationContext(configuration);
        } else {
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        }
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }

    private static SharedPreferences getDefaultSharedPreference(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()) != null) {
            return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        } else {
            return null;
        }
    }

    private static void setSelectedLanguageId(Context context, @Language String id) {
        final SharedPreferences prefs = getDefaultSharedPreference(context.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.KEY, id);
        editor.apply();
    }

    public static String getSelectedLanguageId(Context context) {
        return getDefaultSharedPreference(context.getApplicationContext()).getString(Constants.KEY, Language.ENGLISH);
    }
}