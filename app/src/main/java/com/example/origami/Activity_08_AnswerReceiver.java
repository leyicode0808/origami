package com.example.origami;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class Activity_08_AnswerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Activity_08_Constant.ACTION_SUBMIT_ANSWER.equals(intent.getAction())) {
            // 1. 获取答案并震动1秒
            String answer = intent.getStringExtra("answer");
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(Activity_08_Constant.VIBRATE_DURATION); // 震动1秒
            }

            // 2. 显示提交结果
            Toast.makeText(context, "提交成功！你的答案：\n" + answer, Toast.LENGTH_LONG).show();

        }
    }
}