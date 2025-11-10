package com.example.origami;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class Activity_08_ChooseAdapter extends BaseAdapter {
    private Context mContext;
    private List<Activity_08_ChooseInfo> mGoodsList;

    public Activity_08_ChooseAdapter(Context context, List<Activity_08_ChooseInfo> goods_lists) {
        mContext = context;
        mGoodsList = goods_lists;
    }

    @Override
    public int getCount() {
        return mGoodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGoodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_08_item_choose, null);
            holder.choosetitle = convertView.findViewById(R.id.choosetitle);
            holder.radioGroup = convertView.findViewById(R.id.rg_options); // 新增：获取RadioGroup
            holder.choosea = convertView.findViewById(R.id.choosea);
            holder.chooseb = convertView.findViewById(R.id.chooseb);
            holder.choosec = convertView.findViewById(R.id.choosec);
            holder.choosed = convertView.findViewById(R.id.choosed);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Activity_08_ChooseInfo info = mGoodsList.get(position);
        // 显示题目和选项
        holder.choosetitle.setText((info.id) + "." + info.title); // 增加题目编号
        holder.choosea.setText("A. " + info.chooseA);
        holder.chooseb.setText("B. " + info.chooseB);
        holder.choosec.setText("C. " + info.chooseC);
        holder.choosed.setText("D. " + info.chooseD);

        // 回显已选答案（如果有）
        clearRadioButton(holder); // 先清空选择
        if (info.selectedAnswer != null) {
            switch (info.selectedAnswer) {
                case "A":
                    holder.choosea.setChecked(true);
                    break;
                case "B":
                    holder.chooseb.setChecked(true);
                    break;
                case "C":
                    holder.choosec.setChecked(true);
                    break;
                case "D":
                    holder.choosed.setChecked(true);
                    break;
            }
        }

        // 监听选项变化，记录选中答案
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.choosea) {
                    info.selectedAnswer = "A";
                } else if (checkedId == R.id.chooseb) {
                    info.selectedAnswer = "B";
                } else if (checkedId == R.id.choosec) {
                    info.selectedAnswer = "C";
                } else if (checkedId == R.id.choosed) {
                    info.selectedAnswer = "D";
                }
            }
        });

        return convertView;
    }

    // 清空RadioButton的选择状态
    private void clearRadioButton(ViewHolder holder) {
        holder.choosea.setChecked(false);
        holder.chooseb.setChecked(false);
        holder.choosec.setChecked(false);
        holder.choosed.setChecked(false);
    }

    public final class ViewHolder {
        public TextView choosetitle;
        public RadioGroup radioGroup; // 新增：RadioGroup
        public RadioButton choosea;
        public RadioButton chooseb;
        public RadioButton choosec;
        public RadioButton choosed;
    }
}