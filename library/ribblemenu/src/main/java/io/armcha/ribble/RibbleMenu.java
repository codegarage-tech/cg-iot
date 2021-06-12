package io.armcha.ribble;

import android.content.Context;

public class RibbleMenu {

    private static Context mContext;

    public static void initRibbleMenu(Context context) {
        if (mContext == null) {
            mContext = context;
        }
    }

    public static Context getRibbleContext() {
        return mContext;
    }
}
