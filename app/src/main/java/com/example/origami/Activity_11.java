package com.example.origami;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_11 extends AppCompatActivity {
    private Button btnToMain;
    // 声明另外两个跳转按钮
    private Button btnWebView1;
    private Button btnWebView2;

    private Button btnPdfViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_11); // 注意：布局文件名需与实际一致（此处假设为activity_11.xml）

        // 绑定按钮
        btnToMain = findViewById(R.id.btnToMain);
        btnWebView1 = findViewById(R.id.btnWebView1); // 对应布局中"WebView的使用"按钮的id
        btnWebView2 = findViewById(R.id.btnWebView2);
        btnPdfViewer = findViewById(R.id.btnPdfViewer); // 对应布局中"浏览pdf文件"按钮的id

        // 返回主页面
        btnToMain.setOnClickListener(v -> finish());

        btnWebView1.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_11.this, Activity_11_01.class);
            startActivity(intent);
        });
        btnWebView2.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_11.this, Activity_11_02.class);
            startActivity(intent);
        });


        btnPdfViewer.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_11.this, Activity_11_03.class);
            startActivity(intent);
        });

        // 处理系统窗口Insets（边缘适配）
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}