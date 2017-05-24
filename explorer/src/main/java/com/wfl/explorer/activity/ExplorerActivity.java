package com.wfl.explorer.activity;

import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.wfl.explorer.R;
import com.wfl.explorer.adapter.MainPageAdapter;
import com.wfl.explorer.base.BaseActivity;
import com.wfl.explorer.filetree.FileTree;
import com.wfl.explorer.fragment.FragmentBackHandler;
import com.wfl.explorer.fragment.TabHelper;
import com.wfl.explorer.fragment.TabManager;
import com.wfl.explorer.model.PageInfo;

import java.util.List;

public class ExplorerActivity extends BaseActivity implements TabHelper {
    private ViewPager mViewPager;
    private MainPageAdapter mainPageAdapter;
    private TabLayout mTabLayout;
    private List<PageInfo> mPageInfos;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        initPages();
        initViews();
    }
    
    private void initViews() {
        mTabLayout = findView(R.id.main_tablayout);
        
        mViewPager = findView(R.id.main_viewpager);
        mainPageAdapter = new MainPageAdapter(getSupportFragmentManager(), mPageInfos);
        mViewPager.setAdapter(mainPageAdapter);

        initTabLayout();
        
    }
    
    private void initTabLayout() {
        mTabLayout.removeAllTabs();
        for (int i=0; i<mPageInfos.size(); i++) {
            PageInfo pageInfo = mPageInfos.get(i);
            mTabLayout.addTab(mTabLayout.newTab().setText(pageInfo.title).setTag(i));
        }
        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                mViewPager.setCurrentItem((Integer) tab.getTag());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }
    
    private void initPages() {
        mPageInfos = TabManager.getInitPages(this, new int[] {TabManager.TAB_MY, TabManager.TAB_EXTERNAL});
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        
    }

    @Override
    public void newTab(FileTree fileTree) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.title = fileTree.getName();
        pageInfo.fileTree = fileTree;
        mPageInfos.add(pageInfo);
        mainPageAdapter.setPageInfos(mPageInfos);
    }

    @Override
    public void deleteTab(FileTree fileTree) {
        
    }

    @Override
    public void onBackPressed() {
        int item = mViewPager.getCurrentItem();
        Fragment fragment = mPageInfos.get(item).fragment;
        boolean processed = false;
        if (fragment instanceof FragmentBackHandler) {
            processed = ((FragmentBackHandler) fragment).onBackPressed();
        }
        if (!processed) {
            super.onBackPressed();
        }
    }
}