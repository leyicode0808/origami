package com.example.origami;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import java.util.List;

public class Activity_06_PageAdapter extends PagerAdapter {
    private Context mContext; // 声明上下文对象
    private List<Integer> mLayoutIds; // 存储页面布局ID列表

    // 构造方法：初始化上下文和布局ID列表
    public Activity_06_PageAdapter(Context context, List<Integer> layoutIds) {
        mContext = context;
        mLayoutIds = layoutIds;
    }

    // 获取页面数量
    @Override
    public int getCount() {
        return mLayoutIds.size();
    }

    // 判断视图与对象是否关联
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    // 初始化指定位置的页面
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // 根据布局ID加载页面视图
        View view = LayoutInflater.from(mContext)
                .inflate(mLayoutIds.get(position), container, false);
        // 将视图添加到容器
        container.addView(view);
        return view;
    }

    // 销毁指定位置的页面
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 从容器中移除视图
        container.removeView((View) object);
    }
}