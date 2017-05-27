package com.wfl.explorer.framework.common.file;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by sn on 2017/5/13.
 */

public class FileHelper {

    /**
     * Get the suffix of the type_icon_file, return "" if no suffix found.
     * @param file
     * @return
     */
    public static String getFileSuffix(@NonNull File file) {
        String name = file.getName();
        if (!name.contains(".") || name.indexOf(".") == name.length() - 1) {
            return "";
        }
        return name.substring(name.lastIndexOf(".") + 1);
    }

    public static String getFileName(@NonNull String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        if (!path.contains("/") || path.indexOf("/") == path.length() - 1) {
            return path;
        }
        return path.substring(path.lastIndexOf("/") + 1);
    }
    
}
