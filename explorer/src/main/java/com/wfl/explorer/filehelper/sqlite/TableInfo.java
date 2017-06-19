package com.wfl.explorer.filehelper.sqlite;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wfl on 17/6/2.
 */

public class TableInfo implements Parcelable {
    public List<Column> columns;
    
    public List<Column> primaryKeys;


    @IntDef({Cursor.FIELD_TYPE_INTEGER, Cursor.FIELD_TYPE_STRING,Cursor.FIELD_TYPE_FLOAT,Cursor.FIELD_TYPE_BLOB,Cursor.FIELD_TYPE_NULL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ColumnType {}

    public static class Column implements Parcelable {
        public int cid;
        public String name;
        public String type;
        public boolean notnull;
        public String dflt_value;
        // pk
        public boolean pk;

//        @ColumnType
        public int typeInt;


        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof Column)) {
                return false;
            }
            return cid == ((Column) o).cid;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.cid);
            dest.writeString(this.name);
            dest.writeString(this.type);
            dest.writeByte(this.notnull ? (byte) 1 : (byte) 0);
            dest.writeString(this.dflt_value);
            dest.writeByte(this.pk ? (byte) 1 : (byte) 0);
            dest.writeInt(this.typeInt);
        }

        public Column() {
        }

        protected Column(Parcel in) {
            this.cid = in.readInt();
            this.name = in.readString();
            this.type = in.readString();
            this.notnull = in.readByte() != 0;
            this.dflt_value = in.readString();
            this.pk = in.readByte() != 0;
            this.typeInt = in.readInt();
        }

        public static final Creator<Column> CREATOR = new Creator<Column>() {
            @Override
            public Column createFromParcel(Parcel source) {
                return new Column(source);
            }

            @Override
            public Column[] newArray(int size) {
                return new Column[size];
            }
        };
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
