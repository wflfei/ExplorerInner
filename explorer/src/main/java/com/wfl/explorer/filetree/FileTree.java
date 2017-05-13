package com.wfl.explorer.filetree;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

/**
 * Created by wfl on 16/8/1.
 */
public interface FileTree {
    boolean isDirectory();
    boolean canGoBack();
    FileTree back();
    FileTree next(Context context, String childName);
    int getIconRes();
    void displayIcon(Activity activity, ImageView imageView);
    String getName();
    List<FileTree> getFileLists();
    
    FileTree getUpFileTree();
    File getFile();
    
    Bundle toBundle();
    
}
