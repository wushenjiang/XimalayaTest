package com.liuge.ximalaya.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.liuge.ximalaya.utils.FragmentCreator;

/**
 * FileName: MainContentAdapter
 * Author: LiuGe
 * Date: 2020/7/23 21:04
 * Description: 主界面的viewpager适配器
 */
public class MainContentAdapter extends FragmentPagerAdapter {


    public MainContentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FragmentCreator.getFragment(position);
    }

    @Override
    public int getCount() {
        return FragmentCreator.PAGE_COUNT;
    }
}
