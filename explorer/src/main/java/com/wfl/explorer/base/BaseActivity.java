package com.wfl.explorer.base;

import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by wfl on 16/8/1.
 */
public class BaseActivity extends FragmentActivity {
    
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
}
