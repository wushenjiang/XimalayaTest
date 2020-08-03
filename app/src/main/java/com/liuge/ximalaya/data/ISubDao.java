package com.liuge.ximalaya.data;

import com.ximalaya.ting.android.opensdk.model.album.Album;

/**
 * FileName: ISubDao
 * Author: LiuGe
 * Date: 2020/8/2 16:19
 * Description: 订阅的dao接口
 */
public interface ISubDao {

    void setCallback(ISubDaoCallback callback);
    /**
     * 添加专辑订阅
     * @param album
     */
    void addAlbum(Album album);

    /**
     * 删除订阅内容
     * @param album
     */
    void delAlbum(Album album);

    /**
     * 查询全部订阅内容
     */
    void listAlbums();
}
