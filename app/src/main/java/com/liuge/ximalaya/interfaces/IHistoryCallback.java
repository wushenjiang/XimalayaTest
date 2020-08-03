package com.liuge.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * FileName: IHistoryCallback
 * Author: LiuGe
 * Date: 2020/8/3 16:20
 * Description: 历史的presenter接口回调
 */
public interface IHistoryCallback {
    /**
     * 历史内容加载完成
     * @param tracks
     */
    void onHistoriesLoaded(List<Track> tracks);


}
