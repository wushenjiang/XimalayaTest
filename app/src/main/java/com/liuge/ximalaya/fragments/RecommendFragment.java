package com.liuge.ximalaya.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.liuge.ximalaya.DetailActivity;
import com.liuge.ximalaya.R;
import com.liuge.ximalaya.adapters.AlbumListAdapter;
import com.liuge.ximalaya.base.BaseFragment;
import com.liuge.ximalaya.interfaces.IRecommendViewCallback;
import com.liuge.ximalaya.presenters.AlbumDetailPresenter;
import com.liuge.ximalaya.presenters.RecommendPresenter;
import com.liuge.ximalaya.utils.LogUtil;
import com.liuge.ximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * FileName: RecommendFragment
 * Author: LiuGe
 * Date: 2020/7/23 21:16
 * Description: 推荐的Fragment
 */
public class RecommendFragment extends BaseFragment implements IRecommendViewCallback, UILoader.OnRetryClickListener, AlbumListAdapter.OnAlbumItemClickListener {
    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mRecommendRv;
    private AlbumListAdapter mAlbumListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUiLoader;
    private TwinklingRefreshLayout mTwinklingRefreshLayout;

    @Override
    protected View onSubViewLoaded(final LayoutInflater layoutInflater, ViewGroup container) {
        mUiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return createSuccessView(layoutInflater,container);
            }
        };
        // 获取到逻辑层的对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        // 先要设置通知结课的注册
        mRecommendPresenter.registerViewCallback(this);
        // 获取推荐列表
        mRecommendPresenter.getRecommendList();
        // 在添加view前先将已有view清空,防止出错
        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }
        mUiLoader.setOnRetryClickListener(this);
        // 返回view,给界面显示
        return mUiLoader;
    }

    private View createSuccessView(LayoutInflater layoutInflater, ViewGroup container) {
        // view加载完成
        mRootView = layoutInflater.inflate(R.layout.fragment_recommend, container,false);
        // RecyclerView的使用
        // 1.找到控件
        mRecommendRv = mRootView.findViewById(R.id.recommend_list);
        mTwinklingRefreshLayout = mRootView.findViewById(R.id.over_scroll_view);
        // 开启回弹效果
        mTwinklingRefreshLayout.setPureScrollModeOn();
        // 2.设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendRv.setLayoutManager(linearLayoutManager);
        mRecommendRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(),5);
                outRect.bottom = UIUtil.dip2px(view.getContext(),5);
                outRect.left = UIUtil.dip2px(view.getContext(),5);
                outRect.right = UIUtil.dip2px(view.getContext(),5);
            }
        });
        // 3.设置适配器
        mAlbumListAdapter = new AlbumListAdapter();
        mRecommendRv.setAdapter(mAlbumListAdapter);
        mAlbumListAdapter.setAlbumItemClickListener(this);
        return mRootView;
    }

    @Override
    public void onRecommendListLoaded(List<Album> result) {
        LogUtil.d(TAG,"onRecommendListLoaded");
        // 当我们获取到推荐内容的时候,这个方法就会被调用(成功了)
        // 数据回来以后,更新UI
        mAlbumListAdapter.setData(result);
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);

    }

    @Override
    public void onNetworkError() {
        LogUtil.d(TAG,"onNetworkError");
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onEmpty() {
        LogUtil.d(TAG,"onEmpty");
        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        LogUtil.d(TAG,"onLoading");
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消接口的注册
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void onRetryClick() {
        // 表示网络不佳的时候,用户点击了重试
        // 重新获取数据即可
        if (mRecommendPresenter != null) {
            mRecommendPresenter.getRecommendList();
        }
    }

    @Override
    public void onItemClick(int position, Album album) {
        // 把每张专辑的数据设置给presenter
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        // item被点击了,跳转到详情界面
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);
    }
}
