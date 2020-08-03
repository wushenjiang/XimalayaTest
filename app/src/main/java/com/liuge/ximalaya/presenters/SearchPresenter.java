package com.liuge.ximalaya.presenters;

import com.liuge.ximalaya.data.XimalayaApi;
import com.liuge.ximalaya.interfaces.ISearchCallback;
import com.liuge.ximalaya.interfaces.ISearchPresenter;
import com.liuge.ximalaya.utils.Constants;
import com.liuge.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: SearchPresenter
 * Author: LiuGe
 * Date: 2020/8/1 20:09
 * Description: 搜索的presenter
 */
public class SearchPresenter implements ISearchPresenter {

    private List<Album> mSearchResult = new ArrayList<>();

    private static final String TAG = "SearchPresenter";
    // 当前的搜索关键字
    private String mCurrentKeyWord = null;
    private XimalayaApi mXimalayaApi;
    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;

    private SearchPresenter() {
        mXimalayaApi = XimalayaApi.getInstance();
    }

    private static SearchPresenter sSearchPresenter = null;

    public static SearchPresenter getInstance() {
        if (sSearchPresenter == null) {
            synchronized (SearchPresenter.class) {
                if (sSearchPresenter == null) {
                    sSearchPresenter = new SearchPresenter();
                }
            }
        }
        return sSearchPresenter;
    }

    private List<ISearchCallback> mCallbacks = new ArrayList<>();

    @Override
    public void doSearch(String keyword) {
        mCurrentPage = DEFAULT_PAGE;
        mSearchResult.clear();
        // 保存关键字，用于重新搜索
        this.mCurrentKeyWord = keyword;
        search(keyword);
    }

    private void search(String keyword) {
        mXimalayaApi.searchByKeyword(keyword, mCurrentPage, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {
                List<Album> albums = searchAlbumList.getAlbums();
                mSearchResult.addAll(albums);
                if (albums != null) {
                    if (mIsLoadMore) {
                        for (ISearchCallback callback : mCallbacks) {
                            callback.onLoadMoreResult(mSearchResult, albums.size()!=0);
                        }
                        mIsLoadMore = false;

                    } else {
                        for (ISearchCallback callback : mCallbacks) {
                            callback.onSearchResultLoaded(mSearchResult);
                        }
                    }

                } else {
                    LogUtil.d(TAG, "albums is null");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "search errorCode -->" + errorCode);
                LogUtil.d(TAG, "search errorMsg -->" + errorMsg);
                for (ISearchCallback callback : mCallbacks) {
                    if (mIsLoadMore) {
                        callback.onLoadMoreResult(mSearchResult, false);
                        mCurrentPage--;
                        mIsLoadMore = false;
                    } else {
                        callback.onError(errorCode, errorMsg);
                    }
                }
            }
        });
    }

    @Override
    public void reSearch() {
        search(mCurrentKeyWord);
    }

    private boolean mIsLoadMore = false;

    @Override
    public void loaderMore() {
        // 判断有没有必要进行加载更多
        if (mSearchResult.size() < Constants.COUNT_DEFAULT) {
            for (ISearchCallback callback : mCallbacks) {
                callback.onLoadMoreResult(mSearchResult, false);
            }
        } else {
            mIsLoadMore = true;
            mCurrentPage++;
            search(mCurrentKeyWord);
        }


    }


    @Override
    public void getHotWords() {
        // todo:做一个热词缓存
        mXimalayaApi.getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(HotWordList hotWordList) {
                if (hotWordList != null) {
                    List<HotWord> hotWords = hotWordList.getHotWordList();
                    for (ISearchCallback callback : mCallbacks) {
                        callback.onHotWordLoaded(hotWords);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getHotWords errorCode -->" + errorCode);
                LogUtil.d(TAG, "getHotWords errorMsg -->" + errorMsg);

            }
        });
    }

    @Override
    public void getRecommendWord(String keyword) {
        mXimalayaApi.getSuggestWord(keyword, new IDataCallBack<SuggestWords>() {
            @Override
            public void onSuccess(SuggestWords suggestWords) {
                if (suggestWords != null) {
                    List<QueryResult> keyWordList = suggestWords.getKeyWordList();
                    LogUtil.d(TAG, "keywordList -->" + keyWordList.size());
                    for (ISearchCallback callback : mCallbacks) {
                        callback.onRecommendWordLoaded(keyWordList);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getRecommendWord  errorCode -->" + errorCode);
                LogUtil.d(TAG, "getRecommendWord errorMsg -->" + errorMsg);
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchCallback iSearchCallback) {
        if (!mCallbacks.contains(iSearchCallback)) {
            mCallbacks.add(iSearchCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(ISearchCallback iSearchCallback) {
        mCallbacks.remove(iSearchCallback);
    }
}
