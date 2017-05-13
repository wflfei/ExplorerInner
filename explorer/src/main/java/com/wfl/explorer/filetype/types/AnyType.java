package com.wfl.explorer.filetype.types;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.wfl.explorer.R;
import com.wfl.explorer.filetype.BaseFileType;

import java.io.File;

/**
 * Created by wfl on 16/8/2.
 */
public class AnyType extends BaseFileType {
    @Override
    protected String[] getSuffixs() {
        return new String[0];
    }

    @Override
    public boolean isMine(File file) {
        return false;
    }

    @Override
    public void open(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "*/*");
        Intent chooser = Intent.createChooser(intent, "请选择打开方式");
        context.startActivity(chooser);
    }
}
