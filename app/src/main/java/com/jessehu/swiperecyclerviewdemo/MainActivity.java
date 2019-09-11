package com.jessehu.swiperecyclerviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jessehu.swiperecyclerview.BaseSwipeAdapter;
import com.jessehu.swiperecyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        List<String> contents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            contents.add("这是内容" + (i + 1));
        }
        final List<String> titles = Arrays.asList("删除", "哈哈");
        List<BaseSwipeAdapter.MenuItem> menus = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            BaseSwipeAdapter.MenuItem menuItem = new BaseSwipeAdapter.MenuItem();
            menuItem.setTitle(title);
            if (i == 0) {
                menuItem.setBgColor(Color.YELLOW);
            } else {
                menuItem.setBgColor(Color.BLUE);
            }
            menus.add(menuItem);
        }
        SwipeAdapter swipeAdapter = new SwipeAdapter(mContext, R.layout.item_view, contents);
        swipeAdapter.setMenus(menus);
        swipeAdapter.setMenuWidth(100);
        SwipeRecyclerView listView = findViewById(R.id.srv_list);
        listView.setAdapter(swipeAdapter);
        swipeAdapter.setOnMenuItemClickListener(new BaseSwipeAdapter.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(mContext, titles.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
