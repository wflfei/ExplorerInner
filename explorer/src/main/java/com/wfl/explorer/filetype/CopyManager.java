package com.wfl.explorer.filetype;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wfl on 16/8/2.
 */
public class CopyManager {
    private static CopyManager instance = new CopyManager();
    private List<Destination> destinations = new ArrayList<>();
    private String source;
    
    private CopyManager() {
        
    }
    
    public static CopyManager getInstance() {
        if (instance == null) {
            instance = new CopyManager();
        }
        return instance;
    }
    
    public void registerDestination(Destination destination) {
        this.destinations.add(destination);
    }
    
    public void unregisterDestination(Destination destination) {
        this.destinations.remove(destination);
    }
    
    public String getSource() {
        return source;
    }
    
    public void startCopy(String path) {
        source = path;
        for (Destination dest : destinations) {
            dest.newCopy(path);
        }
    }

    public void endCopy(String path) {
        for (Destination dest : destinations) {
            dest.endCopy(path);
        }
        source = null;
    }
    
    public void copy(Context context, String from, String to) {
        File srcFile = new File(from);
        String destName = to + File.separator + srcFile.getName();
        if (srcFile.isDirectory()) {
            copyDirectory(from, destName, false);
        } else {
            copyFile(from, destName, false);
        }
        
    }

    public static boolean copyFile(String srcFileName, String destFileName,
                                   boolean overlay) {
        File srcFile = new File(srcFileName);
        
        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }
        
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            if (overlay) {
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录  
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在  
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败  
                    return false;
                }
            }
        }

        // 复制文件  
        int byteread = 0; // 读取的字节数  
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean copyDirectory(String srcDirName, String destDirName,
                                        boolean overlay) {
        // 判断源目录是否存在  
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            return false;
        } else if (!srcDir.isDirectory()) {
            return false;
        }

        // 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符  
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File destDir = new File(destDirName);
        // 如果目标文件夹存在  
        if (destDir.exists()) {
            // 如果允许覆盖则删除已存在的目标目录  
            if (overlay) {
                new File(destDirName).delete();
            } else {
                return false;
            }
        } else {
            // 创建目的目录
            if (!destDir.mkdirs()) {
                return false;
            }
        }

        boolean flag = true;
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 复制文件  
            if (files[i].isFile()) {
                flag = copyFile(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = copyDirectory(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        } else {
            return true;
        }
    }

    public boolean backPress() {
        if (source != null) {
            endCopy(source);
            return true;
        }
        return false;
    }
    
    public interface Destination {
        void newCopy(String path);
        void endCopy(String path);
    }
}
