package com.liuge.ximalaya.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.liuge.ximalaya.DetailActivity;
import com.liuge.ximalaya.R;
import com.liuge.ximalaya.adapters.AlbumListAdapter;
import com.liuge.ximalaya.base.BaseApplication;
import com.liuge.ximalaya.base.BaseFragment;
import com.liuge.ximalaya.interfaces.ISubscriptionCallback;
import com.liuge.ximalaya.presenters.AlbumDetailPresenter;
import com.liuge.ximalaya.presenters.SubscriptionPresenter;
import com.liuge.ximalaya.utils.Constants;
import com.liuge.ximalaya.utils.ToastUtil;
import com.liuge.ximalaya.views.ConfirmDialog;
import com.liuge.ximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * FileName: SubscriptionFragmentg
 * Author: LiuGe
 * Date: 2020/7/23 21:18
 * Description: 订阅的fragment
 */
public class SubscriptionFragment extends BaseFragment implements ISubscriptionCallback, AlbumListAdapter.OnAlbumItemClickListener, AlbumListAdapter.onAlbumItemLongClickListener, ConfirmDialog.onDialogActionClickListener {

    private SubscriptionPresenter mSubscriptionPresenter;
    private RecyclerView mSubListView;
    private AlbumListAdapter mAlbumListAdapter;
    private Album mCurrentClickAlbum = null;
    private UILoader mUiLoader;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        FrameLayout rootView = (FrameLayout) layoutInflater.inflate(R.layout.fragment_subscription, container, false);
        if (mUiLoader == null) {
            mUiLoader = new UILoader(container.getContext()) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView();
                }

                @Override
                protected View getEmptyView() {
                    // 创建一个新的View
                    View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view, this, false);
                    TextView tipsView = emptyView.findViewById(R.id.empty_view_tips_tv);
                    tipsView.setText("没有订阅呢,快去订阅吧~");
                    return emptyView;
                }
            };
            rootView.addView(mUiLoader);
        }else{
            // 如果已有view,先清空再添加
            if (mUiLoader.getParent() instanceof ViewGroup) {
                ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
                rootView.addView(mUiLoader);
            }
        }

        return rootView;
    }

    private View createSuccessView() {
        View itemView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.item_subscription, null);
        TwinklingRefreshLayout refreshLayout = itemView.findViewById(R.id.over_scroll_view);
        refreshLayout.setPureScrollModeOn();
        mSubListView = itemView.findViewById(R.id.sub_list);
        mSubListView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        mSubListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });
        //
        mAlbumListAdapter = new AlbumListAdapter();
        mAlbumListAdapter.setAlbumItemClickListener(this);
        mAlbumListAdapter.setOnAlbumItemLongClickListener(this);
        mSubListView.setAdapter(mAlbumListAdapter);
        mSubscriptionPresenter = SubscriptionPresenter.getInstance();
        mSubscriptionPresenter.registerViewCallback(this);
        mSubscriptionPresenter.getSubscriptionList();
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        }
        return itemView;
    }

    @Override
    public void onAddResult(boolean isSuccess) {

    }

    @Override
    public void onDeleteResult(boolean isDeleted) {
        // 给出取消订阅的提示
        ToastUtil.showShort(BaseApplication.getAppContext(), getString(isDeleted ? R.string.cancel_sub_success : R.string.cancel_sub_failed));
    }

    @Override
    public void onSubscriptionsLoaded(List<Album> albums) {
        if (albums.size() == 0) {
            mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
        }else{
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }
        // 更新UI
        if (mAlbumListAdapter != null) {
            mAlbumListAdapter.setData(albums);
        }
    }

    @Override
    public void onSubFull() {
        ToastUtil.showShort(getActivity(), "订阅不得超过" + Constants.MAX_SUB_COUNT + "条");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消接口的注册
        if (mSubscriptionPresenter != null) {
            mSubscriptionPresenter.unRegisterViewCallback(this);
        }
        if (mAlbumListAdapter != null) {
            mAlbumListAdapter.setAlbumItemClickListener(null);
        }
    }

    @Override
    public void onItemClick(int position, Album album) {
        // 把每张专辑的数据设置给presenter
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        // item被点击了,跳转到详情界面
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Album album) {
        this.mCurrentClickAlbum = album;
        // 这是订阅的item被长按了
//        ToastUtil.showShort(BaseApplication.getAppContext(),"该item被长按了");
        ConfirmDialog confirmDialog = new ConfirmDialog(getActivity());
        confirmDialog.setonDialogActionClickListener(this);
        confirmDialog.show();
    }

    @Override
    public void onCancelSubClick() {
        // 取消订阅
        if (mCurrentClickAlbum != null && mSubscriptionPresenter != null) {
            mSubscriptionPresenter.deleteSubscription(mCurrentClickAlbum);
        }
    }

    @Override
    public void onGiveUpClick() {
        // 放弃取消订阅,关闭dialog
    }
}
