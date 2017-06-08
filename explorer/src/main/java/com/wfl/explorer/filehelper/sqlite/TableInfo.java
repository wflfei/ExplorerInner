package com.wfl.explorer.filehelper.sqlite;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wfl on 17/6/2.
 */

public class TableInfo implements Parcelable {
    public List<Column> columns;
    
    public List<Column> primaryKeys;
    
    
    public static class Column {
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

    public Column getColumnByCid(int cid) {
        if (columns == null || columns.size() == 0) {
            return null;
        }
        for (Column column:
             columns) {
            if (column.cid == cid) {
                return column;
            }
        }
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.columns);
        dest.writeList(this.primaryKeys);
    }

    public TableInfo() {
    }

    protected TableInfo(Parcel in) {
        this.columns = new ArrayList<Column>();
        in.readList(this.columns, Column.class.getClassLoader());
        this.primaryKeys = new ArrayList<Column>();
        in.readList(this.primaryKeys, Column.class.getClassLoader());
    }

    public static final Parcelable.Creator<TableInfo> CREATOR = new Parcelable.Creator<TableInfo>() {
        @Override
        public TableInfo createFromParcel(Parcel source) {
            return new TableInfo(source);
        }

        @Override
        public TableInfo[] newArray(int size) {
            return new TableInfo[size];
        }
    };
}
