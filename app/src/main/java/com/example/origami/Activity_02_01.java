package com.example.origami;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_02_01 extends AppCompatActivity {
    private Button btnBackToAct02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_0201);
        btnBackToAct02 = findViewById(R.id.btnBackToAct02);
        btnBackToAct02.setOnClickListener(v -> finish());
    }
}