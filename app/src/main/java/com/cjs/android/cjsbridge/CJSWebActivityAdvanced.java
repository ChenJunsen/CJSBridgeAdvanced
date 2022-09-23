package com.cjs.android.cjsbridge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.cjs.android.cjsbridge.core.CJSBridge;
import com.cjs.android.cjsbridge.web.CJSWebView;

import cjsbridge_ui.head.CJSHeadBar;

public class CJSWebActivityAdvanced extends AppCompatActivity implements CJSWebView.WebViewInitListener {

    private CJSHeadBar headBar;//标题栏
    private CJSWebView webView;//Web容器

    private boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cjs_web_advanced);
        headBar = findViewById(R.id.headBar);
        webView = findViewById(R.id.webView);

        Intent i = getIntent();
        if (i != null) {
            headBar.setTitle(i.getStringExtra("title"));
            webView.loadUrl(i.getStringExtra("url"));
        }

        //重写返回键方法
        headBar.setOnBackClickListener(new CJSHeadBar.OnBackClickListener() {
            @Override
            public void onBackClick(View v) {
                CJSBridge.triggerEvent(webView, "back");
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });

        //设置标题栏右键刷新
        headBar.setOnRightClickListener(new CJSHeadBar.OnRightClickListener() {
            @Override
            public void onRightClick(View v) {
                if (webView != null) {
                    webView.reload();
                    Toast.makeText(CJSWebActivityAdvanced.this, "刷新", Toast.LENGTH_SHORT).show();
                }
            }
        });

        webView.setWebViewInitListener(this);

    }

    @Override
    public void onWebViewBridgeInitialized(WebView webView) {
        //注册事件
        CJSBridge.addEventListener(webView, "resume");
        CJSBridge.addEventListener(webView, "back");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null && isCreated) {
            JSONObject params = new JSONObject();
            params.put("action", "resume");
            //原生页面唤醒时，告知H5页面唤醒
            CJSBridge.triggerEvent(webView, "resume", params);
        }
        isCreated=true;
    }

}
