package com.wfl.explorerinner;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.wfl.explorer.framework.common.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wfl on 16/1/6.
 * <br>
 * assets目录下的db文件的管理
 */
public class AssetsDatabaseManager {
    private static final String TAG = "AssetsDatabaseManager";
    private static final String PREF_NAME = "assets_database";
    private static final String DIR_NAME ="database";

    private static AssetsDatabaseManager mInstance;
    private Context context;
    private Map<String, SQLiteDatabase> databases = new HashMap<>();

    public static AssetsDatabaseManager getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new AssetsDatabaseManager(context);
        }
        return mInstance;
    }

    private AssetsDatabaseManager(Context context) {
        this.context = context;
    }

    public SQLiteDatabase openDatabase(String dbFile) {
        if (databases.get(dbFile) != null) {
            return databases.get(dbFile);
        }
        if (context == null) {
            return null;
        }
        File dFile = getDatabaseFile(dbFile);
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean hasCopy = sp.getBoolean(dbFile, false);
        LogUtils.v(TAG, "file exist ? " + dFile.exists() + "  " + dFile.getName());
        if (!hasCopy || !dFile.exists()) {
            if (!copyAssetsFile(dbFile, dFile)) {
                LogUtils.e(TAG, "copy database: " + dbFile + " from assets failed");
                return null;
            }
        }
        sp.edit().putBoolean(dbFile, true).apply();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        databases.put(dbFile, db);
        return db;
    }

    /**
     * 获取db文件被拷贝到的文件
     * @param dbFile
     * @return
     */
    private File getDatabaseFile(String dbFile) {
        File dir = context.getDir(DIR_NAME, context.MODE_PRIVATE);
        return new File(dir, dbFile);
    }

    private boolean copyAssetsFile(String dbFile, File dest) {
        if (context == null) {
            return false;
        }
        try {
            InputStream inputStream = context.getAssets().open(dbFile);
            FileOutputStream fileOutputStream = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean closeDatabase(String dbFile) {
        if (databases.containsKey(dbFile)) {
            databases.get(dbFile).close();
            databases.remove(dbFile);
            return true;
        } else {
            LogUtils.w(TAG, "No opened database named: " + dbFile);
            return false;
        }
    }

    public void closeAllDatabase() {
        LogUtils.v(TAG, "close all databases!");
        for (SQLiteDatabase db : databases.values()) {
            db.close();
        }
        databases.clear();
    }
}
