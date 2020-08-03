package com.liuge.ximalaya.base;

/**
 * FileName: IBasePresenter
 * Author: LiuGe
 * Date: 2020/7/25 20:18
 * Description: 基础presenter
 */
public interface IBasePresenter<T> {
    /**
     * 注册UI的回调接口
     * @param t
     */
    void registerViewCallback(T t);

    /**
     * 取消注册
     * @param t
     */
    void unRegisterViewCallback(T t);
}
