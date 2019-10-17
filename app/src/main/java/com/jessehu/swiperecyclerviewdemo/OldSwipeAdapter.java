package com.jessehu.swiperecyclerviewdemo;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jessehu.swiperecyclerview.adapter.BaseOldSwipeAdapter;

import java.util.List;

/**
 * OldSwipeAdapter 原先的adapter版本
 *
 * @author JesseHu
 * @date 2019/9/11
 */
public class OldSwipeAdapter extends BaseOldSwipeAdapter<String> {

    public OldSwipeAdapter(Context mContext, int mLayoutId, List<String> contentData) {
        super(mContext, mLayoutId, contentData);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseOldSwipeAdapter.SwipeHolder holder, String data, int position) {
        TextView titleTv = (TextView) holder.getView(R.id.tv_title);
        titleTv.setText(data);
    }
}
