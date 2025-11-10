package com.example.origami;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_02_02 extends AppCompatActivity {
    private Button btnBackToAct02;
    private RadioGroup radioGroup;
    private Button btnSubmit;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_0202);
        btnBackToAct02 = findViewById(R.id.btnBackToAct02);
        btnBackToAct02.setOnClickListener(v -> finish());

        radioGroup = findViewById(R.id.radioGroup);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取选中的选项
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId == -1) {
                    // 未选择任何选项
                    Toast.makeText(Activity_02_02.this, "请选择一个选项", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 获取选中的文本
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedText = selectedRadioButton.getText().toString();

                // 创建并显示对话框
                dialog = new AlertDialog.Builder(Activity_02_02.this)
                        .setTitle("提交成功")
                        .setMessage("你选择了：" + selectedText)
                        .setCancelable(false) // 不可取消
                        .create();
                dialog.show();

                // 2秒后关闭对话框
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();

                            // 对话框关闭后，再等2秒返回上一页
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish(); // 关闭当前页面，返回Shiyan2Activity
                                }
                            }, 2000);
                        }
                    }
                }, 2000);
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 防止内存泄漏
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}