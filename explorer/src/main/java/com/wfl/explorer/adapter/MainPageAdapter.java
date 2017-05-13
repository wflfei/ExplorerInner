package com.wfl.explorer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wfl.explorer.fragment.DirFragment;
import com.wfl.explorer.model.PageInfo;

import java.util.List;

/**
 * Created by wfl on 16/8/1.
 */
public class MainPageAdapter extends FragmentPagerAdapter {
    private List<PageInfo> mPageInfos;
    
    public MainPageAdapter(FragmentManager fm, List<PageInfo> pageInfos) {
        super(fm);
        this.mPageInfos = pageInfos;
    }
    
    public void setPageInfos(List<PageInfo> pageInfos) {
        this.mPageInfos = pageInfos;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        PageInfo pageInfo = mPageInfos.get(position);
        if (pageInfo.fragment == null) {
            pageInfo.fragment = DirFragment.createInstance(pageInfo.fileTree, pageInfo.mode);
        }
        return pageInfo.fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageInfos.get(position).title;
    }

    @Override
    public int getCount() {
        return mPageInfos == null ? 0 : mPageInfos.size();
    }
}
