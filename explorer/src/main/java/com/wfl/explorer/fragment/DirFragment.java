package com.wfl.explorer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfl.explorer.R;
import com.wfl.explorer.base.BaseActivity;
import com.wfl.explorer.base.BaseFragment;
import com.wfl.explorer.filehelper.CopyManager;
import com.wfl.explorer.filetree.AbsFileTree;
import com.wfl.explorer.filetree.FileTree;

import org.w3c.dom.Text;

/**
 * Created by wfl on 16/7/29.
 */
public class DirFragment extends BaseFragment implements FragmentBackHandler, CopyManager.Destination {
    public static final String KEY_CURRENT = "key_current";
    public static final String KEY_MODE = "key_mode";
    
    private RecyclerView mRecyclerView;
    private Button copyBtn;
    
    private FileTree mFileTree;
    private FileTree rootFileTree;
    
    
    public static DirFragment createInstance(FileTree fileTree, int mode) {
        DirFragment fragment = new DirFragment();
        Bundle args = new Bundle();
        args.putBundle(KEY_CURRENT, fileTree.toBundle());
        args.putInt(KEY_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (!(activity instanceof TabHelper)) {
            throw new RuntimeException("This damn Activity must be instance of TabHelper, you konw");
        }
        Bundle bundle = getArguments().getBundle(KEY_CURRENT);
        String current = bundle.getString("current");
        String name = bundle.getString("name");
        mFileTree = rootFileTree = AbsFileTree.getFileTree(current, name);
        
        CopyManager.getInstance().registerDestination(this);
        
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dir, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CopyManager.getInstance().unregisterDestination(this);
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.dir_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        copyBtn = (Button) getView().findViewById(R.id.dir_copy_btn);
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyManager.getInstance().copy(getActivity(), CopyManager.getInstance().getSource(), mFileTree.getFile().getAbsolutePath());
                CopyManager.getInstance().endCopy(CopyManager.getInstance().getSource());
            }
        });
    }
    
    
    private void gotoNext(FileTree fileTree) {
        mFileTree = fileTree;
        mAdapter.notifyDataSetChanged();
    }
    
    RecyclerView.Adapter mAdapter = new RecyclerView.Adapter<ViewHolder> () {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list_item, null);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.iconImg.setImageResource(mFileTree.getFileLists().get(position).getIconRes());
            String name = mFileTree.getFileLists().get(position).getName();
            if (!isRoot() && position == 0) {
                name = "../" + name;
            }
            holder.nameTv.setText(name);
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mOnClick);
            holder.itemView.setOnLongClickListener(mLongClick);
        }

        @Override
        public int getItemCount() {
            return mFileTree.getFileLists().size();
        }
        
        private View.OnClickListener mOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                
                FileTree fileTree = mFileTree.getFileLists().get(pos);
                if (fileTree.isDirectory()) {
                    gotoNext(fileTree);
                } else {
                    fileTree.next(getActivity(), null);
                }
            }
        };
        
        private View.OnLongClickListener mLongClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int pos = (int) view.getTag();
                FileTree fileTree = mFileTree.getFileLists().get(pos);
                CopyManager.getInstance().startCopy(fileTree.getFile().getAbsolutePath());
                return true;
            }
        };
    };

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconImg;
        public TextView nameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            this.iconImg = (ImageView) itemView.findViewById(R.id.file_icon);
            this.nameTv = (TextView) itemView.findViewById(R.id.file_name);
        }
    }

    @Override
    public void newCopy(String path) {
        copyBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void endCopy(String path) {
        copyBtn.setVisibility(View.GONE);
    }
    
    private boolean isRoot() {
        return mFileTree.getFile().getAbsolutePath().equals(rootFileTree.getFile().getAbsolutePath());
    }

    @Override
    public boolean onBackPressed() {
        boolean isNotRoot = !isRoot();
        boolean copyBack = CopyManager.getInstance().backPress();
        if (!copyBack) {
            if (isNotRoot) {
                gotoNext(mFileTree.getUpFileTree());
            }
        } else {
            CopyManager.getInstance().endCopy(CopyManager.getInstance().getSource());
        }
        
        return isNotRoot || copyBack;
    }
}
