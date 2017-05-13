package com.wfl.explorer.filetype.types;

import android.content.Context;

import com.wfl.explorer.R;
import com.wfl.explorer.filetype.BaseFileType;

import java.io.File;

/**
 * Created by sn on 2017/5/13.
 */

public class SqliteType extends BaseFileType {

    @Override
    protected String[] getSuffixs() {
        return new String[] {"db"};
    }

    @Override
    public void open(Context context, File file) {
        // TODO: 2017/5/13 open Sqlite database
    }

    @Override
    public int getIconRes(File file) {
        return R.drawable.type_icon_db;
    }
}
