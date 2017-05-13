package com.wfl.explorer.filehelper;

import android.content.Context;

import java.io.File;

/**
 * Created by wfl on 16/8/2.
 */
public interface FileType {
    boolean isMine(File file);
    void open(Context context, File file);
}
