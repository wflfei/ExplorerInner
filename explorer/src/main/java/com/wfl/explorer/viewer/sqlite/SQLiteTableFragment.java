package com.wfl.explorer.viewer.sqlite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sn on 2017/5/25.
 */

public class SQLiteTableFragment extends Fragment {
    private String mTableName;


    public static SQLiteTableFragment createInstance(String path) {
        SQLiteTableFragment fragment = new SQLiteTableFragment();
        Bundle args = new Bundle();
        args.putString("path", path);
        fragment.setArguments(args);
        return fragment;
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
        TextView textView = new TextView(getActivity());
        textView.setText(mTableName);
        return textView;

//        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
