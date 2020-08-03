package com.liuge.ximalaya.presenters;

import com.liuge.ximalaya.data.XimalayaApi;
import com.liuge.ximalaya.interfaces.IRecommendPresenter;
import com.liuge.ximalaya.interfaces.IRecommendViewCallback;
import com.liuge.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: RecommendPresenter
 * Author: LiuGe
 * Date: 2020/7/23 23:27
 * Description: 推荐的页面实现层
 */
public class RecommendPresenter implements IRecommendPresenter {
    private static final String TAG = "RecommendPresenter";

    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();
    private List<Album> mCurrentRecommend = null;
    private List<Album> mRecommendList;

    private RecommendPresenter() {

    }

    private static RecommendPresenter sInstance = null;

    /**
     * 获取单例对象
     *
     * @return
     */
    public static RecommendPresenter getInstance() {
        if (sInstance == null) {
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取当前的推荐专辑列表
     *
     * @return 推荐专辑列表，使用前要判空
     */
    public List<Album> getCurrentRecommend() {
        return mCurrentRecommend;
    }

    /**
     * 获取推荐内容 其实就是猜你喜欢
     */
    @Override
    public void getRecommendList() {
        // 如果内容不为空,那么直接使用当前的内容
        if (mRecommendList != null && mRecommendList.size() > 0) {
            LogUtil.d(TAG, "getRecommendList --> from list");
            handlerRecommendResult(mRecommendList);
            return;
        }
        // 获取推荐内容
        // 封装参数
        updateLoading();
        XimalayaApi ximalayaApi = XimalayaApi.getInstance();
        ximalayaApi.getRecommendList(new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG, "thread name --->" + Thread.currentThread().getName());
                // 数据获取成功
                if (gussLikeAlbumList != null) {
                    mRecommendList = gussLikeAlbumList.getAlbumList();
                    // 数据回来,更新UI
//                        updateRecommendUI(albumList);
                    handlerRecommendResult(mRecommendList);

                }
            }

            @Override
            public void onError(int i, String s) {
                // 获取失败
                LogUtil.d(TAG, "error -->" + i);
                LogUtil.d(TAG, "errorMsg -->" + s);
                handlerError();
            }
        });
    }

    private void handlerError() {
        // 通知UI更新
        if (mCallbacks != null) {
            for (IRecommendViewCallback callback : mCallbacks) {
                callback.onNetworkError();
            }
        }
    }

    private void handlerRecommendResult(List<Album> albumList) {
        // 通知UI更新
        if (albumList != null) {
            // 测试清空一下，让界面显示空
//            albumList.clear();
            if (albumList.size() == 0) {
                for (IRecommendViewCallback callback : mCallbacks) {
                    callback.onEmpty();
                }
            } else {
                for (IRecommendViewCallback callback : mCallbacks) {
                    callback.onRecommendListLoaded(albumList);
                }
                this.mCurrentRecommend = albumList;
            }
        }
    }

    private void updateLoading() {
        for (IRecommendViewCallback callback : mCallbacks) {
            callback.onLoading();
        }
    }


    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }
}
