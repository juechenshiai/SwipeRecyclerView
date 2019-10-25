package com.jessehu.swiperecyclerviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jessehu.swiperecyclerview.adapter.BaseSwipeAdapter;

import java.util.List;


/**
 * SwipeAdapter 类似于RecyclerView.Adapter的使用方式
 *
 * @author JesseHu
 * @date 2019/10/15
 */
public class CustomMenuSwipeAdapter extends BaseSwipeAdapter<CustomMenuSwipeAdapter.ContentViewHolder> {
    private List<String> contentData;

    public CustomMenuSwipeAdapter(Context context, List<String> contentData) {
        super(context);
        this.contentData = contentData;
    }

    @Override
    public ContentViewHolder onCreateContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item的布局，不包含menu
        View itemView = mInflater.inflate(R.layout.item_view, parent, false);
        return new ContentViewHolder(itemView);
    }

    @Override
    public void onBindContentViewHolder(ContentViewHolder holder, int position) {
        holder.titleTv.setText(contentData.get(position));
    }

    @Override
    public int getItemCount() {
        return contentData.size();
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

    public class ContentViewHolder extends BaseSwipeAdapter.BaseViewHolder {
        private TextView titleTv;

        // ItemView不包含menu
        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.tv_title);
        }
    }
}
