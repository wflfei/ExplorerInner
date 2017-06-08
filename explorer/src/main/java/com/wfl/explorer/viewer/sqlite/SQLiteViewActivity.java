package com.wfl.explorer.viewer.sqlite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wfl.explorer.R;
import com.wfl.explorer.base.BaseActivity;
import com.wfl.explorer.filehelper.sqlite.SQLiteWrapper;
import com.wfl.explorer.filehelper.sqlite.SQLiteWrapperImpl;
import com.wfl.explorer.framework.common.file.FileHelper;
import com.wfl.explorer.framework.common.utils.IntentUtils;

import java.util.List;

/**
 * Created by sn on 2017/5/24.
 */

public class SQLiteViewActivity extends BaseActivity implements TableEditDialogFragment.OnEditResultListener {
    public static final String KEY_PATH = "database_path";
    private String mPath;
    private ListView mListView;
    private List<String> mTables;
    private SQLiteWrapper mSQLiteWrapper;
    private SQLiteTableFragment mTableFragment;
    private FragmentManager mFragmentManager;


    public SQLiteWrapper getSQLiteWrapper() {
        return mSQLiteWrapper;
    }

    public static void startViewDatabase(@NonNull Context context, String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        Intent intent = new Intent(context, SQLiteViewActivity.class);
        IntentUtils.wrapIntentForStartActivity(context, intent);
        intent.putExtra(KEY_PATH, path);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_view);

        mPath = getIntent().getStringExtra(KEY_PATH);
        if (TextUtils.isEmpty(mPath)) {
            finish();
            return;
        }
        mFragmentManager = getSupportFragmentManager();


        initViews();
    }

    private void initViews() {
        mListView = findView(R.id.sqlite_view_tables_lv);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTable(mTables.get(position));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "add");
        return super.onCreateOptionsMenu(menu);
    }

    private void showTables() {
        if (mTableFragment != null && mTableFragment.isAdded()) {
            mFragmentManager.beginTransaction().remove(mTableFragment).commit();
        }
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTables));
    }

    private void showTable(String tableName) {
        setTitle(tableName);
        mTableFragment = SQLiteTableFragment.createInstance(tableName);
        mFragmentManager.beginTransaction().add(R.id.sqlite_view_table_frag_container, mTableFragment).addToBackStack("table").commit();
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (mTableFragment == null || !mTableFragment.isAdded()) {
                    setTitle(FileHelper.getFileName(mPath));
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mSQLiteWrapper == null) {
            mSQLiteWrapper = new SQLiteWrapperImpl(mPath);
        }
        mSQLiteWrapper.open();
        mTables = mSQLiteWrapper.getTables();
        mSQLiteWrapper.close();
        showTables();
        setTitle(FileHelper.getFileName(mPath));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSQLiteWrapper != null) {
            mSQLiteWrapper.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onEditConfirum(int index, List<String> rowData) {
        if (mTableFragment != null && mTableFragment.isVisible()) {
            mTableFragment.onEditConfirm(index, rowData);
        }
    }
}
