package com.wfl.explorer.framework.common.utils;

import android.content.Context;

/**
 * Created by sn on 2017/5/25.
 */

public class DisplayUtils {

    public static int dp2px(Context context, float dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + .5f);
    }

    public static int getScrrenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
