package com.wfl.explorer.viewer.sqlite;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wfl.explorer.filehelper.sqlite.SQLiteWrapper;
import com.wfl.explorer.viewer.SqliteTableView;

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
    
    
    // View
    SqliteTableView mSqliteTableView;


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
        Activity activity = getActivity();
        if (activity instanceof SQLiteViewActivity) {
            mSQLiteWrapper = ((SQLiteViewActivity) activity).getSQLiteWrapper();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTableName = getArguments().getString("path");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mSqliteTableView = new SqliteTableView(getContext());
        mSqliteTableView.setBackgroundColor(Color.WHITE);
        mSqliteTableView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSQLiteWrapper.open();
        mColNames = mSQLiteWrapper.getColumNames(mTableName);
        mSqliteTableView.setColumnNames(mColNames);
        mData = mSQLiteWrapper.getDataLimited(mTableName, 100);
        mSqliteTableView.setDatas(mData);
        mSqliteTableView.setOnTableActionsListener(mOnTableActionsListener);
        return mSqliteTableView;

//        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    SqliteTableView.OnTableActionsListener mOnTableActionsListener = new SqliteTableView.OnTableActionsListener() {
        @Override
        public void onLongPress(int index, List<String> rowData) {
            
        }

        @Override
        public void onClick(int index, List<String> rowData) {

        }
    };
    
}
