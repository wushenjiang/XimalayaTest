package com.liuge.ximalaya.data;

import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * FileName: IHistoryDaoCallback
 * Author: LiuGe
 * Date: 2020/8/3 16:16
 * Description: 历史dao回调接口
 */
public interface IHistoryDaoCallback {
    /**
     * 添加历史的结果
     * @param isSuccess
     */
    void onHistoryAdd(boolean isSuccess);

    /**
     * 删除历史的结果
     * @param isSuccess
     */
    void onHistoryDel(boolean isSuccess);

    /**
     * 历史数据加载的结果
     * @param tracks
     */
    void onHistoriesLoaded(List<Track>tracks);

    /**
     * 清除历史的结果
     */
    void onHistoryClean(boolean isSuccess);
}
