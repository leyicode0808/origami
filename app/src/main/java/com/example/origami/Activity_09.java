package com.example.origami;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_09 extends AppCompatActivity {
    private Button btnTo0901;
    private Button btnTo0902;
    private Button btnToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_09);

        // 处理系统边距，避免内容被状态栏/导航栏遮挡
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 绑定所有按钮
        initView();
        // 设置按钮跳转逻辑
        setClickListener();
    }

    private void initView() {
        btnTo0901 = findViewById(R.id.btn_to_0901);
        btnTo0902 = findViewById(R.id.btn_to_0902);
        btnToMain = findViewById(R.id.btnToMain);
    }

    private void setClickListener() {
        // 跳转到“存储用户名”页面（Activity_0901）
        btnTo0901.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_09.this, Activity_09_01.class);
            startActivity(intent);
        });

        // 跳转到“增加试题入库”页面（Activity_0902）
        btnTo0902.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_09.this, Activity_09_02.class);
            startActivity(intent);
        });

        // 返回主页面
        btnToMain.setOnClickListener(v -> finish());
    }
}