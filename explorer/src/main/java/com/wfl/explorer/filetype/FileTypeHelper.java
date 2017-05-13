package com.wfl.explorer.filetype;

import android.content.Context;

import com.wfl.explorer.filetype.types.AnyType;
import com.wfl.explorer.filetype.types.ApkType;
import com.wfl.explorer.filetype.types.ArchiveType;
import com.wfl.explorer.filetype.types.ImageType;
import com.wfl.explorer.filetype.types.SqliteType;
import com.wfl.explorer.filetype.types.TextType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wfl on 16/8/2.
 */
public class FileTypeHelper {
    List<FileType> types = new ArrayList<>();
    
    File mFile;
    
    public FileTypeHelper(File file) {
        this.mFile = file;
        registerFileTypes();
    }
    
    private void registerFileTypes() {
        types.add(new TextType());
        types.add(new ImageType());
        types.add(new SqliteType());
        types.add(new ApkType());
        types.add(new ArchiveType());
    }
    
    
    public FileType getFileType() {
        for (FileType type:
             types) {
            if (type.isMine(mFile)) {
                return type;
            }
        }
        return new AnyType();
    }
    
    public void open(Context context) {
        getFileType().open(context, mFile);
    }
    
    
}
