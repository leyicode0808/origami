package com.example.origami;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_06 extends AppCompatActivity {
    private Button btnListView;
    private Button btnGridView;
    private Button btnPageView;
    private Button btnToMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_06);
        // 绑定跳转按钮
        btnToMain = findViewById(R.id.btnToMain);
        btnListView = findViewById(R.id.btn_list_view);
        btnGridView = findViewById(R.id.btn_grid_view);
        btnPageView = findViewById(R.id.btn_page_view);
        btnToMain.setOnClickListener(v -> finish());
        // 跳转到ListView界面（需替换为你实际的ListView页面类名）
        btnListView.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_06.this, Activity_06_01.class);
            startActivity(intent);
        });

        // 跳转到GridView界面（需替换为你实际的GridView页面类名）
        btnGridView.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_06.this, Activity_06_02.class);
            startActivity(intent);
        });

        // 跳转到翻页界面（需替换为你实际的翻页页面类名，如ViewPager页面）
        btnPageView.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_06.this, Activity_06_03.class);
            startActivity(intent);
        });
    }
}