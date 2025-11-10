package com.example.origami;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("SetTextI18n")
public class Activity_11_02 extends AppCompatActivity {
    private String defaultWebUrl = "https://www.bilibili.com"; // 默认B站地址
    private Button btnBackToAct011;
    private Button btnLoadUrl; // 访问按钮
    private EditText etWebUrl; // 网址输入框
    private WebView wvAssetsWeb;
    private TextView tvWebPath;
    private OnBackPressedCallback backCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1102);

        // 初始化所有控件（新增输入框、访问按钮）
        initViews();

        // 配置WebView（保持原有优化配置）
        configWebView();

        // 默认加载B站
        loadWebUrl(defaultWebUrl);

        // 绑定访问按钮点击事件（加载输入的网址）
        btnLoadUrl.setOnClickListener(v -> loadInputUrl());

        // 绑定返回按钮事件
        btnBackToAct011.setOnClickListener(v -> finish());

        // 兼容Android 16+返回手势
        initBackCallback();
    }

    // 初始化所有控件
    private void initViews() {
        tvWebPath = findViewById(R.id.tv_web_path);
        btnBackToAct011 = findViewById(R.id.btnBackToAct011);
        wvAssetsWeb = findViewById(R.id.wv_assets_web);
        etWebUrl = findViewById(R.id.et_web_url); // 绑定输入框
        btnLoadUrl = findViewById(R.id.btn_load_url); // 绑定访问按钮
    }

    // 配置WebView（适配动态网页，如B站、自定义网址）
    private void configWebView() {
        WebSettings webSettings = wvAssetsWeb.getSettings();
        webSettings.setJavaScriptEnabled(true); // 支持JS（必选）
        webSettings.setDomStorageEnabled(true); // 支持DOM存储
        webSettings.setAllowContentAccess(true); // 允许加载资源
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // 缓存策略
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setBuiltInZoomControls(true); // 内置缩放按钮
        // 模拟浏览器UA，避免部分网站拒绝WebView访问
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; SM-G975F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Mobile Safari/537.36");

        // 强制在WebView内打开链接，不跳转到系统浏览器
        wvAssetsWeb.setWebViewClient(new WebViewClient());
    }

    // 加载指定网址
    private void loadWebUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            Toast.makeText(this, "网址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        wvAssetsWeb.loadUrl(url);
        tvWebPath.setText("当前加载：" + url); // 更新提示文本
    }

    // 处理输入框的网址（合法性判断 + 加载）
    private void loadInputUrl() {
        // 1. 获取输入框内容并去除空格
        String inputUrl = etWebUrl.getText().toString().trim();
        if (inputUrl.isEmpty()) {
            Toast.makeText(this, "请输入有效网址", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. 网址合法性处理：如果没有http/https前缀，自动添加https://
        if (!inputUrl.startsWith("http://") && !inputUrl.startsWith("https://")) {
            inputUrl = "https://" + inputUrl;
        }

        // 3. 隐藏软键盘（输入完成后关闭键盘）
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etWebUrl.getWindowToken(), 0);

        // 4. 加载处理后的网址
        loadWebUrl(inputUrl);
    }

    // 初始化返回手势回调（兼容Android 16+）
    private void initBackCallback() {
        backCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (wvAssetsWeb.canGoBack()) {
                    wvAssetsWeb.goBack(); // WebView有历史页，返回上一页
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed(); // 关闭Activity
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除回调 + 销毁WebView，避免内存泄漏
        if (backCallback != null) {
            backCallback.remove();
        }
        if (wvAssetsWeb != null) {
            wvAssetsWeb.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wvAssetsWeb.clearHistory();
            wvAssetsWeb.destroy();
            wvAssetsWeb = null;
        }
    }
}