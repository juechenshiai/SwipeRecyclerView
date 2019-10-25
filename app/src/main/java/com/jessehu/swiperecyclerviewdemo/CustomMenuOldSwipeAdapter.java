package com.jessehu.swiperecyclerviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
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
public class CustomMenuOldSwipeAdapter extends BaseOldSwipeAdapter<String> {

    public CustomMenuOldSwipeAdapter(Context mContext, int mLayoutId, List<String> contentData) {
        super(mContext, mLayoutId, contentData);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseOldSwipeAdapter.SwipeHolder holder, String data, int position) {
        TextView titleTv = (TextView) holder.getView(R.id.tv_title);
        titleTv.setText(data);
    }

    @Override
    public View createCustomMenuView(int menuPosition) {
        switch (menuPosition) {
            case 0:
                TextView textView = new TextView(mContext);
                textView.setText("123654");
                textView.setGravity(Gravity.CENTER);
                textView.setWidth(150);
                textView.setTextColor(Color.RED);
                textView.setBackgroundColor(Color.BLUE);
                return textView;
            case 1:
                return mInflater.inflate(R.layout.item_menu_view, null);
            default:
                return super.createCustomMenuView(menuPosition);
        }

    }

    @Override
    public int setCustomMenuCount(int viewType) {
        return 2;
    }
}
