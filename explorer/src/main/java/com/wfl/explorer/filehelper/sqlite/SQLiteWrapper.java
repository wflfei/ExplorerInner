package com.wfl.explorer.filehelper.sqlite;

import android.database.Cursor;

import java.util.List;
import java.util.Map;

/**
 * Created by wfl on 17/5/17.
 */

public interface SQLiteWrapper {
    
    boolean isValid();
    
    void open(int flags);
    void open();

    void close();
    
    List<String> getTables();
    
    Map<String, String> getTableStructure(String tableName);

    TableInfo getTableInfo(String tableName);
    
    Cursor getQueryCursor(String tableName);
    Map<String, String> nextRowData(Cursor cursor);
    
    Map<String, String> getRowData(String tableName, int row);

    List<String> getColumNames(String tableName);
    List<List<String>> getDataLimited(String tableName, int limited);
    
//    List<Map<String, String>> getDataLimited(String tableName, int limited);
    
    int updateRowDataOfTable(String tableName, TableInfo tableInfo, List<String> rowData);
    
    int deleteRowDataOfTable(String tableName, TableInfo tableInfo, List<String> rowData);
}
