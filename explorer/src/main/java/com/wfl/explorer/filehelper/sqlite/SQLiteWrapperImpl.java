package com.wfl.explorer.filehelper.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wfl on 17/5/18.
 */

public class SQLiteWrapperImpl implements SQLiteWrapper {
    private String mFilePath;
    SQLiteDatabase mDatabase;
    boolean mValid = false;
    
    public SQLiteWrapperImpl(String path) {
        mFilePath = path;
        if (TextUtils.isEmpty(path) || !(new File(path).exists())) {
            mValid = false;
        } else {
            mValid = true;
        }
    }

    @Override
    public void open() {
        open(0);
    }

    @Override
    public void open(int flags) {
        if (mValid) {
            try {
                mDatabase = SQLiteDatabase.openDatabase(mFilePath, null , SQLiteDatabase.OPEN_READWRITE, mDatabaseErrorHandler);
            } catch (SQLiteException e) {
                e.printStackTrace();
                mValid = false;
            }
        }
    }
    
    private DatabaseErrorHandler mDatabaseErrorHandler = new DatabaseErrorHandler() {
        @Override
        public void onCorruption(SQLiteDatabase dbObj) {
            mValid = false;
        }
    };

    @Override
    public void close() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    @Override
    public boolean isValid() {
        return mValid && (mDatabase != null);
    }

    @Override
    public List<String> getTables() {
        if (!isValid()) {
            return null;
        }
        List<String> tables = new ArrayList<>();
        Cursor cursor = querySQL("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                tables.add(name);
            }
        }
        return tables;
    }

    @Override
    public Map<String, String> getTableStructure(String tableName) {
        Cursor cursor = querySQL(String.format("PRAGMA table_info(%s)", tableName));
        Map<String, String> result = new HashMap<>();
        if (cursor != null) {
            int count = 0;
            while (cursor.moveToNext()) {
//                int colCount = cursor.getColumnCount();
//                String value = "";
//                for (int i = 0; i < colCount; i++) {
//                    value += cursor.getColumnNames()[i];
//                    
//                }
                result.put(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("type")));
                count++;
            }
        }
        return result;
    }

    @Override
    public TableInfo getTableInfo(String tableName) {
        Cursor cursor = querySQL(String.format("PRAGMA table_info(%s)", tableName));
        List<TableInfo.Column> columns = new ArrayList<>();
        TableInfo tableInfo = new TableInfo();
        if (cursor != null) {
            int count = 0;
            tableInfo.primaryKeys = new ArrayList<>();
            while (cursor.moveToNext()) {
//                int colCount = cursor.getColumnCount();
//                String value = "";
//                for (int i = 0; i < colCount; i++) {
//                    value += cursor.getColumnNames()[i];
//                    value += "...";
//                }
                
                TableInfo.Column column = new TableInfo.Column();
                column.name = cursor.getString(cursor.getColumnIndex("name"));
                column.cid = cursor.getInt(cursor.getColumnIndex("cid"));
                column.type = cursor.getString(cursor.getColumnIndex("type"));
                column.notnull = cursor.getInt(cursor.getColumnIndex("notnull")) == 1;
                column.pk = cursor.getInt(cursor.getColumnIndex("pk")) == 1;
                column.dflt_value = cursor.getString(cursor.getColumnIndex("dflt_value"));
                columns.add(column);
                if (column.pk) {
                    tableInfo.primaryKeys.add(column);
                }
                count++;
            }
        }
        tableInfo.columns = columns;
        return tableInfo;
    }

    @Override
    public Cursor getQueryCursor(String tableName) {
        return null;
    }

    @Override
    public Map<String, String> nextRowData(Cursor cursor) {
        return null;
    }

    @Override
    public Map<String, String> getRowData(String tableName, int row) {
        return null;
    }

    @Override
    public List<String> getColumNames(String tableName) {
        Cursor cursor = querySQL("SELECT * from " + tableName + ";");
        if (cursor != null) {
            String[] columns = cursor.getColumnNames();
            return Arrays.asList(columns);
        }
        return new ArrayList<>();
    }

    @Override
    public List<List<String>> getDataLimited(String tableName, int limited) {
        Cursor cursor = querySQL("SELECT * from " + tableName + " limit " + limited);
        List<List<String>> result = new ArrayList<>();
        if (cursor != null) {
            int columnCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                List<String> rowData = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    String data = getAndConvertData(cursor, i);
                    if (data != null) {
                        rowData.add(data);
                    }
                }
                result.add(rowData);
            }
        }
        return result;
    }


//    @Override
//    public List<Map<String, String>> getDataLimited(String tableName, int limited) {
//        return null;
//    }

    @Override
    public int updateRowDataOfTable(String tableName, TableInfo tableInfo, List<String> rowData) {
        ContentValues cv = new ContentValues();
        if (tableInfo == null) {
            tableInfo = getTableInfo(tableName);
        }
        if (tableInfo == null || tableInfo.columns == null || tableInfo.columns.size() == 0) {
            return -1;
        }
        for (int i=0; i<tableInfo.columns.size(); i++) {
            cv.put(tableInfo.columns.get(i).name, rowData.get(tableInfo.columns.get(i).cid));
        }
        return update(tableName, cv, primaryKeyWhereClause(tableInfo), primaryKeyWhereArgs(tableInfo, rowData));
    }

    @Override
    public int deleteRowDataOfTable(String tableName, TableInfo tableInfo, List<String> rowData) {
        if (tableInfo == null) {
            tableInfo = getTableInfo(tableName);
        }
        if (tableInfo == null || tableInfo.columns == null || tableInfo.columns.size() == 0) {
            return -1;
        }
        return delete(tableName, primaryKeyWhereClause(tableInfo), primaryKeyWhereArgs(tableInfo, rowData));
    }

    public void execSQL(String sql) {
        if (!isValid()) {
            return;
        }
        mDatabase.execSQL(sql);
    }
    
    private Cursor querySQL(String sql) {
        if (!isValid()) {
            return null;
        }
        return mDatabase.rawQuery(sql, null);
    }
    
    private int update(String tableName, ContentValues cv, String whereClause, String[] whereArgs) {
        if (!isValid()) {
            return -1;
        }
        return mDatabase.update(tableName,cv, whereClause, whereArgs);
    }
    
    private int delete(String tableName, String whereClause, String[] whereArgs) {
        if (!isValid()) {
            return -1;
        }
        return mDatabase.delete(tableName, whereClause, whereArgs);
    }
    
    
    private String getAndConvertData(@NonNull Cursor cursor, int index) {
        if (index < 0 || index >= cursor.getColumnCount()) {
            return null;
        }
        int dataType = cursor.getType(index);
        String result;
        switch (dataType) {
            case Cursor.FIELD_TYPE_STRING:
                result = cursor.getString(index);
            break;
            case Cursor.FIELD_TYPE_INTEGER:
                result = String.valueOf(cursor.getInt(index));
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                result = String.valueOf(cursor.getFloat(index));
                break;
            case Cursor.FIELD_TYPE_BLOB:
                result = "Blob Data";
                break;
            case Cursor.FIELD_TYPE_NULL:
                result = "NULL";
                break;
            default:
                result = "NULL";
                break;
        }
        return result;
    }
    
    
    
    
    private String primaryKeyWhereClause(TableInfo tableInfo) {
        if (tableInfo == null) {
            return null;
        }
        StringBuilder where = new StringBuilder();
        for (int i=0; i<tableInfo.columns.size(); i++) {
            if (tableInfo.columns.get(i).pk) {
                if (where.length() > 0) {
                    where.append(" AND ");
                }
                where.append(tableInfo.columns.get(i).name);
                where.append(" = ?");
            }
        }
        return where.toString();
    }

    private String[] primaryKeyWhereArgs(TableInfo tableInfo, List<String> rowData) {
        if (tableInfo == null || rowData == null) {
            return null;
        }
        List<String> pks = new ArrayList<>();
        for (int i=0; i<tableInfo.columns.size(); i++) {
            if (tableInfo.columns.get(i).pk) {
                if (rowData.size() <= tableInfo.columns.get(i).cid) {
                    pks.add("");
                } else {
                    pks.add(rowData.get(tableInfo.columns.get(i).cid));
                }
            }
        }
        return pks.toArray(new String[] {});
    }
}
