package com.wfl.explorer.viewer.sqlite;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wfl.explorer.R;
import com.wfl.explorer.filehelper.sqlite.TableInfo;

import java.util.ArrayList;
import java.util.List;


public class TableEditDialogFragment extends AppCompatDialogFragment {
    private static final String ARG_INDEX = "index";
    private static final String ARG_ROW_DATA = "row_data";
    private static final String ARG_TABLE_INFO = "table_info";

    private TableInfo mTableInfo;
    private int mIndex;
    private List<String> mRowData;

    private List<EditText> mEditViews = new ArrayList<>();

    private OnEditResultListener mListener;

    public TableEditDialogFragment() {
        // Required empty public constructor
    }


    public static TableEditDialogFragment newInstance(TableInfo tableInfo, int index, ArrayList<String> rowData) {
        TableEditDialogFragment fragment = new TableEditDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TABLE_INFO, tableInfo);
        args.putInt(ARG_INDEX, index);
        args.putStringArrayList(ARG_ROW_DATA, rowData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTableInfo = getArguments().getParcelable(ARG_TABLE_INFO);
            mIndex = getArguments().getInt(ARG_INDEX);
            mRowData = getArguments().getStringArrayList(ARG_ROW_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = inflater.inflate(R.layout.fragment_table_edit_dialog, container, false);
        LinearLayout content = (LinearLayout) v.findViewById(R.id.sqlite_edit_content);
        Button cofirmBtn = (Button) v.findViewById(R.id.sqlite_edit_confirm_btn);
        cofirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfimClick();
            }
        });
        Button cancelBtn = (Button) v.findViewById(R.id.sqlite_edit_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick();
            }
        });
        createView(content);
        return v;
    }

    public void onConfimClick() {
        if (mListener != null) {
            mListener.onEditConfirum(mIndex, mRowData);
        }
        dismiss();
    }
    public void onCancelClick() {
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditResultListener) {
            mListener = (OnEditResultListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void createView(LinearLayout content) {
        if (mTableInfo == null || mRowData == null || mRowData.size() == 0) {
            return;
        }
        mEditViews.clear();
        for (int i = 0; i < mRowData.size(); i++) {
            TableInfo.Column col = mTableInfo.getColumnByCid(i);
            boolean editable = (col != null) && !col.pk && !"Blob Data".equals(mRowData.get(i))&& !"Null".equals(mRowData.get(i));
            final TextInputLayout textInputLayout = createEditItem(mRowData.get(i), editable);
            textInputLayout.setTag(i);
            textInputLayout.setHint(col.name);
            textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int cid = (int) textInputLayout.getTag();
                    mRowData.remove(cid);
                    mRowData.add(cid, s.toString().trim());
                }
            });
            textInputLayout.getEditText().setInputType(getInputTypeByColumn(col));
//            mEditViews.add(textView);
            content.addView(textInputLayout);
        }
    }

    private TextInputLayout createEditItem(String text, boolean editable) {
        TextInputEditText textView = new TextInputEditText(getContext());
        textView.setText(text);
        textView.setEnabled(editable);
        TextInputLayout textInputLayout = new TextInputLayout(getContext());
        textInputLayout.addView(textView);
        
        return textInputLayout;
    }

    private int getInputTypeByColumn(TableInfo.Column column) {
        if (column == null) {
            return EditorInfo.TYPE_NULL;
        }
        if (column.typeInt == Cursor.FIELD_TYPE_INTEGER) {
            return EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_SIGNED;
        } else if (column.typeInt == Cursor.FIELD_TYPE_STRING) {
            return EditorInfo.TYPE_CLASS_TEXT;
        } else if (column.typeInt == Cursor.FIELD_TYPE_FLOAT) {
            return  EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL;
        }
        return EditorInfo.TYPE_NULL;
    }



    public interface OnEditResultListener {
        void onEditConfirum(int index, List<String> rowData);
    }
}
