package com.liuge.ximalaya.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuge.ximalaya.R;
import com.liuge.ximalaya.base.BaseApplication;

/**
 * FileName: UILoader
 * Author: LiuGe
 * Date: 2020/7/24 17:32
 * Description: UI加载器 改成this是为了让入口唯一
 */
public abstract class UILoader extends FrameLayout {

    private  OnRetryClickListener mOnRetryClickListener = null;
    private View mLoadingView;
    private View mSuccessView;
    private View mNetworkErrorView;
    private View mEmptyView;

    public enum UIStatus{
        LOADING,SUCCESS,NETWORK_ERROR,EMPTY,NONE
    }
    public UIStatus mCurrentStatus = UIStatus.NONE;

    public UILoader(@NonNull Context context) {
        this(context,null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化界面
        init();
    }

    /**
     * 暴露出公有方法供外部调用更新UI
     * @param status
     */
    public void updateStatus(UIStatus status){
        mCurrentStatus = status;
        // 更新UI一定要在主线程上
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }
    /**
     * 初始化UI
     */
    private void init() {
        // 根据状态判断UI加载界面
        switchUIByCurrentStatus();
    }

    /**
     * 将view添加进去，并根据状态设置是否显示
     */
    private void switchUIByCurrentStatus() {
        // 加载中
        if (mLoadingView == null) {
            mLoadingView = getLoadingView();
            addView(mLoadingView);
        }
        // 根据状态设置是否可见
        mLoadingView.setVisibility(mCurrentStatus == UIStatus.LOADING? VISIBLE:GONE);
        // 成功
        if (mSuccessView == null) {
            // 由于成功页面不唯一，故交给子类实现
            mSuccessView = getSuccessView(this);
            addView(mSuccessView);
        }
        // 根据状态设置是否可见
        mSuccessView.setVisibility(mCurrentStatus == UIStatus.SUCCESS? VISIBLE:GONE);
        // 网络错误页面
        if (mNetworkErrorView == null) {
            mNetworkErrorView = getNetworkErrorView();
            addView(mNetworkErrorView);
        }
        // 根据状态设置是否可见
        mNetworkErrorView.setVisibility(mCurrentStatus == UIStatus.NETWORK_ERROR? VISIBLE:GONE);

        // 数据为空的页面
        if (mEmptyView == null) {
            mEmptyView = getEmptyView();
            addView(mEmptyView);
        }
        // 根据状态设置是否可见
        mEmptyView.setVisibility(mCurrentStatus == UIStatus.EMPTY? VISIBLE:GONE);





    }

    protected View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view,this,false);
    }

    protected View getNetworkErrorView() {
        View networkErrorView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_error_view,this,false);
        networkErrorView.findViewById(R.id.network_error_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新获取数据
                if (mOnRetryClickListener != null) {
                    mOnRetryClickListener.onRetryClick();
                }

            }
        });
        return networkErrorView;
    }

    protected abstract View getSuccessView(ViewGroup container);


    protected View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view,this,false);
    }
    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener){
        this.mOnRetryClickListener = onRetryClickListener;
    }
    public interface OnRetryClickListener{
        void onRetryClick();
    }
}
