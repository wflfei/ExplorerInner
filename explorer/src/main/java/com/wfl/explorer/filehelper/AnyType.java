package com.wfl.explorer.filehelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by wfl on 16/8/2.
 */
public class AnyType implements FileType {
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
