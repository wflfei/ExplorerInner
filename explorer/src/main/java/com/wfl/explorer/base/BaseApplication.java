package com.wfl.explorer.base;

import android.app.Application;
import android.content.Context;

import com.wfl.explorer.framework.common.app.App;

/**
 * Created by sn on 2017/5/13.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        App.getInstance().init(this);
    }
}
