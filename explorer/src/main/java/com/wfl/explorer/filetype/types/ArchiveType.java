package com.wfl.explorer.filetype.types;

import android.content.Context;

import com.wfl.explorer.R;
import com.wfl.explorer.filetype.BaseFileType;

import java.io.File;

/**
 * Created by sn on 2017/5/13.
 */

public class ArchiveType extends BaseFileType {
    private static final String[] SUFFIXS = new String[] {"zip", "ZIP", "rar", "RAR"};

    @Override
    protected String[] getSuffixs() {
        return SUFFIXS;
    }

    @Override
    public void open(Context context, File file) {

    }

    @Override
    public int getIconRes(File file) {
        return R.drawable.type_icon_archive;
    }
}
