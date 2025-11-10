package com.example.origami;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_09_01 extends AppCompatActivity {
    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private Button btnDoLogin;
    private Button btnBackTo09;
    // 用于存储用户名的SharedPreferences（命名为"login_info"，私有模式）
    private SharedPreferences loginSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_0901);

        // 处理系统边距，避免内容被遮挡
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件和SharedPreferences
        initViewAndSp();
        // 自动加载已保存的用户名
        loadSavedUsername();
        // 绑定按钮点击事件
        setButtonListeners();
    }

    private void initViewAndSp() {
        etLoginUsername = findViewById(R.id.et_login_username);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnDoLogin = findViewById(R.id.btn_do_login);
        btnBackTo09 = findViewById(R.id.btnBackToAct09);

        // 初始化SharedPreferences：存储路径为/data/data/应用包名/shared_prefs/login_info.xml
        loginSp = getSharedPreferences("login_info", Context.MODE_PRIVATE);
    }

    // 读取SharedPreferences中保存的用户名，自动填充到输入框
    private void loadSavedUsername() {
        // 第二个参数""是默认值：若未存过用户名，返回空字符串
        String savedUsername = loginSp.getString("saved_username", "");
        if (!savedUsername.isEmpty()) {
            etLoginUsername.setText(savedUsername);
            etLoginPassword.requestFocus(); // 光标自动定位到密码框，优化体验
        }
    }

    private void setButtonListeners() {
        // 登录按钮逻辑
        btnDoLogin.setOnClickListener(v -> {
            String inputUsername = etLoginUsername.getText().toString().trim();
            String inputPassword = etLoginPassword.getText().toString().trim();

            // 1. 简单校验：用户名和密码不能为空
            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(Activity_09_01.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. 模拟登录校验（实际项目需替换为后端接口校验，此处用固定密码"123456"测试）
            if (inputPassword.equals("123456")) {
                // 登录成功：保存用户名到SharedPreferences
                SharedPreferences.Editor editor = loginSp.edit();
                editor.putString("saved_username", inputUsername); // 存入用户名
                editor.apply(); // 异步提交（比commit()高效，避免阻塞主线程）

                // 提示登录成功，并跳转到主界面（可根据需求修改跳转目标）
                Toast.makeText(Activity_09_01.this, "登录成功！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Activity_09_01.this, MainActivity.class));
                finish(); // 关闭当前登录页，避免返回键回到登录页
            } else {
                // 密码错误提示
                Toast.makeText(Activity_09_01.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                etLoginPassword.setText(""); // 清空密码输入框
                etLoginPassword.requestFocus();
            }
        });

        // 返回实验9页面（Activity_09）
        btnBackTo09.setOnClickListener(v -> finish());
    }
}