package com.hykj.gamecenter.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/10/28.
 * 用于显示menu的adapter，目前的设计是 所有menuItem 的格式一样
 * 该 adapter不涉及 重用
 */
public interface IMenuAdapter {
    int getCount();
    View getView(int position, ViewGroup parent);

    /**
     * 添加分隔线
     * @return
     */
    View getDivider();
}
