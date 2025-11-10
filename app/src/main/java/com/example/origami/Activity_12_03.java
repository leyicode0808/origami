package com.example.origami;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_12_03 extends AppCompatActivity {

    private VideoView videoView;
    private Button btnPlay, btnStop, btnBackToAct12;
    private Uri videoUri;
    private boolean isFirstLaunch = true; // 首次启动标记（控制自动播放）
    private boolean isVideoPrepared = false; // 标记视频是否准备完成（修复错误关键）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1203);

        // 初始化控件
        videoView = findViewById(R.id.vv_video);
        btnPlay = findViewById(R.id.btn_play);
        btnStop = findViewById(R.id.btn_stop);
        btnBackToAct12 = findViewById(R.id.btnBackToAct12);

        // 绑定raw文件夹的apple.mp4
        videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.apple);
        videoView.setVideoURI(videoUri);

        // 准备完成监听：首次启动自动播放，同时标记准备完成
        videoView.setOnPreparedListener(mp -> {
            isVideoPrepared = true; // 视频准备完成，设为true
            if (isFirstLaunch) {
                videoView.start();
                Toast.makeText(this, "自动播放中", Toast.LENGTH_SHORT).show();
                isFirstLaunch = false; // 首次播放后重置
            }
        });

        // Play按钮：继续/重新播放
        btnPlay.setOnClickListener(v -> {
            if (isVideoPrepared && !videoView.isPlaying()) {
                videoView.start();
                Toast.makeText(this, "开始播放", Toast.LENGTH_SHORT).show();
            }
        });

        // Stop按钮：停止播放并重置到开头（不自动播放）
        btnStop.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause(); // 暂停播放
                videoView.seekTo(0); // 回到视频开头
                Toast.makeText(this, "已停止播放", Toast.LENGTH_SHORT).show();
            }
        });

        // 返回按钮：关闭当前页面
        btnBackToAct12.setOnClickListener(v -> finish());
    }

    // 页面暂停时暂停视频
    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    // 页面恢复时：只恢复“暂停状态”的视频，Stop后不自动播放（修复错误）
    @Override
    protected void onResume() {
        super.onResume();
        // 只有“视频已准备完成”+“不是首次启动”+“当前未播放”，才恢复播放
        if (isVideoPrepared && !isFirstLaunch && !videoView.isPlaying()) {
            videoView.start();
        }
    }

    // 销毁时释放资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback(); // 彻底释放视频资源
    }
}