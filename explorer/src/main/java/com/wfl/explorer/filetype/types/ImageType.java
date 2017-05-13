package com.wfl.explorer.filetype.types;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wfl.explorer.R;
import com.wfl.explorer.activity.ImageViewActivity;
import com.wfl.explorer.filetype.BaseFileType;

import java.io.File;

/**
 * Created by wfl on 16/8/2.
 */
public class ImageType extends BaseFileType {
    private static final String[] SUFFIXS = new String[] {"jpg", "jpeg", "png", "PNG", "JPG", "JPEG"};

    @Override
    protected String[] getSuffixs() {
        return SUFFIXS;
    }

    @Override
    public void open(Context context, File file) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(type_icon_file), "image/jpeg");
//        context.startActivity(intent);
        openSelf(context, file);
    }

    private void openSelf(Context context, File file) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra(ImageViewActivity.IMAGE, Uri.fromFile(file));
        context.startActivity(intent);
    }

    @Override
    public int getIconRes(File file) {
        return 0;
    }

    @Override
    public void displayIcon(Activity activity, ImageView imageView, File file) {
        Glide.with(activity)
                .load(file)
                .placeholder(R.drawable.type_icon_file)
                .into(imageView);
    }
}
