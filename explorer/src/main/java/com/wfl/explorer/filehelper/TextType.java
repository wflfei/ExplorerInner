package com.wfl.explorer.filehelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import com.wfl.explorer.activity.TextViewActivity;

import java.io.File;

/**
 * Created by wfl on 16/8/2.
 */
public class TextType implements FileType {
    public static final String[] PREFIX = new String[] {"txt", "html", "xml", "log"};

    @Override
    public boolean isMine(File file) {
        String prefix = file.getName().substring(file.getName().lastIndexOf('.') + 1);
        for (String s : PREFIX) {
            if (prefix.equals(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void open(Context context, File file) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "text/plain");
//        context.startActivity(intent);
        openSelf(context, file);
    }
    
    private void openSelf(Context context, File file) {
        Intent intent = new Intent(context, TextViewActivity.class);
        intent.putExtra(TextViewActivity.TEXT, file.getAbsolutePath());
        context.startActivity(intent);
    }
}
