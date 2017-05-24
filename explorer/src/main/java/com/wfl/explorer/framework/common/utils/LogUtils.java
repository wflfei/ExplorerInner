package com.wfl.explorer.framework.common.utils;

import android.util.Log;

/**
 * Created by wfl on 17/4/5.
 */

public class LogUtils {
    
    
    public static void v(Class cls, String msg) {
        String tag = cls.getSimpleName();
        Log.v(tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void d(Class cls, String msg) {
        String tag = cls.getSimpleName();
        Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void w(Class cls, String msg) {
        String tag = cls.getSimpleName();
        Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void e(Class cls, String msg) {
        String tag = cls.getSimpleName();
        Log.v(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.v(tag, msg);
    }
}
