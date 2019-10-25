package com.jessehu.swiperecyclerviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jessehu.swiperecyclerview.SwipeRecyclerView;
import com.jessehu.swiperecyclerview.menu.MenuItem;
import com.jessehu.swiperecyclerview.menu.MenuView;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseOldSwipeAdapter demo
 *
 * @author JesseHu
 * @date 2019/9/20
 */
public class CustomMenuDemoOldActivity extends AppCompatActivity {
    private Context mContext;
    private static final String TAG = CustomMenuDemoOldActivity.class.getSimpleName();
    private List<String> mContents;
    private List<String> mTitles;
    private CustomMenuOldSwipeAdapter mSwipeAdapter;
    private SwipeRecyclerView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mContext = this;

        initData();

        List<MenuItem> menus = createMenuItems(mTitles);
        mSwipeAdapter = new CustomMenuOldSwipeAdapter(mContext, R.layout.item_view, mContents);
        mSwipeAdapter.setMenus(menus);
        // 统一设置menu item的宽度，只针对没有设置宽度的menu item
        // mSwipeAdapter.setMenuWidth(200);

        mListView = findViewById(R.id.srv_list);
        mListView.setAdapter(mSwipeAdapter);

        setListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mContents = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mContents.add("这是内容" + (i + 1));
        }

        mTitles = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            mTitles.add("菜单" + i);
        }
    }

    /**
     * 创建菜单(菜单属性)
     */
    private List<MenuItem> createMenuItems(List<String> titles) {
        List<MenuItem> menus = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            MenuItem menuItem = new MenuItem();
            menuItem.setTitle(title);
            menuItem.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));

            switch (i) {
                case 1:
                    menuItem.setBgColor(Color.RED);
                    menuItem.setTextColor(Color.BLUE);
                    menuItem.setIconPadding(15);
                    menuItem.setWidth(120);
                    menuItem.setTextSize(12);
                    menuItem.setIconGravity(Gravity.TOP);
                    break;
                case 0:
                default:
                    menuItem.setBgColor(Color.YELLOW);
                    menuItem.setTextColor(Color.GREEN);
                    menuItem.setIconPadding(10);
                    menuItem.setWidth(120);
                    menuItem.setTextSize(14);
                    menuItem.setIconGravity(Gravity.START);
                    break;
            }
            menus.add(menuItem);
        }
        return menus;
    }

    /**
     * 设置事件监听
     */
    private void setListener() {
        mSwipeAdapter.setOnMenuItemClickListener((view, itemPosition, menuPosition) -> {
            if (view instanceof MenuView) {
                Toast.makeText(mContext,
                        mTitles.get(menuPosition) + mContents.get(itemPosition), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext,
                        "菜单" + (menuPosition + 1) + mContents.get(itemPosition), Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setOnMenuStatusListener(new SwipeRecyclerView.OnMenuStatusListener() {
            @Override
            public void onOpenStart(View itemView, List<View> menuViewList, int position) {
                Log.i(TAG, "onOpenStart: " + mContents.get(position));
                itemView.setBackground(getResources().getDrawable(R.drawable.bg_open));
            }

            @Override
            public void onOpenFinish(View itemView, List<View> menuViewList, int position) {
                Log.i(TAG, "onOpenFinish: " + mContents.get(position));
                itemView.setBackground(getResources().getDrawable(R.drawable.bg_opened));
            }

            @Override
            public void onCloseStart(View itemView, List<View> menuViewList, int position) {
                Log.i(TAG, "onCloseStart: " + mContents.get(position));
                itemView.setBackground(getResources().getDrawable(R.drawable.bg_open));
            }

            @Override
            public void onCloseFinish(View itemView, List<View> menuViewList, int position) {
                Log.i(TAG, "onCloseFinish: " + mContents.get(position));
                itemView.setBackground(getResources().getDrawable(R.drawable.bg_normal));
            }
        });
        mSwipeAdapter.setOnItemClickListener((view, position) -> {
            mListView.closeMenu();
            Toast.makeText(mContext, mContents.get(position), Toast.LENGTH_SHORT).show();
        });
    }
}
