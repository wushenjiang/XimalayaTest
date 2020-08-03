package com.liuge.ximalaya.interfaces;

import com.liuge.ximalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

/**
 * FileName: IPlayerPresenter
 * Author: LiuGe
 * Date: 2020/7/25 20:18
 * Description: 播放器的presenter
 */
public interface IPlayerPresenter extends IBasePresenter<IPlayerCallback> {

    /**
     * 播放
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 上一首
     */
    void playPre();

    /**
     * 播放下一首
     */
    void playNext();

    /**
     * 切换播放模式
     * @param mode
     */
    void switchPlayMode(XmPlayListControl.PlayMode mode);

    /**
     * 获取播放列表
     */
    void getPlayList();

    /**
     * 根据节目的位置进行播放
     * @param index 节目在列表中的位置
     */
    void playByIndex(int index);

    /**
     * 切换播放进度
     * @param progress
     */
    void seekTo(int progress);

    /**
     * 判断播放器是否在播放
     * @return
     */
    boolean isPlaying();

    /**
     * 播放器列表内容反转
     */
    void reversePlayList();

    /**
     * 播放指定id的节目
     * @param id
     */
    void playByAlbumId(long id);

}
