package com.example.origami;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;//延申到任务栏，扩展屏幕
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnExit;

    // 用private数组存储20个按钮引用（替代20个单独的private变量）
    private Button[] buttons = new Button[16]; // 索引0对应btn_1，索引1对应btn_2...
    // 定义「已使用的按钮编号」列表（根据getTargetActivity的case整理）
    private final List<Integer> USED_BUTTONS = Arrays.asList(1, 2, 6, 8, 9, 11, 12);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 处理系统边距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnExit = findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(v -> {
            finishAffinity();
            System.exit(0);
        });

        // 批量初始化按钮，并存储到private数组中（保持封装性）
        for (int i = 0; i < 16; i++) {
            int buttonNum = i + 1; // 按钮编号：1-20
            // 动态获取按钮ID（btn_1到btn_20）
            int buttonId = getResources().getIdentifier(
                    "btn_" + buttonNum,
                    "id",
                    getPackageName()
            );
            // 初始化并存储到private数组（相当于private Button btn_1, btn_2...）
            buttons[i] = findViewById(buttonId);

            // 设置点击事件（用final保存当前按钮编号）
            final int finalButtonNum = buttonNum;
            if (buttons[i] != null) {
                // 关键：未使用的按钮 → 背景色改为@color/btn_color_0
                if (!USED_BUTTONS.contains(finalButtonNum)) {
                    buttons[i].setBackgroundResource(R.color.btn_color_0);
                }
                // 点击事件（核心修改）
                buttons[i].setOnClickListener(v -> {
                    // 判断：是否是已使用的按钮
                    if (USED_BUTTONS.contains(finalButtonNum)) {
                        // 已使用 → 正常跳转对应页面
                        Intent intent = new Intent(MainActivity.this, getTargetActivity(finalButtonNum));
                        startActivity(intent);
                    } else {
                        // 未使用 → 弹提示，不跳转
                        Toast.makeText(MainActivity.this, "该按钮还没绑定任何活动，暂无任何功能", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    // 页面匹配方法（同上）
    private Class<?> getTargetActivity(int buttonNum) {
        switch (buttonNum) {
            case 1: return Activity_01.class;
            case 2: return Activity_02.class;
            case 6: return Activity_06.class;
            case 8: return Activity_08.class;
            case 9: return Activity_09.class;
            case 11: return Activity_11.class;
            case 12: return Activity_12.class;

            default: return MainActivity.class;
        }
    }
}