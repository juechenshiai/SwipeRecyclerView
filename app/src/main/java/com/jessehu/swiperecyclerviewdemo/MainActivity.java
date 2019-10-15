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

/**
 * demo
 *
 * @author JesseHu
 * @date 2019/9/20
 */
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
                menuItem.setTextColor(Color.RED);
                menuItem.setPadding(20);
                menuItem.setIconPadding(20);
                menuItem.setWidth(150);
                menuItem.setIconGravity(Gravity.END);
                menuItem.setTextSize(20);
            } else {
                menuItem.setBgColor(Color.BLUE);
                menuItem.setBgDrawable(getResources().getDrawable(R.drawable.bg_menu));
                menuItem.setTextColor(Color.YELLOW);
                menuItem.setWidth(200);
                menuItem.setIconPadding(10);
                menuItem.setIconGravity(Gravity.BOTTOM);
                menuItem.setIconSize(80);
                menuItem.setTextSize(10);
            }
            menuItem.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
            menus.add(menuItem);
        }
        SwipeAdapter swipeAdapter = new SwipeAdapter(mContext, R.layout.item_view, contents);
        swipeAdapter.setMenus(menus);
//        swipeAdapter.setMenuWidth(200);
        final SwipeRecyclerView listView = findViewById(R.id.srv_list);
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
                itemView.setBackground(getResources().getDrawable(R.drawable.bg_opened));
            }

            @Override
            public void onCloseStart(View itemView, List<MenuView> menuViewList, int position) {
                Log.i(TAG, "onCloseStart: " + contents.get(position));
                itemView.setBackground(getResources().getDrawable(R.drawable.bg_open));
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
                listView.closeMenu();
                Toast.makeText(mContext, contents.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
