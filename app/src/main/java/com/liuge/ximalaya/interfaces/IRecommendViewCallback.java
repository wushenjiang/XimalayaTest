package com.liuge.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * FileName: IRecommendViewCallback
 * Author: LiuGe
 * Date: 2020/7/23 23:25
 * Description: 推荐的接口回调
 */
public interface IRecommendViewCallback {
    /**
     * 获取推荐内容的结果
     * @param result
     */
    void onRecommendListLoaded(List<Album> result);

    /**
     * 网络错误
     */
    void onNetworkError();

    /**
     * 数据为空
     */
    void onEmpty();

    /**
     * 正在加载
     */
    void onLoading();
}
