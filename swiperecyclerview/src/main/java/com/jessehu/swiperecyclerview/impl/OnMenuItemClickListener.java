package com.jessehu.swiperecyclerview.impl;

import android.view.View;

/**
 * 菜单点击回调
 *
 * @author JesseHu
 * @date 2019/10/16
 */
public interface OnMenuItemClickListener {
    /**
     * 菜单点击回调
     *
     * @param view         菜单对应的view
     * @param itemPosition ItemView在RecyclerView中的位置
     * @param menuPosition 菜单位置
     */
    void onClick(View view, int itemPosition, int menuPosition);
}