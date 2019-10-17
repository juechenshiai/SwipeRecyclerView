package com.jessehu.swiperecyclerview.impl;

import android.view.View;

/**
 * item点击回调
 *
 * @author JesseHu
 * @date 2019/10/16
 */
public interface OnItemClickListener {
    /**
     * ItemView点击回调
     *
     * @param view     自定义content的layout，即初始化adapter时传入的layout
     * @param position 当前点击的item的position
     */
    void onClick(View view, int position);
}