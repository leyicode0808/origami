package com.example.origami;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Activity_06_02_PhoneAdapter extends BaseAdapter
{
    private Context mContext; // 声明一个上下文对象
    private List<Activity_06_CartInfo> mGoodsList;

    public Activity_06_02_PhoneAdapter(Context context, List<Activity_06_CartInfo> goods_lists)
    {
        mContext = context;
        mGoodsList = goods_lists;
    }

    // 获取列表项的个数
    public int getCount() {
        return mGoodsList.size();
    }

    // 获取列表项的数据
    public Object getItem(int arg0) {
        return mGoodsList.get(arg0);
    }

    // 获取列表项的编号
    public long getItemId(int arg0) {
        return arg0;
    }

    // 获取指定位置的列表项视图
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) { // 转换视图为空
            holder = new ViewHolder(); // 创建一个新的视图持有者
            // 根据布局文件item_goods.xml生成转换视图对象
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_0602_item_goods, null);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.iv_thumb = convertView.findViewById(R.id.iv_thumb);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.btn_add = convertView.findViewById(R.id.btn_add);
            // 将视图持有者保存到转换视图当中
            convertView.setTag(holder);
        } else { // 转换视图非空
            // 从转换视图中获取之前保存的视图持有者
            holder = (ViewHolder) convertView.getTag();
        }
        final Activity_06_CartInfo goods = mGoodsList.get(position);
        holder.tv_name.setText(goods.name); // 显示商品的名称
        //holder.iv_thumb.setImageURI(Uri.parse(goods.pic_path)); // 设置商品图片
        holder.iv_thumb.setImageResource(goods.pic);
        holder.tv_price.setText("" + (int) goods.price); // 显示商品的价格

        return convertView;
    }

    // 定义一个视图持有者，以便重用列表项的视图资源
    public final class ViewHolder {
        public TextView tv_name; // 声明商品名称的文本视图对象
        public ImageView iv_thumb; // 声明商品图片的图像视图对象
        public TextView tv_price; // 声明商品价格的文本视图对象
        public Button btn_add; // 声明加入购物车的按钮对象
    }


}
