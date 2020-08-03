package com.liuge.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * FileName: IAlbumDetailViewCallback
 * Author: LiuGe
 * Date: 2020/7/24 22:17
 * Description: 专辑详情回调
 */
public interface IAlbumDetailViewCallback {
    /**
     * 专辑详情内容加载出来了.
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);

    /**
     * 网络错误
     */
    void onNetworkError(int errorCode,String errorMsg);
    /**
     * 把album传UI使用
     * @param album
     */
    void onAlbumLoaded(Album album);

    /**
     * 加载更多的结果
     * @param size size>0表示加载成功，否则加载失败
     */
    void onLoaderMoreFinished(int size);

    /**
     * 下拉加载更多的结果
     * @param size size>0表示加载成功，否则加载失败
     */
    void onRefreshFinished(int size);
}
