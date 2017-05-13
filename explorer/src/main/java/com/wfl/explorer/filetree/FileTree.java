package com.wfl.explorer.filetree;

import android.content.Context;
import android.os.Bundle;

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
    String getName();
    List<FileTree> getFileLists();
    
    FileTree getUpFileTree();
    File getFile();
    
    Bundle toBundle();
    
}
