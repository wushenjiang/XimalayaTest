package com.liuge.ximalaya.interfaces;

import com.liuge.ximalaya.base.IBasePresenter;

/**
 * FileName: IRecommendPresenter
 * Author: LiuGe
 * Date: 2020/7/23 23:23
 * Description: 推荐页面逻辑抽取
 */
public interface IRecommendPresenter extends IBasePresenter<IRecommendViewCallback> {

    /**
     * 获取推荐内容
     */
    void getRecommendList();



}
