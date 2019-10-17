package com.jessehu.swiperecyclerviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

/**
 * MainActivity
 *
 * @author JesseHu
 * @date 2019/10/17
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_new).setOnClickListener(v -> {
            Intent intent = new Intent(this, DemoActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btn_old).setOnClickListener(v -> {
            Intent intent = new Intent(this, DemoOldActivity.class);
            startActivity(intent);
        });
    }
}
