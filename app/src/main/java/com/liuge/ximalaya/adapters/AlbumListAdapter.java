package com.liuge.ximalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liuge.ximalaya.R;
import com.liuge.ximalaya.utils.LogUtil;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: RecommendListAdapter
 * Author: LiuGe
 * Date: 2020/7/23 22:29
 * Description: 推荐的recyclerview的适配器
 */
public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.InnerHolder> {
    private static final String TAG = "RecommendListAdapter";
    private List<Album> mData = new ArrayList<>();
    private OnAlbumItemClickListener mItemClickListener = null;
    private onAlbumItemLongClickListener mLongClickListener = null;


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 这里是载入view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
        // 设置数据
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    int clickPosition = (int) v.getTag();
                    mItemClickListener.onItemClick(clickPosition,mData.get(clickPosition));
                }
                LogUtil.d(TAG,"holder.itemView.click ->" + v.getTag());
            }
        });
        holder.setData(mData.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongClickListener != null) {
                    int clickPosition = (int) v.getTag();
                    mLongClickListener.onItemLongClick(mData.get(clickPosition));
                }
                // true表示该事件已被消费
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        // 返回要显示的个数
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }
    public  void setData(List<Album> albumList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(albumList);
        }
        // 更新UI
        notifyDataSetChanged();
    }


    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            // 找到各个控件,设置数据
            // 专辑的封面
            ImageView albumCoverIv = itemView.findViewById(R.id.album_cover);
            // title
            TextView albumTitleTv = itemView.findViewById(R.id.album_title_tv);
            // 描述
            TextView albumDesTv = itemView.findViewById(R.id.album_description_tv);
            // 播放数量
            TextView albumPlayCountTv = itemView.findViewById(R.id.album_play_count);
            // 专辑内容数量
            TextView albumContentCountTv = itemView.findViewById(R.id.album_content_size);

            albumTitleTv.setText(album.getAlbumTitle());
            albumDesTv.setText(album.getAlbumIntro());
            albumPlayCountTv.setText(album.getPlayCount() + "");
            albumContentCountTv.setText(album.getIncludeTrackCount() + "");
            // 对图片结果判空，防止网速太慢没有加载出图片时程序崩溃
            if (!StringUtils.isEmpty(album.getCoverUrlLarge())) {
                Picasso.get().load(album.getCoverUrlLarge()).into(albumCoverIv);
            }else{
                // 如果为空，设置默认值
                albumCoverIv.setImageResource(R.mipmap.ximalay_logo);
            }


        }
    }
    public void setAlbumItemClickListener(OnAlbumItemClickListener listener){
        this.mItemClickListener = listener;
    }
    public interface OnAlbumItemClickListener {
        void onItemClick(int position, Album album);
    }
    public void setOnAlbumItemLongClickListener(onAlbumItemLongClickListener listener){
        this.mLongClickListener = listener;
    }
    /**
     * item长按的接口
     */
    public interface onAlbumItemLongClickListener{
        void onItemLongClick(Album album);
    }
}
