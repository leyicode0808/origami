package com.example.origami;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_12_02 extends AppCompatActivity {

    // 控件声明
    private Button btnOpenCamera;
    private Button btnBackToAct12;
    private ImageView ivPhoto;


    // 拍照相关变量：存储照片的Uri、照片文件路径
    private Uri photoUri;
    private String currentPhotoPath;

    // 权限申请 launcher（适配Android 13+，替代旧版onRequestPermissionsResult）
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                // 检查相机权限和存储权限是否都授予
                boolean cameraGranted = permissions.getOrDefault(Manifest.permission.CAMERA, false);
                boolean storageGranted = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
                        permissions.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false);

                if (cameraGranted && storageGranted) {
                    openSystemCamera(); // 权限都有，打开系统相机
                } else {
                    Toast.makeText(Activity_12_02.this, "请授予相机和存储权限", Toast.LENGTH_SHORT).show();
                }
            });

    // 相机结果 launcher（获取拍照后的照片）
    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // 拍照成功，显示照片到ImageView
                    ivPhoto.setImageURI(photoUri);
                } else {
                    Toast.makeText(this, "拍照已取消", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1202);

        // 初始化控件
        initViews();

        // 设置按钮点击事件
        setButtonListeners();
    }

    // 初始化布局控件
    private void initViews() {
        btnOpenCamera = findViewById(R.id.btnOpenCamera);
        btnBackToAct12 = findViewById(R.id.btnBackToAct12);
        ivPhoto = findViewById(R.id.ivPhoto);
    }

    // 设置按钮点击逻辑
    private void setButtonListeners() {
        // 1. 打开相机按钮：先检查权限，再打开相机
        btnOpenCamera.setOnClickListener(v -> checkCameraAndStoragePermission());

        // 2. 返回按钮：关闭当前Activity，返回上一级（Activity_12）
        btnBackToAct12.setOnClickListener(v -> finish());
    }

    // 检查相机和存储权限（Android 6.0+需动态申请）
    private void checkCameraAndStoragePermission() {
        // 需要申请的权限列表：相机权限 + 存储权限（Android Q以下需要）
        String[] neededPermissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10（Q）及以上：存储权限无需动态申请，用媒体库API
            neededPermissions = new String[]{Manifest.permission.CAMERA};
        } else {
            neededPermissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

        // 检查权限是否已授予
        boolean needRequest = false;
        for (String permission : neededPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                needRequest = true;
                break;
            }
        }

        if (needRequest) {
            // 未授予，发起权限申请
            requestPermissionLauncher.launch(neededPermissions);
        } else {
            // 已授予，直接打开相机
            openSystemCamera();
        }
    }

    // 打开系统相机（核心：创建照片文件 + 调用相机Intent）
    private void openSystemCamera() {
        // 1. 创建存储照片的临时文件（避免拍照后照片丢失）
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "创建照片文件失败", Toast.LENGTH_SHORT).show();
            return;
        }

        if (photoFile != null) {
            // 2. 生成照片的Uri（Android 7.0+用FileProvider，避免Uri暴露异常）
            photoUri = FileProvider.getUriForFile(
                    this,
                    "com.example.origami.fileprovider",  // 与Manifest中配置的authorities一致
                    photoFile
            );

            // 3. 调用系统相机的Intent
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 将照片保存到指定Uri（确保拍照后能获取到完整照片，而非缩略图）
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            // 授予相机临时访问Uri的权限（避免权限不足）
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // 4. 启动相机，等待拍照结果
            takePictureLauncher.launch(takePictureIntent);
        }
    }

    // 创建存储照片的文件（路径：外部存储/Pictures/OrigamiPhotos/xxx.jpg）
    private File createImageFile() throws IOException {
        // 生成唯一的文件名（用时间戳，避免重复）
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // 获取外部存储的Pictures目录（系统默认图片存储目录，无需额外权限）
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // 创建临时照片文件（后缀为.jpg）
        File image = File.createTempFile(
                imageFileName,  // 文件名前缀
                ".jpg",         // 文件名后缀
                storageDir      // 存储目录
        );

        // 保存照片的绝对路径（后续可用于其他操作，如压缩、分享）
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}