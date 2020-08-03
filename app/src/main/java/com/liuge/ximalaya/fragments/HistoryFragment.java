package com.liuge.ximalaya.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.liuge.ximalaya.PlayerActivity;
import com.liuge.ximalaya.R;
import com.liuge.ximalaya.adapters.TrackListAdapter;
import com.liuge.ximalaya.base.BaseApplication;
import com.liuge.ximalaya.base.BaseFragment;
import com.liuge.ximalaya.interfaces.IHistoryCallback;
import com.liuge.ximalaya.presenters.HistoryPresenter;
import com.liuge.ximalaya.presenters.PlayerPresenter;
import com.liuge.ximalaya.views.ConfirmCheckBoxDialog;
import com.liuge.ximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * FileName: HistoryFragment
 * Author: LiuGe
 * Date: 2020/7/23 21:18
 * Description: 历史的fragment
 */
public class HistoryFragment extends BaseFragment implements IHistoryCallback, TrackListAdapter.ItemClickListener, TrackListAdapter.ItemLongClickListener, ConfirmCheckBoxDialog.onDialogActionClickListener {

    private UILoader mUiLoader;
    private RecyclerView mHistoryList;
    private TrackListAdapter mTrackListAdapter;
    private HistoryPresenter mHistoryPresenter;
    private TwinklingRefreshLayout mRefreshLayout;
    private Track mCurrentClickHistory = null;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        FrameLayout rootView = (FrameLayout) layoutInflater.inflate(R.layout.fragment_history, container, false);
        if (mUiLoader == null) {
            mUiLoader = new UILoader(BaseApplication.getAppContext()) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView();
                }

                @Override
                protected View getEmptyView() {
                    // 创建一个新的View
                    View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view, this, false);
                    TextView tipsView = emptyView.findViewById(R.id.empty_view_tips_tv);
                    tipsView.setText("没有历史记录哦~");
                    return emptyView;
                }
            };
        }else {
            if(mUiLoader.getParent() instanceof ViewGroup) {
                ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
            }
        }
        // 拿到presenter
        mHistoryPresenter = HistoryPresenter.getInstance();
        mHistoryPresenter.registerViewCallback(this);
        mHistoryPresenter.listHistories();
        rootView.addView(mUiLoader);
        return rootView;
    }

    private View createSuccessView() {
        View successView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.item_history, null);
        mRefreshLayout = successView.findViewById(R.id.over_scroll_view);
        mRefreshLayout.setPureScrollModeOn();
        mHistoryList = successView.findViewById(R.id.history_list);
        mHistoryList.setLayoutManager(new LinearLayoutManager(successView.getContext()));
        // 设置item的上下间距
        mHistoryList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 2);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 2);
                outRect.left = UIUtil.dip2px(view.getContext(), 2);
                outRect.right = UIUtil.dip2px(view.getContext(), 2);
            }
        });
        // 设置适配器
        mTrackListAdapter = new TrackListAdapter();
        mTrackListAdapter.setItemClickListener(this);
        mTrackListAdapter.setItemLongClickListener(this);
        mHistoryList.setAdapter(mTrackListAdapter);
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        }

        return successView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mHistoryPresenter != null) {
            mHistoryPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void onHistoriesLoaded(List<Track> tracks) {
        if (tracks.size() == 0) {
            mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
        }else{
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }
        // 更新数据
        if (mTrackListAdapter != null) {
            mTrackListAdapter.setData(tracks);
        }

    }

    @Override
    public void onItemClick(List<Track> detailData, int position) {
        // 设置播放器的数据
        PlayerPresenter playerPresenter = PlayerPresenter.getInstance();
        playerPresenter.setPlayList(detailData, position);
        // 跳转到播放器页面
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Track track) {
        this.mCurrentClickHistory = track;
        // 去删除历史
        //ToastUtil.showShort(getContext(),"历史记录被长按了");
        ConfirmCheckBoxDialog dialog = new ConfirmCheckBoxDialog(getActivity());
        dialog.setonDialogActionClickListener(this);
        dialog.show();
    }

    @Override
    public void onCancelSubClick() {
        // 没事做
    }

    @Override
    public void onConfirmClick(boolean isChecked) {
        // 去删除历史
        if (mCurrentClickHistory != null && mHistoryPresenter != null) {
            if(!isChecked){
                mHistoryPresenter.delHistory(mCurrentClickHistory);
            }else{
                mHistoryPresenter.cleanHistories();
            }
        }
    }
}
