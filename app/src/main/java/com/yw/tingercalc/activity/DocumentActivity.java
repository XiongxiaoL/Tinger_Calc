package com.yw.tingercalc.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.yw.tingercalc.R;

public class DocumentActivity extends AppCompatActivity {

    //各种说明
    WebView webView;
    ScrollView s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_ativity);
        webView=findViewById(R.id.webview);
        s=findViewById(R.id.home);
        initWebviewSetting();
        load();
    }

    private void load() {
        int type = getIntent().getIntExtra("type", 0);
        s.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        switch (type){
            case 1:
                webView.loadUrl("file:///android_asset/web/test/gl/gl.html");
                break;
            case 11:{
                webView.loadUrl("file:///android_asset/web/test/glEn/gailv_en.html");
                break;
            }
            case 2:
                webView.loadUrl("file:///android_asset/web/test/jz.html");
                break;
            case 22:{
                webView.loadUrl("file:///android_asset/web/test/juzhen_en.html");
                break;
            }
            case 3:
                webView.loadUrl("file:///android_asset/web/test/A.html");
                break;
            case 33:
                webView.loadUrl("file:///android_asset/web/test/A_en.html");
                break;
            case 4:
                webView.loadUrl("file:///android_asset/web/test/szjs/szjs.html");
                break;
            case 44:
                webView.loadUrl("file:///android_asset/web/test/szjs/szjs_en.html");
                break;
            case 5:
                s.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
//                webView.loadUrl("file:///android_asset/web/test/home/home.html");
                break;

        }
    }

    private void initWebviewSetting() {
        final WebSettings webSettings=webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不缓存网页
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
//
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;// 返回false
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // 加载开始
            }
        });

    }

}