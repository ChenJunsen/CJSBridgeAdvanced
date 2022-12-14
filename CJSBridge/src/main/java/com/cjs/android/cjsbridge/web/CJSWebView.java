package com.cjs.android.cjsbridge.web;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import com.cjs.android.cjsbridge.common.scheme.CJScheme;
import com.cjs.android.cjsbridge.common.tools.L;
import com.cjs.android.cjsbridge.common.tools.SystemUtil;
import com.cjs.android.cjsbridge.core.CJSBridge;
import com.cjs.android.cjsbridge.core.exception.CJSBException;
import com.cjs.android.cjsbridge.dispatch.CJSActionDispatcher;


/**
 * 进阶版WebView
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @createTime 2020/9/16 0016 17:59
 */
public class CJSWebView extends WebView implements CJSActionDispatcher {

    public CJSWebView(Context context) {
        super(context);
        init(context);
    }

    public CJSWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CJSWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CJSWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public CJSWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init(context);
    }

    private WebViewInitListener webViewInitListener;

    public void setWebViewInitListener(WebViewInitListener webViewInitListener) {
        this.webViewInitListener = webViewInitListener;
    }

    private void init(Context context) {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        CJSWebViewClient webViewClient = new CJSWebViewClient();
        webViewClient.setCjsActionDispatcher(this);
        setWebViewClient(webViewClient);
        setWebChromeClient(new CJSWebChromeClient((Activity) context));
        //可选操作 增加设备UA，方便H5判断当前设备型号
        String uaStr = webSettings.getUserAgentString();
        webSettings.setUserAgentString(uaStr + "/" + SystemUtil.getSystemModel());

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    CJSBridge2.callH5(CJSWebView.this,"CJSBridge.registerEvent(resume)");
                } catch (CJSBException e) {
                    e.printStackTrace();
                }
            }
        },3000);*/
    }


    @Override
    public void onPageStarted(WebView webView, String url) {

    }

    @Override
    public void onJSBridgeInitialized(WebView webView) {
        L.i("init", "CJSBridge注入成功");
        if(webViewInitListener!=null){
            webViewInitListener.onWebViewBridgeInitialized(webView);
        }
    }


    @Override
    public void dispatchH5Action(WebView webView, CJScheme cjScheme) {
        try {
            CJSBridge.getInstance().callNative(webView, cjScheme);
        } catch (CJSBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageFinished(WebView webView, String url) {

    }

    /**
     * CJSWebView监听器
     * @author JasonChen
     * @email chenjunsen@outlook.com
     * @createTime 2020/9/24 0024 18:03
     */
    public interface WebViewInitListener{
        /**
         * JS桥初始化完成的时候
         * @param webView
         */
        void onWebViewBridgeInitialized(WebView webView);
    }
}
