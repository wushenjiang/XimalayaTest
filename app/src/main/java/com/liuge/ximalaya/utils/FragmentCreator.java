package com.liuge.ximalaya.utils;

import com.liuge.ximalaya.base.BaseFragment;
import com.liuge.ximalaya.fragments.HistoryFragment;
import com.liuge.ximalaya.fragments.RecommendFragment;
import com.liuge.ximalaya.fragments.SubscriptionFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * FileName: FragmentCreator
 * Author: LiuGe
 * Date: 2020/7/23 21:24
 * Description: fragment创建器
 */
public class FragmentCreator {

    public final static int INDEX_RECOMMEND = 0;
    public final static int INDEX_SUBSCRIPTION = 1;
    public final static int INDEX_HISTORY = 2;
    public final static int PAGE_COUNT = 3;
    private static Map<Integer, BaseFragment> sCache = new HashMap<>();

    public static BaseFragment getFragment(int index) {
        BaseFragment baseFragment = sCache.get(index);
        if (baseFragment != null) {
            return baseFragment;
        }
        switch (index) {
            case INDEX_RECOMMEND:
                baseFragment = new RecommendFragment();
                break;
            case INDEX_SUBSCRIPTION:
                baseFragment = new SubscriptionFragment();
                break;
            case INDEX_HISTORY:
                baseFragment = new HistoryFragment();
                break;
        }
        sCache.put(index, baseFragment);
        return baseFragment;
    }
}
