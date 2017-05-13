package com.wfl.explorer.model;

import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;

import com.wfl.explorer.filetree.FileTree;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by wfl on 16/8/1.
 */
public class PageInfo {

    public static final int MODE_BROWSE = 1;
    public static final int MODE_COPY = 2;

    @IntDef({MODE_BROWSE, MODE_COPY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PageMode{}
    
    
    public Fragment fragment;
    public String title;
    public String icon;
    
    public String current;
    public FileTree fileTree;
    
    public @PageInfo.PageMode int mode;
}
