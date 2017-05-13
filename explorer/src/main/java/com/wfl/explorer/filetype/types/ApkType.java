package com.wfl.explorer.filetype.types;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.wfl.explorer.filetype.BaseFileType;
import com.wfl.explorer.framework.common.app.App;

import java.io.File;

/**
 * Created by sn on 2017/5/13.
 */

public class ApkType extends BaseFileType {

    @Override
    protected String[] getSuffixs() {
        return new String[] {"apk"};
    }

    @Override
    public void open(Context context, @NonNull File file) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getIconRes(File file) {
        return 0;
    }

    @Override
    public Drawable getIconDrawable(File file) {
        Context context = App.getInstance().getContext();
        return getUninstallApkIcon(context, file.getAbsolutePath());
//        PackageManager pm = context.getPackageManager();
//        PackageInfo packageInfo = pm.getPackageArchiveInfo(file.getPath(), 0);
//        packageInfo.applicationInfo.sourceDir = file.getAbsolutePath();
//        packageInfo.applicationInfo.publicSourceDir = file.getAbsolutePath();
//        Drawable iconDrawable = packageInfo.applicationInfo.loadIcon(packageManager);
//        if(iconDrawable == null){
//            return null;
//        }
//
//        if(iconDrawable instanceof BitmapDrawable && ((BitmapDrawable) iconDrawable).getBitmap() == ((BitmapDrawable) packageManager.getDefaultActivityIcon()).getBitmap()){
//            return null;
//        }
//
//        return iconDrawable;
    }

    @Override
    public void displayIcon(Activity activity, ImageView imageView, File file) {
        imageView.setImageDrawable(getUninstallApkIcon(activity, file.getAbsolutePath()));
    }

    public static Drawable getUninstallApkIcon(Context context, String apkPath) {
        Resources res = getResource(context, apkPath);
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = info.applicationInfo;
        if (appInfo.icon != 0) {
            Drawable icon = res.getDrawable(appInfo.icon);
            //AssetManager must be closed to avoid ProcessKiller after unmounting usb disk.
            res.getAssets().close();
            return icon;
        }
        return null;
    }

    public static Resources getResource(Context context, String apkPath) {
        AssetManager assetManager = createAssetManager(apkPath);
        return new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
    }

    //利用反射调用AssetManager的addAssetPath()方法
    private static AssetManager createAssetManager(String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            AssetManager.class.getDeclaredMethod("addAssetPath", String.class).invoke(
                    assetManager, apkPath);
            return assetManager;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }
}
