package com.wfl.explorer.filetree;

import android.content.Context;

import com.wfl.explorer.R;
import com.wfl.explorer.filehelper.FileTypeHelper;

import java.io.File;
import java.io.FilenameFilter;
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
        return R.drawable.file;
    }

    @Override
    public List<FileTree> getFileLists() {
        return null;
    }
}
