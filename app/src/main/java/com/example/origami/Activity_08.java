package com.example.origami;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Activity_08 extends AppCompatActivity {

    private List<Activity_08_ChooseInfo> chooses;
    private Activity_08_ChooseAdapter adapter;
    private SQLiteDatabase db;
    // 1. 新增：保存广播接收器实例（解决实例不匹配问题）
    private Activity_08_AnswerReceiver answerReceiver;
    // 在 onCreate 方法上添加注解，抑制 Lint 警告
    @SuppressLint("UnspecifiedRegisterReceiverFlag")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_08_test_choose);

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        ListView list = findViewById(R.id.lv_cart);
        Button btnSubmit = findViewById(R.id.btn_settle);

        chooses = new ArrayList<>();
        adapter = new Activity_08_ChooseAdapter(this, chooses);
        list.setAdapter(adapter);

        openDatabase();
        queryProblemsFromDB();

        btnSubmit.setOnClickListener(v -> {
            boolean hasUnanswered = false;
            for (Activity_08_ChooseInfo info : chooses) {
                if (info.selectedAnswer == null) {
                    hasUnanswered = true;
                    break;
                }
            }
            if (hasUnanswered) {
                Toast.makeText(Activity_08.this, "请完成所有题目再提交", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder answerSb = new StringBuilder();
            for (Activity_08_ChooseInfo info : chooses) {
                answerSb.append(info.id).append(":").append(info.selectedAnswer).append(",");
            }
            String answerStr = answerSb.toString().replaceAll(",$", "");

            Intent intent = new Intent(Activity_08_Constant.ACTION_SUBMIT_ANSWER);
            intent.putExtra("answer", answerStr);
            // 新增这一行：指定广播只发给当前 App
            intent.setPackage(getPackageName());
            sendBroadcast(intent);
        });

        // 2. 修复：广播注册（解决实例不匹配 + Android13+ 安全标记问题）
        answerReceiver = new Activity_08_AnswerReceiver();
        IntentFilter filter = new IntentFilter(Activity_08_Constant.ACTION_SUBMIT_ANSWER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(answerReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(answerReceiver, filter);
        }
    }

    private void openDatabase() {
        try {
            db = openOrCreateDatabase("ChooseDB.db", MODE_PRIVATE, null);
            // 3. 新增：创建表（解决表不存在报错）
            String createTable = "CREATE TABLE IF NOT EXISTS ProblemS (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Name TEXT NOT NULL," +
                    "A TEXT NOT NULL," +
                    "B TEXT NOT NULL," +
                    "C TEXT NOT NULL," +
                    "D TEXT NOT NULL)";
            db.execSQL(createTable);
        } catch (Exception e) {
            Toast.makeText(this, "数据库打开失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void queryProblemsFromDB() {
        if (db == null || !db.isOpen()) {
            Toast.makeText(this, "数据库未打开", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = null;
        try {
            cursor = db.query(
                    "ProblemS",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "_id ASC"
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // 关键修改：先获取索引并校验有效性（避免 -1 导致崩溃）
                    int idIndex = cursor.getColumnIndex("_id");
                    int nameIndex = cursor.getColumnIndex("Name");
                    int aIndex = cursor.getColumnIndex("A");
                    int bIndex = cursor.getColumnIndex("B");
                    int cIndex = cursor.getColumnIndex("C");
                    int dIndex = cursor.getColumnIndex("D");

                    // 若任意字段索引无效，跳过当前数据并提示
                    if (idIndex == -1 || nameIndex == -1 || aIndex == -1 || bIndex == -1 || cIndex == -1 || dIndex == -1) {
                        Toast.makeText(this, "跳过无效题目（字段名不匹配）", Toast.LENGTH_SHORT).show();
                        continue;
                    }

                    // 从有效索引读取数据（消除警告）
                    int problemId = cursor.getInt(idIndex);
                    String title = cursor.getString(nameIndex);
                    String chooseA = cursor.getString(aIndex);
                    String chooseB = cursor.getString(bIndex);
                    String chooseC = cursor.getString(cIndex);
                    String chooseD = cursor.getString(dIndex);

                    Activity_08_ChooseInfo info = new Activity_08_ChooseInfo();
                    info.id = problemId;
                    info.title = title;
                    info.chooseA = chooseA;
                    info.chooseB = chooseB;
                    info.chooseC = chooseC;
                    info.chooseD = chooseD;
                    chooses.add(info);

                } while (cursor.moveToNext());

                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "数据库中暂无题目数据，请点击按钮9添加数据", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "查询题目失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 修复：注销同一个广播实例
        unregisterReceiver(answerReceiver);
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}