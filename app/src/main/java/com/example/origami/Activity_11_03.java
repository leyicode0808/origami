
package com.example.origami;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Activity_11_03 extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TextView tvPageInfo;
    private PdfRenderer pdfRenderer;
    private ParcelFileDescriptor pfd;
    private int totalPages = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1103);

        initViews();
        loadPdf();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        tvPageInfo = findViewById(R.id.tv_page_info);

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // 设置页面变化监听器
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updatePageInfo(position + 1);
            }
        });
    }

    private void loadPdf() {
        try {
            // 从 assets 加载 PDF
            String assetFileName = "tangshi.pdf";
            File pdfFile = new File(getCacheDir(), assetFileName);

            // 如果文件不存在，从 assets 复制
            if (!pdfFile.exists()) {
                copyPdfFromAssets(assetFileName, pdfFile);
            }

            // 打开 PDF
            pfd = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(pfd);
            totalPages = pdfRenderer.getPageCount();

            // 设置 ViewPager 适配器
            PdfPagerAdapter adapter = new PdfPagerAdapter();
            viewPager.setAdapter(adapter);

            // 更新页面信息
            updatePageInfo(1);

        } catch (Exception e) {
            Toast.makeText(this, "加载PDF失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void copyPdfFromAssets(String assetName, File outputFile) throws Exception {
        InputStream inputStream = getAssets().open(assetName);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();
    }

    private void updatePageInfo(int currentPage) {
        tvPageInfo.setText("第 " + currentPage + " 页 / 共 " + totalPages + " 页");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
        if (pfd != null) {
            try {
                pfd.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ViewPager 适配器
    private class PdfPagerAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<PdfPageViewHolder> {

        @Override
        public PdfPageViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return new PdfPageViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(PdfPageViewHolder holder, int position) {
            try {
                // 渲染指定页面
                PdfRenderer.Page page = pdfRenderer.openPage(position);

                // 计算合适尺寸
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = (int) (page.getHeight() * width / page.getWidth());

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                holder.imageView.setImageBitmap(bitmap);
                page.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return totalPages;
        }
    }

    // ViewHolder 类
    private static class PdfPageViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        ImageView imageView;

        PdfPageViewHolder(ImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }
    }
}