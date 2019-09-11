package com.jessehu.swiperecyclerviewdemo;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jessehu.swiperecyclerview.BaseSwipeAdapter;

import java.util.List;

/**
 * SwipeAdapter
 *
 * @author JesseHu
 * @date 2019/9/11
 */
public class SwipeAdapter extends BaseSwipeAdapter<String> {

    public SwipeAdapter(Context mContext, int mLayoutId, List<String> contentData) {
        super(mContext, mLayoutId, contentData);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseSwipeAdapter.SwipeHolder holder, String data, int position) {
        TextView titleTv = (TextView) holder.getView(R.id.tv_title);
        titleTv.setText(data);
    }
}
