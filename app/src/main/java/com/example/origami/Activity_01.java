package com.example.origami;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_01 extends AppCompatActivity {
    private EditText editHeight;
    private EditText editWeight;
    private Button btnCalculate;
    private TextView resultText;
    private Button btnToMain;
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_01);
        // 绑定原有控件
        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        btnCalculate = findViewById(R.id.BMI);
        resultText = findViewById(R.id.resultText);
        // 绑定跳转按钮
        btnToMain = findViewById(R.id.btnToMain);
        btnToMain.setOnClickListener(v -> finish());
        // 计算按钮点击事件
        btnCalculate.setOnClickListener(v -> calculateBMI());

    }


    private void calculateBMI() {
        String heightStr = editHeight.getText().toString().trim();
        String weightStr = editWeight.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "请输入身高和体重", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float height = Float.parseFloat(heightStr);
            float weight = Float.parseFloat(weightStr);

            if (height <= 0 || weight <= 0) {
                Toast.makeText(this, "请输入有效的身高和体重", Toast.LENGTH_SHORT).show();
                return;
            }

            float bmi = weight / (height * height);
            resultText.setText(String.format("你的BMI值：%.1f", bmi));

        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入数字", Toast.LENGTH_SHORT).show();
        }
    }

}
