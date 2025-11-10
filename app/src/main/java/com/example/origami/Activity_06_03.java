package com.example.origami;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import java.util.Arrays;
import java.util.List;

public class Activity_06_03 extends AppCompatActivity {
    private Button btnBackToAct06; // 仅保留返回Activity_06的按钮
    private ViewPager mViewPager; // 声明ViewPager对象
    private Activity_06_PageAdapter mAdapter; // 声明翻页适配器
    private List<Integer> mLayoutIds; // 存储三个目标页面的布局ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_0603);

        // 初始化“返回Activity_06”按钮
        btnBackToAct06 = findViewById(R.id.btnBackToAct06);
        btnBackToAct06.setOnClickListener(v -> finish()); // 点击关闭当前页，返回Activity_06

        // 初始化控件、数据、适配器
        initView();
        initData();
        setAdapter();
    }

    // 初始化控件（仅保留ViewPager，移除mBtnToMain相关）
    private void initView() {
        mViewPager = findViewById(R.id.viewPager);
    }

    // 初始化数据：添加三个目标页面的布局ID
    private void initData() {
        mLayoutIds = Arrays.asList(
                R.layout.activity_01,
                R.layout.activity_02,
                R.layout.activity_0201
        );
    }

    // 设置适配器
    private void setAdapter() {
        mAdapter = new Activity_06_PageAdapter(this, mLayoutIds);
        mViewPager.setAdapter(mAdapter);
    }
}