package com.example.origami;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_09_02 extends AppCompatActivity {

    SQLiteDatabase db;
    private Button btnBackTo09;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_0902_timu);
        btnBackTo09 = findViewById(R.id.btnBackToAct09);
        btnBackTo09.setOnClickListener(v -> {
            finish(); // 关闭当前录入页面，返回上一级（Activity_09）
        });

        // 打开或创建数据库
        db = openOrCreateDatabase("ChooseDB.db", MODE_PRIVATE, null);
        String createTable = "CREATE TABLE IF NOT EXISTS ProblemS("+
                "_id INTEGER PRIMARY KEY," +
                "Name TEXT NOT NULL," +
                "A TEXT NOT NULL," +
                "B TEXT NOT NULL,"+
                "C TEXT NOT NULL,"+
                "D TEXT NOT NULL)";
        db.execSQL(createTable);

        Button bt = findViewById(R.id.input);
        bt.setOnClickListener(new CallBackByButton01());
    }

    private class CallBackByButton01 implements View.OnClickListener{
        public void onClick(View v){
            EditText t1 = findViewById(R.id.et_seq); // _id
            EditText t2 = findViewById(R.id.et_title); // Name
            EditText t3 = findViewById(R.id.et_choosea); // A
            EditText t4 = findViewById(R.id.et_chooseb); // B
            EditText t5 = findViewById(R.id.et_choosec); // C
            EditText t6 = findViewById(R.id.et_choosed); // D

            // 检查输入是否为空
            if (t1.getText().toString().isEmpty() || t2.getText().toString().isEmpty() ||
                    t3.getText().toString().isEmpty() || t4.getText().toString().isEmpty() ||
                    t5.getText().toString().isEmpty() || t6.getText().toString().isEmpty()) {
                Toast.makeText(Activity_09_02.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues cv = new ContentValues();

            try {
                int id = Integer.parseInt(t1.getText().toString());
                cv.put("_id", id);
                cv.put("Name", t2.getText().toString());
                cv.put("A", t3.getText().toString());
                cv.put("B", t4.getText().toString());
                cv.put("C", t5.getText().toString());
                cv.put("D", t6.getText().toString());

                // 插入数据并获取结果
                long result = db.insert("ProblemS", null, cv);

                if (result == -1) {
                    // 插入失败，很可能是主键冲突
                    Toast.makeText(Activity_09_02.this, "ID已存在，请使用不同的ID", Toast.LENGTH_SHORT).show();
                } else {
                    // 插入成功
                    Toast.makeText(Activity_09_02.this, "数据插入成功", Toast.LENGTH_SHORT).show();
                    // 清空输入框，方便下次输入
                    t1.setText("");
                    t2.setText("");
                    t3.setText("");
                    t4.setText("");
                    t5.setText("");
                    t6.setText("");
                }
            } catch (NumberFormatException e) {
                Toast.makeText(Activity_09_02.this, "ID必须是数字", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(Activity_09_02.this, "插入失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭数据库
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
