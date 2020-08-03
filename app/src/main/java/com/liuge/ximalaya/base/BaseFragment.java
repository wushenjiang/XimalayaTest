package com.liuge.ximalaya.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * FileName: BaseFragment
 * Author: LiuGe
 * Date: 2020/7/23 21:17
 * Description: 抽象出的Fragment
 */
public abstract class BaseFragment extends Fragment {

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = onSubViewLoaded(inflater,container);
        return mRootView;
    }


    protected abstract View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container);
}
