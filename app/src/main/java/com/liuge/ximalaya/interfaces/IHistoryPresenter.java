package com.liuge.ximalaya.interfaces;

import com.liuge.ximalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;

/**
 * FileName: IHistoryPresenter
 * Author: LiuGe
 * Date: 2020/8/3 16:20
 * Description: 历史的presenter接口
 */
public interface IHistoryPresenter extends IBasePresenter<IHistoryCallback> {
    /**
     * 获取历史内容
     */
    void listHistories();

    /**
     * 添加历史
     * @param track
     */
    void addHistory(Track track);

    /**
     * 删除历史
     * @param track
     */
    void delHistory(Track track);

    /**
     * 清除历史
     */
    void cleanHistories();
}
