package com.meembusoft.localemanager.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;

import java.util.Locale;

import static android.content.pm.PackageManager.GET_META_DATA;
import static android.os.Build.VERSION_CODES.N;

public class Utils {
    /**
     * Simply compares the given version with the {@linkplain
     * Build.VERSION#SDK_INT SDK_INT} of the Android System.
     *
     * @param version version to compare.
     * @return {@code boolean} evaluated 'True' if the current version is the
     * same or higher than the given one. Else, 'false' is returned.
     */
    public static boolean isAtLeastAndroidVersion(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    /**
     * Obtains the current locale being used by the system.
     *
     * @param resources the system resources (obtained from a {@link Context},
     *                  for example) from which the locale is obtained.
     * @return {@code Locale} with the user configuration.
     */
    public static Locale getLocale(@NonNull Resources resources) {
        Configuration config = resources.getConfiguration();
        return Utils.isAtLeastAndroidVersion(N) ?
                config.getLocales().get(0) :
                config.locale;
    }

    /**
     * Obtains the Android system locale, even if the user has changed the
     * application one.
     *
     * @param config configuration from which the system locale will be
     *               obtained.
     * @return {@code Locale} with the system language.
     */
    public static Locale getSystemLocale(@NonNull Configuration config) {
        return isAtLeastAndroidVersion(N) ?
                config.getLocales().get(0) :
                config.locale;
    }

    /**
     * Updates the string resource identifier (in the package's resources) of
     * the activity's label.
     *
     * @param activity the source activity from which the title will be changed
     *                 - it cannot be {@code null} and it must exist.
     * @see PackageManager#getActivityInfo(ComponentName, int)
     */
    public static void resetActivityTitle(@NonNull Activity activity) {
        try {
            ActivityInfo info = activity.getPackageManager().getActivityInfo(activity.getComponentName(), GET_META_DATA);
            if (info.labelRes != 0) {
                activity.setTitle(info.labelRes);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            // We do not handle this error as it should never happen - we are
            // getting the componentName from the proper activity, so the
            // package with that given name must exists
        }
    }

    public static void restartActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            Intent intent = activity.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.finish();
            activity.overridePendingTransition(0, 0);

            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
        }
    }
}