package com.jessehu.swiperecyclerviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jessehu.swiperecyclerview.BaseSwipeAdapter;
import com.jessehu.swiperecyclerview.MenuView;
import com.jessehu.swiperecyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        final List<String> contents = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            contents.add("这是内容" + (i + 1));
        }
        final List<String> titles = Arrays.asList("删除", "点击");
        List<BaseSwipeAdapter.MenuItem> menus = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            BaseSwipeAdapter.MenuItem menuItem = new BaseSwipeAdapter.MenuItem();
            menuItem.setTitle(title);
            if (i == 0) {
                menuItem.setBgColor(Color.YELLOW);
                menuItem.setTextColor(Color.BLUE);
                menuItem.setPadding(20);
            } else {
                menuItem.setBgColor(Color.BLUE);
                menuItem.setTextColor(Color.YELLOW);
                menuItem.setWidth(300);
            }
            menuItem.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
            menuItem.setIconGravity(Gravity.END);
            menuItem.setIconSize(50);
            menuItem.setTextSize(10);
            menus.add(menuItem);
        }
        SwipeAdapter swipeAdapter = new SwipeAdapter(mContext, R.layout.item_view, contents);
        swipeAdapter.setMenus(menus);
//        swipeAdapter.setMenuWidth(200);
        SwipeRecyclerView listView = findViewById(R.id.srv_list);
        listView.setAdapter(swipeAdapter);
        swipeAdapter.setOnMenuItemClickListener(new BaseSwipeAdapter.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int itemPosition, int menuPosition) {
                Toast.makeText(mContext, titles.get(menuPosition) + contents.get(itemPosition), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnMenuStatusListener(new SwipeRecyclerView.OnMenuStatusListener() {
            @Override
            public void onOpenStart(View itemView, List<MenuView> menuViewList, int position) {
                Log.i(TAG, "onOpenStart: " + contents.get(position));
                itemView.setBackground(getResources().getDrawable(R.drawable.bg_open));
            }

            @Override
            public void onOpenFinish(View itemView, List<MenuView> menuViewList, int position) {
                Log.i(TAG, "onOpenFinish: " + contents.get(position));
            }

            @Override
            public void onCloseStart(View itemView, List<MenuView> menuViewList, int position) {
                Log.i(TAG, "onCloseStart: " + contents.get(position));
            }

            @Override
            public void onCloseFinish(View itemView, List<MenuView> menuViewList, int position) {
                Log.i(TAG, "onCloseFinish: " + contents.get(position));
                itemView.setBackground(getResources().getDrawable(R.drawable.bg_normal));
            }
        });
        swipeAdapter.setOnItemClickListener(new BaseSwipeAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(mContext, contents.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
