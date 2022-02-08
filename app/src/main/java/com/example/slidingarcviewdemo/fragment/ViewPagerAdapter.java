package com.example.slidingarcviewdemo.fragment;



import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.slidingarcviewdemo.domain.BoxBean;
import com.example.slidingarcviewdemo.domain.Products;

import java.util.List;

/**
 * @author 小訾
 * @projectName xunmi-android
 * @packageName cn.rongcloud.im.ui.blindbox.adapter
 * @description: viewpageAdapter
 * @date :2021/11/17 16:55
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<BoxBean> titleLists;

    private Fragment fragment;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<BoxBean> titleLists) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleLists = titleLists;
    }

    public Fragment getCurrentFragment() {
        return fragment;
    }

    @Override
    public Fragment getItem(int index) {
        return fragmentList.get(index);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        fragment = (Fragment) object;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titleLists != null && titleLists.size() > position) {
            return titleLists.get(position).getName();
        }
        return super.getPageTitle(position);
    }

    // 动态设置我们标题的方法
    public void setPageTitle(int position, BoxBean title) {
        if (position >= 0 && position < titleLists.size()) {
            titleLists.set(position, title);
            notifyDataSetChanged();
        }
    }

}
