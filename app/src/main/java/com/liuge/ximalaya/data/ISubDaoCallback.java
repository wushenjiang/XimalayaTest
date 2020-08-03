package com.liuge.ximalaya.data;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * FileName: ISubDaoCallback
 * Author: LiuGe
 * Date: 2020/8/2 16:42
 * Description: 通知接口
 */
public interface ISubDaoCallback {

    /**
     * 添加的结果回调方法
     * @param isSuccess
     */
    void onAddResult(boolean isSuccess);

    /**
     * 删除结果回调方法
     * @param isSuccess
     */
    void onDelResult(boolean isSuccess);

    /**
     * 加载的结果
     * @param result
     */
    void onSubListLoaded(List<Album> result);
}
