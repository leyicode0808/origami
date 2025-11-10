package com.example.origami;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_12 extends AppCompatActivity {

    // 声明布局中的四个按钮（三个功能按钮 + 一个返回按钮）
    private Button btnMusicPlay;
    private Button btnTakePhoto;
    private Button btnVideoPlay;
    private Button btnToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_12);

        // 1. 绑定布局中的按钮（ID需与activity_12.xml中完全一致）
        initButtons();

        // 2. 为每个按钮设置跳转/点击逻辑
        setButtonClickListeners();

        // 保留原有的全面屏适配：处理系统状态栏、导航栏的内边距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 初始化按钮：绑定布局中的控件ID
    private void initButtons() {
        btnMusicPlay = findViewById(R.id.btn_music_play);   // 音乐列表的播放按钮
        btnTakePhoto = findViewById(R.id.btn_take_photo);   // 手机拍照按钮
        btnVideoPlay = findViewById(R.id.btn_video_play);   // 视频播放按钮
        btnToMain = findViewById(R.id.btnToMain);           // 返回按钮
    }

    // 设置按钮点击事件：实现跳转逻辑
    private void setButtonClickListeners() {
        // ① 音乐列表的播放 → 跳转到 Activity_12_01
        btnMusicPlay.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_12.this, Activity_12_01.class);
            startActivity(intent);
        });

        // ② 手机拍照 → 跳转到 Activity_12_02
        btnTakePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_12.this, Activity_12_02.class);
            startActivity(intent);
        });

        // ③ 视频播放 → 跳转到 Activity_12_03
        btnVideoPlay.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_12.this, Activity_12_03.class);
            startActivity(intent);
        });

        // ④ 返回按钮 → 关闭当前Activity（返回上一级页面）
        btnToMain.setOnClickListener(v -> finish());
    }
}