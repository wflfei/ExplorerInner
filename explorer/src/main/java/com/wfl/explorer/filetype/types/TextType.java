package com.wfl.explorer.filetype.types;

import android.content.Context;
import android.content.Intent;

import com.wfl.explorer.activity.TextViewActivity;
import com.wfl.explorer.filetype.BaseFileType;

import java.io.File;

/**
 * Created by wfl on 16/8/2.
 */
public class TextType extends BaseFileType {
    private static final String[] SUFFIXS = new String[] {"txt", "html", "xml", "log"};

    @Override
    protected String[] getSuffixs() {
        return SUFFIXS;
    }

    @Override
    public void open(Context context, File file) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(type_icon_file), "text/plain");
//        context.startActivity(intent);
        openSelf(context, file);
    }
    
    private void openSelf(Context context, File file) {
        Intent intent = new Intent(context, TextViewActivity.class);
        intent.putExtra(TextViewActivity.TEXT, file.getAbsolutePath());
        context.startActivity(intent);
    }
}
