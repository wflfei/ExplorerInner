package com.wfl.explorer.filetype;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.wfl.explorer.R;
import com.wfl.explorer.framework.common.file.FileHelper;

import java.io.File;

/**
 * Created by sn on 2017/5/13.
 */

public abstract class BaseFileType implements FileType {

    protected abstract String[] getSuffixs();

    @Override
    public boolean isMine(File file) {
        String suffix = FileHelper.getFileSuffix(file);
        String[] suffixs = getSuffixs();
        if (suffixs == null) {
            return false;
        }
        for (String s : suffixs) {
            if (suffix.equals(s)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public int getIconRes(File file) {
        return R.drawable.type_icon_file;
    }

    @Override
    public Drawable getIconDrawable(File file) {
        return null;
    }

    @Override
    public void displayIcon(Activity activity, ImageView imageView, File file) {

    }
}
