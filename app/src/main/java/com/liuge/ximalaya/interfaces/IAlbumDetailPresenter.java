package com.liuge.ximalaya.interfaces;

import com.liuge.ximalaya.base.IBasePresenter;

/**
 * FileName: IAlbumDetailPresenter
 * Author: LiuGe
 * Date: 2020/7/24 22:11
 * Description: 专辑详情接口
 */
public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailViewCallback> {

    /**
     * 下拉刷新加载更多内容
     */
    void pull2RefreshMore();
    /**
     *  上拉加载更多
     */
    void loadMore();

    /**
     * 获取专辑详情
     * @param albumId
     * @param page
     */
    void getAlbumDetail(int albumId,int page);

}
