package com.wfl.explorer.filetree;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.wfl.explorer.R;
import com.wfl.explorer.filetype.FileTypeHelper;

import java.io.File;
import java.util.List;

/**
 * Created by wfl on 16/8/2.
 */
public class SingleFile extends AbsFileTree {
    FileTypeHelper fileTypeHelper;

    public SingleFile(File file, String name, FileTree upFileTree) {
        super(file, name, upFileTree);
        fileTypeHelper = new FileTypeHelper(file);
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public FileTree next(Context context, String childName) {
        fileTypeHelper.open(context);
        return null;
    }

    @Override
    public int getIconRes() {
        return fileTypeHelper.getFileType().getIconRes(getFile());
    }

    @Override
    public void displayIcon(Activity activity, ImageView imageView) {
        fileTypeHelper.getFileType().displayIcon(activity, imageView, getFile());
    }

    @Override
    public List<FileTree> getFileLists() {
        return null;
    }
}
