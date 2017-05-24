package com.wfl.explorer.viewer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by wfl on 17/5/1.
 */

public class SqliteTableView extends View {
    List<String> mColumnNames;
    List<List<String>> mDatas;

    private int column = 0;
    private int num = 0;

    
    public SqliteTableView(Context context) {
        super(context);
    }

    public SqliteTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SqliteTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SqliteTableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
