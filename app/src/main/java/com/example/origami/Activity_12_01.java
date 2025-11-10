package com.example.origami;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Activity_12_01 extends AppCompatActivity {

    private RecyclerView rvAudioList;
    private Button btnBackToAct12;
    private AudioAdapter audioAdapter;
    private List<AudioModel> audioList;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_1201);

        rvAudioList = findViewById(R.id.rvAudioList);
        btnBackToAct12 = findViewById(R.id.btnBackToAct12);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setStatusBarTextColor(true);

        initAudioList();
        initRecyclerView();

        btnBackToAct12.setOnClickListener(v -> finish());
    }

    private void initAudioList() {
        audioList = new ArrayList<>();
        audioList.add(new AudioModel("春天里.mp3", "03:42", R.raw.chuntianli));
        audioList.add(new AudioModel("遇见.mp3", "04:12", R.raw.yujian));
        audioList.add(new AudioModel("稻香.mp3", "03:57", R.raw.daoxiang));
    }

    private void initRecyclerView() {
        rvAudioList.setLayoutManager(new LinearLayoutManager(this));
        audioAdapter = new AudioAdapter(audioList, position -> {
            AudioModel audio = audioList.get(position);
            playAudio(audio.getRawResId());
        });
        rvAudioList.setAdapter(audioAdapter);
    }

    private void playAudio(int rawResId) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(this, rawResId);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            Toast.makeText(this, "播放中：" + audioList.get(getCurrentAudioPosition(rawResId)).getAudioName(), Toast.LENGTH_SHORT).show();

            mediaPlayer.setOnCompletionListener(mp -> {
                Toast.makeText(this, "播放完毕", Toast.LENGTH_SHORT).show();
                mp.release();
                mediaPlayer = null;
            });
        } else {
            Toast.makeText(this, "音频加载失败", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCurrentAudioPosition(int rawResId) {
        for (int i = 0; i < audioList.size(); i++) {
            if (audioList.get(i).getRawResId() == rawResId) {
                return i;
            }
        }
        return 0;
    }

    // 修改1：消除 SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 过时警告
    private void setStatusBarTextColor(boolean isDarkText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.setSystemBarsAppearance(
                        isDarkText ? WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS : 0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                );
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 关键：添加注解抑制过时警告，不影响功能
            @SuppressWarnings("deprecation")
            int flag = isDarkText ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : 0;
            getWindow().getDecorView().setSystemUiVisibility(flag);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static class AudioModel {
        private String audioName;
        private String duration;
        private int rawResId;

        public AudioModel(String audioName, String duration, int rawResId) {
            this.audioName = audioName;
            this.duration = duration;
            this.rawResId = rawResId;
        }

        public String getAudioName() { return audioName; }
        public String getDuration() { return duration; }
        public int getRawResId() { return rawResId; }
    }

    public static class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {
        private List<AudioModel> audioList;
        private OnItemClickListener onItemClickListener;

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public AudioAdapter(List<AudioModel> audioList, OnItemClickListener onItemClickListener) {
            this.audioList = audioList;
            this.onItemClickListener = onItemClickListener;
        }

        // 修改2：消除 View.inflate() 过时警告（替换为 LayoutInflater，不影响宽度）
        @Override
        public AudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 官方推荐替代方案，保持 attachToRoot=false，宽度正常
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_1201_item_audio, parent, false);
            return new AudioViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AudioViewHolder holder, int position) {
            AudioModel audio = audioList.get(position);
            holder.tvAudioName.setText(audio.getAudioName());
            holder.tvAudioDuration.setText(audio.getDuration());
            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
        }

        @Override
        public int getItemCount() { return audioList.size(); }

        public static class AudioViewHolder extends RecyclerView.ViewHolder {
            TextView tvAudioName;
            TextView tvAudioDuration;

            public AudioViewHolder(View itemView) {
                super(itemView);
                tvAudioName = itemView.findViewById(R.id.tvAudioName);
                tvAudioDuration = itemView.findViewById(R.id.tvAudioDuration);
            }
        }
    }
}