package com.wfl.explorer.filetree;

import android.os.Bundle;

import java.io.File;
import java.util.List;

/**
 * Created by wfl on 16/8/2.
 */
public abstract class AbsFileTree implements FileTree {
    protected File mFile;
    protected String name;
    protected FileTree upFileTree;
    
    public AbsFileTree(File file, String name, FileTree upFileTree) {
        this.mFile = file;
        this.name = name;
        this.upFileTree = upFileTree;
    }

    @Override
    public boolean canGoBack() {
        return upFileTree != null;
    }

    @Override
    public FileTree back() {
        return upFileTree;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public File getFile() {
        return mFile;
    }

    @Override
    public FileTree getUpFileTree() {
        return upFileTree;
    }

    @Override
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("current", mFile.getAbsolutePath());
        bundle.putString("name", getName());
        return bundle;
    }
    
    public static FileTree getFileTree(String current, String name) {
        File childFile = new File(current);
        if (childFile.exists()) {
            FileTree fileTree;
            if (childFile.isDirectory()) {
                fileTree = new Directory(childFile, name, null);
            } else {
                fileTree = new SingleFile(childFile, name, null);
            }
            return fileTree;
        }
        return null;
    }
}
