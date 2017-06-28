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
import android.widget.TextView;

import com.wfl.explorer.R;
import com.wfl.explorer.framework.common.utils.DisplayUtils;

import java.util.List;

/**
 * Created by sn on 2017/6/28.
 */

public class SQLiteTableView extends HorizontalScrollView {
    private static int MAX_COL_WIDTH;

    List<String> mColumnNames;
    List<List<String>> mDatas;

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
    }

    public void setDatas(List<List<String>> datas) {
        mDatas = datas;
    }

    private void init() {
        MAX_COL_WIDTH = DisplayUtils.getScrrenWidth(getContext());

    }


    static class TableAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
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
            return null;
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

        public static void reMeasure(List<String> mColumnNames, List<List<String>> mDatas) {
            sWidth = sHeight = 0;
            sWidth = measureWidth(mColumnNames, mDatas, 10000);
        }

        public static int measureWidth(List<String> mColumnNames, List<List<String>> mDatas, int max) {
            if (mColumnNames == null || mColumnNames.size() == 0) {
                return max;
            }
            int width = 0;
            sWidthSA = new SparseIntArray();
            for (int i = 0; i < mColumnNames.size(); i++) {
                int maxWidth = measureMaxWidthOfColumn(mColumnNames, mDatas, i);
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

        private static int measureMaxWidthOfColumn(List<String> mColumnNames, List<List<String>> mDatas, int column) {
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

        private static int measureItemWidth(String text) {
            if (TextUtils.isEmpty(text)) {
                return 0;
            }
            if (sTestTv == null) {
                sTestTv = generateItemTextView();
            }
            sTestTv.setText(text);
            sTestTv.measure(wMeaspec, hMeaspec);
            return sTestTv.getMeasuredWidth();
        }




        public TableRowView(Context context) {
            super(context);
            paddingVertical = DisplayUtils.dp2px(getContext(), 4);
            paddingHoritation = DisplayUtils.dp2px(getContext(), 2);

        }

        public void setRowData(List<String> rowData) {
        }

        public TextView generateItemTextView() {
            TextView textView = new TextView(getContext());
            textView.setPadding(paddingHoritation, paddingVertical, paddingHoritation, paddingVertical);
            textView.setBackgroundResource(R.drawable.bg_right_divider);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            return textView;
        }
    }


}
