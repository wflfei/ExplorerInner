package com.wfl.explorer.viewer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.wfl.explorer.framework.common.utils.DisplayUtils;
import com.wfl.explorer.framework.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wfl on 17/5/1.
 */

public class SqliteTableView extends ScrollView {
    private static int DEFAULT_COL_WIDTH;
    private static int MAX_COL_WIDTH;
    private static int STROKE_WIDTH;
    
    private static int ITEM_HORIZONTAL_PADDING;
    private static int ITEM_VERTICAL_PADDING;

    List<String> mColumnNames;
    List<List<String>> mDatas;
    
    private SparseArray<Integer> positions;
    
    private int currentHeight =0;
    private int currentWidth = 0;
    
    private List<Integer> mColWidth;
    private int mItemHeight;

    private TextPaint mTextPaint;
    private Paint mPaint;

    
    public SqliteTableView(Context context) {
        this(context, null);
    }

    public SqliteTableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SqliteTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SqliteTableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        initPaint();
        DEFAULT_COL_WIDTH = DisplayUtils.getScrrenWidth(getContext()) / 5;
        // 默认最大宽度为一个屏幕宽
        MAX_COL_WIDTH = DisplayUtils.getScrrenWidth(getContext());
        STROKE_WIDTH = DisplayUtils.dp2px(getContext(), 0.5f);
        ITEM_HORIZONTAL_PADDING = DisplayUtils.dp2px(getContext(), 10);
        ITEM_VERTICAL_PADDING = DisplayUtils.dp2px(getContext(), 3);
        mColWidth = new ArrayList<>();
        mItemHeight = (int) (mTextPaint.getFontMetrics().bottom-mTextPaint.getFontMetrics().top) + 2 * ITEM_VERTICAL_PADDING + STROKE_WIDTH;

        HorizontalScrollView horiziontalScrollView = new HorizontalScrollView(getContext());
        addView(horiziontalScrollView, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        
        Content content = new Content(getContext());
        content.setBackgroundColor(Color.WHITE);
        horiziontalScrollView.addView(content, new ScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setColumnNames(List<String> columnNames) {
        mColumnNames = columnNames;
        mColWidth.clear();
    }

    public void setDatas(List<List<String>> datas) {
        mDatas = datas;
        mColWidth.clear();
    }

    private void initPaint() {
        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(48);
        mTextPaint.setAntiAlias(true);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }


    class Content extends View {


        public Content(Context context) {
            super(context);
        }

        public Content(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public Content(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public Content(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = measureWidth(MeasureSpec.getSize(widthMeasureSpec));
            int height = measureHeight(MeasureSpec.getSize(heightMeasureSpec));
            LogUtils.v(SqliteTableView.class, "width: " + width + " height: " + height);
            setMeasuredDimension(width, height);
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }

        private int measureWidth(int max) {
            if (mColumnNames == null || mColumnNames.size() == 0) {
                return max;
            }
            int width = 0;
            if (mColWidth.isEmpty()) {
                for (int i = 0; i < mColumnNames.size(); i++) {
                    int maxWidth = measureMaxWidthOfColumn(i);
                    mColWidth.add(maxWidth);
                    width += maxWidth;
                }
            } else {
                for (int w:
                     mColWidth) {
                    width += w;
                }
            }
            if (width < max) {
                return max;
            } else {
                return width;
            }
        }

        private int measureHeight(int max) {
            if (mColumnNames == null || mColumnNames.size() == 0) {
                return max;
            }
            if (mDatas == null || mDatas.size() == 0) {
                return mItemHeight + STROKE_WIDTH;
            }
            int height = mDatas.size() * mItemHeight + STROKE_WIDTH;
            if (height < max) {
                return max;
            } else {
                return height;
            }
        }
        
        private int measureMaxWidthOfColumn(int column) {
            int max = 0;
            if (mColumnNames != null && mColumnNames.size() > column) {
                String nameText = mColumnNames.get(column);
                max = measureItemWidth(nameText);
            }
            if (mDatas == null) {
                return max;
            }
            for (int i = 0; i < mDatas.size(); i++) {
                List<String> rowData = mDatas.get(i);
                if (rowData != null && rowData.size() > column) {
                    String value = rowData.get(column);
                    int len = measureItemWidth(value);
                    if (len > max) {
                        max = len;
                    }
                    if (max >= MAX_COL_WIDTH) {
                        max = MAX_COL_WIDTH;
                        break;
                    }
                }
            }
            return max;
        }
        
        private int measureItemWidth(String text) {
            if (TextUtils.isEmpty(text)) {
                return 0;
            }
            return (int) mTextPaint.measureText(text) + 2 * ITEM_HORIZONTAL_PADDING;
        }

        private int measureItemHeight(String text) {
            if (TextUtils.isEmpty(text)) {
                return 0;
            }
            return mItemHeight;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            resetDrawing();
            drawNames(canvas);
            drawDatas(canvas);
        }

        private void resetDrawing() {
            currentHeight = getPaddingTop();
            currentWidth = getPaddingLeft();
        }

        private void drawNames(Canvas canvas) {
            canvas.drawLine(currentWidth, currentHeight, getWidth() - getPaddingRight(), currentHeight, mPaint);
            currentHeight += STROKE_WIDTH;
            if (mColumnNames == null || mColumnNames.size() == 0) {
                return;
            }
            for (int i=0; i<mColumnNames.size(); i++) {
                String text = mColumnNames.get(i);
                if (TextUtils.isEmpty(text)) {
                    continue;
                }
                drawItem(-1, i, text, canvas, i == 0, i == mColumnNames.size() - 1);
            }

        }

        private void drawDatas(Canvas canvas) {
            if (mDatas == null || mDatas.size() == 0) {
                return;
            }
            for (int i = 0; i < mDatas.size(); i++) {
                List<String> rowData = mDatas.get(i);
                if (rowData != null && rowData.size() > 0) {
                    for (int j = 0; j < rowData.size(); j++) {
                        String text = rowData.get(j);
//                        if (TextUtils.isEmpty(text)) {
//                            continue;
//                        }
                        drawItem(i, j, text, canvas, j == 0, j == rowData.size() - 1);
                    }
                }

            }
        }

        private void drawItem(int rowIndex, int columnIndex, String text, Canvas canvas, boolean isFirst, boolean isLast) {
            int width = mColWidth.get(columnIndex);
            StaticLayout staticLayout = new StaticLayout(text, mTextPaint, width, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
            int lineCount = staticLayout.getLineCount();
            if (lineCount > 1) {
                text = text.substring(0, staticLayout.getLineEnd(0) - 3) + "...";
                staticLayout = new StaticLayout(text, mTextPaint, width, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
            }
            if (isFirst) {
                canvas.drawLine(currentWidth, currentHeight, currentWidth, currentHeight + mItemHeight, mPaint);
            }
            if (rowIndex >= 0) {
                positions.put(rowIndex, currentHeight);
            }
            canvas.translate(currentWidth, currentHeight + ITEM_VERTICAL_PADDING);
            staticLayout.draw(canvas);
            canvas.translate(-currentWidth, -currentHeight - ITEM_VERTICAL_PADDING);
            if (mTouching != null && rowIndex >= 0) {
                if (mTouching.y > currentHeight && mTouching.y < currentHeight + mItemHeight && mTouching.x > currentWidth && mTouching.x < currentWidth + width) {
                    mPaint.setColor(Color.argb(35, 55, 55, 55));
                    canvas.drawRect(currentWidth, currentHeight, currentWidth + width, currentHeight + mItemHeight, mPaint);
                    mPaint.setColor(Color.BLACK);
                }
            }
            currentWidth += width;
            canvas.drawLine(currentWidth, currentHeight, currentWidth, currentHeight + mItemHeight, mPaint);
            if (isLast) {
                currentHeight += mItemHeight;
                currentWidth = getPaddingLeft();
                canvas.drawLine(currentWidth, currentHeight, getWidth() - getPaddingRight(), currentHeight, mPaint);
//                currentHeight += STROKE_WIDTH;
            }
        }


        GestureDetectorCompat mGestureDetectorCompat = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                int x = (int) e.getX();
                int y = (int) e.getY();
                if (mOnTableActionsListener != null) {
                    int index = getTouchedIndex(y);
                    mOnTableActionsListener.onLongPress(index, mDatas.get(index));
                }
                
            }
            
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                int x = (int) e.getX();
                int y = (int) e.getY();
                if (mOnTableActionsListener != null) {
                    int index = getTouchedIndex(y);
                    mOnTableActionsListener.onClick(index, mDatas.get(index));
                }
                return super.onSingleTapUp(e);
            }
        });
        
        private Point mTouching = null;
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = MotionEventCompat.getActionMasked(event);
            if (action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                mTouching = new Point(x, y);
                removeCallbacks(mPress);
                postDelayed(mPress, 50);
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                mTouching = null;
                removeCallbacks(mPress);
                postDelayed(mPress, 50);
            }
            mGestureDetectorCompat.onTouchEvent(event);
            return true;
        }
        
        Runnable mPress = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        
        private int getTouchedIndex(int y) {
            for (int i=0; i<mDatas.size(); i++) {
                int cur = positions.get(i);
                if (y >= cur) {
                    if (i < mDatas.size() - 1) {
                        int next = positions.get(i + 1);
                        if (y < next) {
                            return i;
                        }
                    } else {
                        return i;
                    }
                }
            }
            return 0;
        }
    }
    
    
    
    // Actions
    
    public interface OnTableActionsListener {
        void onLongPress(int index, List<String> rowData);
        void onClick(int index, List<String> rowData);
    }
    
    public OnTableActionsListener mOnTableActionsListener;

    public void setOnTableActionsListener(OnTableActionsListener onTableActionsListener) {
        mOnTableActionsListener = onTableActionsListener;
    }
}
