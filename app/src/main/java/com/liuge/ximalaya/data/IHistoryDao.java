package com.liuge.ximalaya.data;

import com.ximalaya.ting.android.opensdk.model.track.Track;

/**
 * FileName: IHistoryDao
 * Author: LiuGe
 * Date: 2020/8/3 16:13
 * Description: 历史的dao接口
 */
public interface IHistoryDao {

    /**
     * 设置回调接口
     * @param callback
     */
    void setCallback(IHistoryDaoCallback callback);

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
     * 清除历史内容
     */
    void clearHistory();

    /**
     * 获取历史内容
     */
    void listHistories();
}
