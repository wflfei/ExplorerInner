package com.wfl.explorer.viewer.sqlite;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wfl.explorer.filehelper.sqlite.SQLiteWrapper;
import com.wfl.explorer.filehelper.sqlite.TableInfo;
import com.wfl.explorer.viewer.SqliteTableView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sn on 2017/5/25.
 */

public class SQLiteTableFragment extends Fragment {
    private String mTableName;
    private SQLiteWrapper mSQLiteWrapper;
    
    // Data
    List<String> mColNames;
    List<List<String>> mData;
    TableInfo mTableInfo;
    
    
    // View
    SQLiteTableView mSqliteTableView;


    public static SQLiteTableFragment createInstance(String path) {
        SQLiteTableFragment fragment = new SQLiteTableFragment();
        Bundle args = new Bundle();
        args.putString("path", path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (activity instanceof SQLiteViewActivity) {
            mSQLiteWrapper = ((SQLiteViewActivity) activity).getSQLiteWrapper();
        }
        mTableName = getArguments().getString("path");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mSqliteTableView = new SQLiteTableView(getContext());
        mSqliteTableView.setBackgroundColor(Color.WHITE);
        mSqliteTableView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSQLiteWrapper.open();
        mTableInfo = mSQLiteWrapper.getTableInfo(mTableName);
        mColNames = mSQLiteWrapper.getColumNames(mTableName);
        mSqliteTableView.setColumnNames(mColNames);
        mData = mSQLiteWrapper.getDataLimited(mTableName, 100);
        mSQLiteWrapper.close();
        mSqliteTableView.setDatas(mData);
//        mSqliteTableView.setOnTableActionsListener(mOnTableActionsListener);
        return mSqliteTableView;

//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onEditConfirm(int index, List<String> rowData) {
        if (mSQLiteWrapper != null) {
            mSQLiteWrapper.open();
            mSQLiteWrapper.updateRowDataOfTable(mTableName, mTableInfo, rowData);
            mData = mSQLiteWrapper.getDataLimited(mTableName, 100);
            mSQLiteWrapper.close();
            mSqliteTableView.setDatas(mData);
            mSqliteTableView.requestLayout();
        }
    }
    
    SqliteTableView.OnTableActionsListener mOnTableActionsListener = new SqliteTableView.OnTableActionsListener() {
        @Override
        public void onLongPress(int index, List<String> rowData) {

            TableEditDialogFragment dialogFragment = TableEditDialogFragment.newInstance(mTableInfo, index, (ArrayList<String>) rowData);
            dialogFragment.show(getChildFragmentManager(), "dialog_edit");
        }

        @Override
        public void onClick(int index, List<String> rowData) {

        }
    };

}
