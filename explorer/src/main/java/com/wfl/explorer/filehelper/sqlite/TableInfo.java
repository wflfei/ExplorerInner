package com.wfl.explorer.filehelper.sqlite;

import java.util.List;

/**
 * Created by wfl on 17/6/2.
 */

public class TableInfo {
    public List<String> columnNames;
    public List<Column> columns;
    
    public List<Column> primaryKeys;
    
    
    static class Column {
        public int cid;
        public String name;
        public String type;
        public boolean notnull;
        public String dflt_value;
        // pk
        public boolean pk;


        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof Column)) {
                return false;
            }
            return cid == ((Column) o).cid;
        }
    }
}
