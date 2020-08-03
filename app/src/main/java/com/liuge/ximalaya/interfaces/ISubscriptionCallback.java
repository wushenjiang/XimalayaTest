package com.liuge.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * FileName: ISubscriptionCallback
 * Author: LiuGe
 * Date: 2020/8/2 15:49
 * Description: 订阅接口回调
 */
public interface ISubscriptionCallback {
    /**
     * 添加的回调
     * @param isSuccess
     */
    void onAddResult(boolean isSuccess);

    /**

     * 删除订阅的回调
     * @param isDeleted
     */
    void onDeleteResult(boolean isDeleted);

    /**
     * 订阅专辑加载的结果回调
     * @param albums
     */
    void onSubscriptionsLoaded(List<Album> albums);

    /**
     * 订阅数量太多,弹出提示
     */
    void onSubFull();
}
