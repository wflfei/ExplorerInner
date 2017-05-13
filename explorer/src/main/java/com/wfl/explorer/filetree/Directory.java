package com.wfl.explorer.filetree;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.wfl.explorer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wfl on 16/8/2.
 */
public class Directory extends AbsFileTree {
    private List<FileTree> mFileList;
    
    public Directory(File file, String name, FileTree upFileTree) {
        super(file, name, upFileTree);
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public FileTree next(Context context, String childName) {
        File childFile = new File(mFile.getAbsolutePath() + File.separator + childName);
        if (childFile.exists()) {
            FileTree fileTree;
            if (childFile.isDirectory()) {
                fileTree = new Directory(childFile, childName, this);
            } else {
                fileTree = new SingleFile(childFile, childName, this);
            }
            return fileTree;
        }
        return null;
    }

    @Override
    public int getIconRes() {
        return R.drawable.type_icon_directory;
    }

    @Override
    public void displayIcon(Activity activity, ImageView imageView) {

    }

    @Override
    public List<FileTree> getFileLists() {
        if (mFileList == null) {
            String[] names = mFile.list();
            mFileList = new ArrayList<>();
            if (names != null && names.length > 0) {
                for (String name : names) {
                    mFileList.add(next(null, name));
                }
            }
            
            FileTree up = getUpFileTree();
            if (up != null) {
                mFileList.add(0, getUpFileTree());
            }
            
        }
        return mFileList;
    }
}
