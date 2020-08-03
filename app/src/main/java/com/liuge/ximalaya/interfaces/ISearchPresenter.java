package com.liuge.ximalaya.interfaces;

import com.liuge.ximalaya.base.IBasePresenter;

/**
 * FileName: ISearchPresenter
 * Author: LiuGe
 * Date: 2020/8/1 19:59
 * Description: 搜索的接口
 */
public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {
    /**
     * 进行搜索
     * @param keyword
     */
    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void reSearch();

    /**
     * 加载更多的搜索结果
     */
    void loaderMore();

    /**
     * 获取热词
     */
    void getHotWords();

    /**
     * 获取推荐的关键字(联想关键字)
     * @param keyword
     */
    void getRecommendWord(String keyword);

}
