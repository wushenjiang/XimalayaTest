package com.liuge.ximalaya.interfaces;

import com.liuge.ximalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

/**
 * FileName: ISubscriptionPresenter
 * Author: LiuGe
 * Date: 2020/8/2 15:49
 * Description: 订阅的接口
 * 订阅一般有上限，如不能超过100
 */
public interface ISubscriptionPresenter extends IBasePresenter<ISubscriptionCallback> {
    /**
     * 添加订阅
     * @param album
     */
    void addSubscription(Album album);

    /**
     * 删除订阅
     * @param album
     */
    void deleteSubscription(Album album);

    /**
     * 获取订阅列表
     */
    void getSubscriptionList();

    /**
     * 判断专辑是否已订阅
     * @param album
     */
    boolean isSub(Album album);
}
