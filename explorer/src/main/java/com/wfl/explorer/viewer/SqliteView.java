package com.wfl.explorer.viewer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wfl on 17/5/1.
 */

public class SqliteView extends View {
    
    
    
    public SqliteView(Context context) {
        super(context);
    }

    public SqliteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SqliteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SqliteView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
    }
}
