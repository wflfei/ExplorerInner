package com.wfl.explorer.fragment;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.wfl.explorer.R;
import com.wfl.explorer.filetree.Directory;
import com.wfl.explorer.filetree.FileTree;
import com.wfl.explorer.model.PageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wfl on 16/8/2.
 */
public class TabManager {
    
    public static final int TAB_MY = 1;
    public static final int TAB_EXTERNAL = 2;
    
    
    public static List<PageInfo> getInitPages(Context context, int[] ids) {
        List<PageInfo> pageInfos = new ArrayList<>();
        if (ids == null || ids.length == 0) {
            return pageInfos;
        }
        for (int i = 0; i < ids.length; i++) {
            pageInfos.add(getPage(context, ids[i]));
        }
        return pageInfos;
    }
    
    public static PageInfo getPage(Context context, int id) {
        PageInfo page;
        switch (id) {
            case TAB_MY:
                File file = new File(context.getFilesDir().getParent());
                page = getPageByFile(context, file, null);
                break;
            case TAB_EXTERNAL:
                page = getPageByFile(context, Environment.getExternalStorageDirectory(), context.getString(R.string.external_storage_name));
                break;
            default:
                page = getPageByFile(context, context.getFilesDir(), null);
                break;
        }
        return page;
    }
    
    public static PageInfo getPageByFile(Context context, File file, String customName) {
        String name = customName == null ? file.getName() : customName;
        FileTree fileTree = new Directory(file, name, null);
        PageInfo page = new PageInfo();
        page.title = name;
        page.fileTree = fileTree;
        return page;
    }
    
}
