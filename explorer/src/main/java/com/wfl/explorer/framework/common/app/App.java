package com.wfl.explorer.framework.common.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by sn on 2017/5/13.
 */

public class App {
    private static App instance = new App();

    public static App getInstance() {
        return instance;
    }

    private Application mApplication;
    private Context mContext;

    private App() {

    }

    public void init(Application application) {
        this.mApplication = application;
        this.mContext = application.getApplicationContext();
    }

    public Application getApplication() {
        return mApplication;
    }

    public Context getContext() {
        return mContext;
    }
}
