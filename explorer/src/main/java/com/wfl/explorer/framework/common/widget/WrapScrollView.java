package com.wfl.explorer.framework.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

/**
 * Created by wfl on 17/6/14.
 */

public class WrapScrollView extends ScrollView {
    public WrapScrollView(Context context) {
        super(context);
    }

    public WrapScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WrapScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        
        int width = wSize, height = hSize;
        
        int childCount = getChildCount();
        if (childCount > 0) {
            View child = getChildAt(0);
            FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int wChildSpec, hChildSpec;
            if (lp.width == LayoutParams.MATCH_PARENT) {
                wChildSpec = MeasureSpec.makeMeasureSpec(wSize - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
            } else {
                wChildSpec = MeasureSpec.makeMeasureSpec(wSize - getPaddingLeft() - getPaddingRight(), MeasureSpec.AT_MOST);
            }
            hChildSpec = MeasureSpec.makeMeasureSpec(hSize - getPaddingTop() - getPaddingBottom(), MeasureSpec.AT_MOST);
            
            child.measure(wChildSpec, hChildSpec);
            
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            
            if (wMode == MeasureSpec.EXACTLY) {
                width = wSize;
            } else {
                width = childWidth + getPaddingLeft() + getPaddingRight();
                if (width > wSize) {
                    width = wSize;
                }
            }

            if (hMode == MeasureSpec.EXACTLY) {
                height = hSize;
            } else {
                height = childHeight + getPaddingTop() + getPaddingBottom();
                if (height > hSize) {
                    height = hSize;
                }
            }
        }
        setMeasuredDimension(width, height);
    }
}
