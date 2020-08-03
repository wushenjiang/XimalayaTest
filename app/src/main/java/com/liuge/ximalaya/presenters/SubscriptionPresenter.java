package com.liuge.ximalaya.presenters;

import com.liuge.ximalaya.base.BaseApplication;
import com.liuge.ximalaya.data.ISubDaoCallback;
import com.liuge.ximalaya.data.SubscriptionDao;
import com.liuge.ximalaya.interfaces.ISubscriptionCallback;
import com.liuge.ximalaya.interfaces.ISubscriptionPresenter;
import com.liuge.ximalaya.utils.Constants;
import com.liuge.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * FileName: SubscriptionPresenter
 * Author: LiuGe
 * Date: 2020/8/2 17:25
 * Description: 订阅的presenter
 */
public class SubscriptionPresenter implements ISubscriptionPresenter, ISubDaoCallback {

    private static final String TAG = "SubscriptionPresenter";
    private final SubscriptionDao mSubscriptionDao;

    private List<ISubscriptionCallback> mCallbacks = new ArrayList<>();

    private Map<Long, Album> mData = new HashMap<>();

    private SubscriptionPresenter() {
        mSubscriptionDao = SubscriptionDao.getInstance();
        mSubscriptionDao.setCallback(this);
    }

    private void listSubscriptions() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                // 只调用，不处理结果
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.listAlbums();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private static SubscriptionPresenter sPresenter = null;

    public static SubscriptionPresenter getInstance() {
        if (sPresenter == null) {
            synchronized (SubscriptionPresenter.class) {
                sPresenter = new SubscriptionPresenter();
            }
        }
        return sPresenter;
    }

    @Override
    public void addSubscription(final Album album) {
        // 判断当前的订阅数量,不能超过100条
        if (mData.size() >= Constants.MAX_SUB_COUNT) {
            // 给出提示
            for (ISubscriptionCallback callback : mCallbacks) {
                callback.onSubFull();
            }
            return;
        }
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.addAlbum(album);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void deleteSubscription(final Album album) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.delAlbum(album);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void getSubscriptionList() {
        listSubscriptions();
    }

    @Override
    public boolean isSub(Album album) {
        Album result = mData.get(album.getId());
        // 不为空,表示已订阅
        LogUtil.d(TAG,"mData result -->" + result);
        return result != null;
    }

    @Override
    public void registerViewCallback(ISubscriptionCallback iSubscriptionCallback) {
        if (!mCallbacks.contains(iSubscriptionCallback)) {
            mCallbacks.add(iSubscriptionCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(ISubscriptionCallback iSubscriptionCallback) {
        mCallbacks.remove(iSubscriptionCallback);
    }

    @Override
    public void onAddResult(final boolean isSuccess) {
        // 调用查询方法,更新mData,确保能正确更新UI
        listSubscriptions();
        // 添加结果的回调
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onAddResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onDelResult(final boolean isSuccess) {
        // 调用查询方法,更新mData,确保能正确更新UI
        listSubscriptions();
        // 删除结果的回调
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onDeleteResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onSubListLoaded(final List<Album> result) {
        mData.clear();
        // 加载订阅列表的回调
        for (Album album : result) {
            mData.put(album.getId(), album);
        }
        // 通知UI更新
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onSubscriptionsLoaded(result);
                }
            }
        });
    }
}
