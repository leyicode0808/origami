package com.example.origami;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_02 extends AppCompatActivity {
    private Button btnToMain;
    private Button btnToAct0201; // 章节练习按钮
    private Button btnToAct0202; // 模拟考试按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_02);
        // 绑定跳转按钮
        btnToMain = findViewById(R.id.btnToMain);
        btnToAct0201 = findViewById(R.id.btnChapterExercise);
        btnToAct0202=findViewById(R.id.btnMockExam);
        btnToMain.setOnClickListener(v -> finish());
        // 章节练习按钮点击事件
        btnToAct0201.setOnClickListener(v -> {
            Intent intent = new Intent(this,Activity_02_01.class);
            startActivity(intent);
        });
        btnToAct0202.setOnClickListener(v -> {
            Intent intent = new Intent(this,Activity_02_02.class);
            startActivity(intent);
        });
    }
}