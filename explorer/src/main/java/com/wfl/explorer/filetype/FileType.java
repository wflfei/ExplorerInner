package com.wfl.explorer.filetype;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by wfl on 16/8/2.
 */
public interface FileType {
    boolean isMine(File file);
    void open(Context context, File file);
    int getIconRes(File file);
    Drawable getIconDrawable(File file);
    void displayIcon(Activity activity, ImageView imageView, File file);
}
