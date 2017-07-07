package com.wfl.explorer.viewer.sqlite;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wfl.explorer.R;
import com.wfl.explorer.framework.common.utils.DisplayUtils;

import java.util.List;

/**
 * Created by sn on 2017/6/28.
 */

public class SQLiteTableView extends HorizontalScrollView {
    private static int MAX_COL_WIDTH;

    private int offset = 0;
    List<String> mColumnNames;
    List<List<String>> mDatas;
    
    private ListView mListView;
    private TableAdapter mTableAdapter;

    public SQLiteTableView(Context context) {
        this(context, null);
    }

    public SQLiteTableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SQLiteTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SQLiteTableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setColumnNames(List<String> columnNames) {
        mColumnNames = columnNames;
        if (mTableAdapter != null) {
            mTableAdapter.setColumnNames(columnNames);
        }
    }

    public void setDatas(List<List<String>> datas) {
        mDatas = datas;
        if (mTableAdapter != null) {
            mTableAdapter.setDatas(datas);
            TableRowView.reMeasure(getContext(), mColumnNames, mDatas);
            mTableAdapter.notifyDataSetChanged();
        }
    }

    private void init() {
        MAX_COL_WIDTH = DisplayUtils.getScrrenWidth(getContext());
        initListView();
    }
    
    private void initListView() {
        mListView = new TableListView(getContext());
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setMinimumWidth(DisplayUtils.getScrrenWidth(getContext()));
        addView(mListView, new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTableAdapter = new TableAdapter();
        mListView.setAdapter(mTableAdapter);
    }
    

    static class TableAdapter extends BaseAdapter {
        List<String> mColumnNames;
        List<List<String>> mDatas;

        public void setColumnNames(List<String> columnNames) {
            mColumnNames = columnNames;
        }

        public void setDatas(List<List<String>> datas) {
            mDatas = datas;
        }
        
        @Override
        public int getCount() {
            return mDatas == null ? 1 : mDatas.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TableRowView itemView = (TableRowView) convertView;
            if (convertView == null) {
                itemView = new TableRowView(parent.getContext());
            }
            if (position == 0) {
                itemView.setRowData(mColumnNames);
            } else if (position > 0){
                itemView.setRowData(mDatas.get(position - 1));
            }
            return itemView;
        }
        
    }

    static class TableRowView extends LinearLayout {
        static int sWidth = 0;
        static int sHeight = 0;
        static SparseIntArray sWidthSA;

        static TextView sTestTv;
        static int wMeaspec = MeasureSpec.makeMeasureSpec(MAX_COL_WIDTH, MeasureSpec.AT_MOST);
        static int hMeaspec = MeasureSpec.makeMeasureSpec(MAX_COL_WIDTH, MeasureSpec.AT_MOST);

        static int paddingHoritation, paddingVertical;

        private SparseArray<TextView> mTextViewSparseArray;

        public static void reMeasure(Context context, List<String> mColumnNames, List<List<String>> mDatas) {
            sWidth = sHeight = 0;
            paddingVertical = DisplayUtils.dp2px(context, 5);
            paddingHoritation = DisplayUtils.dp2px(context, 6);
            sWidth = measureWidth(context, mColumnNames, mDatas, 10000);
        }

        public static int measureWidth(Context context, List<String> mColumnNames, List<List<String>> mDatas, int max) {
            if (mColumnNames == null || mColumnNames.size() == 0) {
                return max;
            }
            int width = 0;
            sWidthSA = new SparseIntArray();
            for (int i = 0; i < mColumnNames.size(); i++) {
                int maxWidth = measureMaxWidthOfColumn(context, mColumnNames, mDatas, i);
                sWidthSA.put(i, maxWidth);
                width += maxWidth;
            }
            if (width < max) {
                return max;
            } else {
                return width;
            }
        }

//        private static int measureHeight(int max) {
//            if (mColumnNames == null || mColumnNames.size() == 0) {
//                return max;
//            }
//            if (mDatas == null || mDatas.size() == 0) {
//                return mItemHeight + STROKE_WIDTH;
//            }
//            int height = mDatas.size() * mItemHeight + STROKE_WIDTH;
//            if (height < max) {
//                return max;
//            } else {
//                return height;
//            }
//        }

        private static int measureMaxWidthOfColumn(Context context, List<String> mColumnNames, List<List<String>> mDatas, int column) {
            int max = 0;
            if (mColumnNames != null && mColumnNames.size() > column) {
                String nameText = mColumnNames.get(column);
                max = measureItemWidth(context, nameText);
            }
            if (mDatas == null) {
                return max;
            }
            for (int i = 0; i < mDatas.size(); i++) {
                List<String> rowData = mDatas.get(i);
                if (rowData != null && rowData.size() > column) {
                    String value = rowData.get(column);
                    int len = measureItemWidth(context, value);
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

        private static int measureItemWidth(Context context, String text) {
            if (TextUtils.isEmpty(text)) {
                return 0;
            }
            if (sTestTv == null) {
                sTestTv = generateItemTextView(context);
            }
            sTestTv.setText(text);
            sTestTv.measure(wMeaspec, hMeaspec);
            return sTestTv.getMeasuredWidth();
        }




        public TableRowView(Context context) {
            super(context);
            setOrientation(HORIZONTAL);
//            sWidth = sHeight = 0;
//            sWidthSA = null;
            setBackgroundResource(R.drawable.bg_bottom_divider);

        }

        public void setRowData(List<String> rowData) {
            if (rowData == null || rowData.size() == 0) {
                return;
            }
            for (int i = 0; i < rowData.size(); i++) {
                String text = rowData.get(i);
                TextView textView = null;
                if (getChildCount() > i) {
                    textView = (TextView) getChildAt(i);
                } else {
                    textView = generateItemTextView(getContext());
                    LinearLayout.LayoutParams lp = (LayoutParams) textView.getLayoutParams();
                    if (sWidthSA != null && sWidthSA.size() > i) {
                        lp.width = sWidthSA.get(i);
//                        lp.height = sHeight;
                    }
                    addView(textView, -1, lp);
                }
                textView.setText(text);
            }
        }

        public static TextView generateItemTextView(Context context) {
            TextView textView = new TextView(context);
            textView.setBackgroundResource(R.drawable.bg_right_divider);
            textView.setPadding(paddingHoritation, paddingVertical, paddingHoritation, paddingVertical);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            return textView;
        }
    }

    static class TableListView extends ListView {

        public TableListView(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int screenWidth = DisplayUtils.getScrrenWidth(getContext());
            if (getMeasuredWidth() < screenWidth) {
                setMeasuredDimension(screenWidth, getMeasuredHeight());
            }
        }
    }

}
