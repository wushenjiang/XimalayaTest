package com.liuge.ximalaya.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.liuge.ximalaya.MainActivity;
import com.liuge.ximalaya.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * FileName: IndicatorAdapter
 * Author: LiuGe
 * Date: 2020/7/23 20:41
 * Description: 顶部切换栏适配器
 */
public class IndicatorAdapter extends CommonNavigatorAdapter {


    private final String[] mTitles;
    private OnIndicatorTapClickListener mOnTabClickListener;

    public IndicatorAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.indicator_title);

    }

    @Override
    public int getCount() {
        if (mTitles != null) {
            return mTitles.length;
        }
        return 0;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        // 创建View
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        // 设置一般情况下的颜色为灰色
        colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#aaffffff"));
        // 设置选中情况下的颜色为黑色
        colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
        // 单位sp
        colorTransitionPagerTitleView.setTextSize(18);
        // 设置要显示的内容
        colorTransitionPagerTitleView.setText(mTitles[index]);
        // 设置title的点击事件,点击了title就跳转到对应的index里去
        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换viewPager的内容,如果index不一样的话
                if (mOnTabClickListener != null) {
                    mOnTabClickListener.onTabClick(index);
                }
            }
        });
        // 返回view
        return colorTransitionPagerTitleView;

    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
        linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        linePagerIndicator.setColors(Color.parseColor("#ffffff"));
        return linePagerIndicator;
    }
    public void setOnIndicatorTapClickListener(OnIndicatorTapClickListener listener){
        this.mOnTabClickListener = listener;
    }
    public interface OnIndicatorTapClickListener{
        void onTabClick(int index);
    }
}
