package com.wfl.explorer.framework.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sn on 2017/5/24.
 */

public class IntentUtils {

    public static void wrapIntentForStartActivity(Context context, Intent intent) {
        if (context == null || context instanceof Activity) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
