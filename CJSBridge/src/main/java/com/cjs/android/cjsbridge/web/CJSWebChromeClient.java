package com.cjs.android.cjsbridge.web;

import android.app.Activity;
import android.content.DialogInterface;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.cjs.android.cjsbridge.common.tools.L;
import com.cjs.android.cjsbridge.dispatch.CJSActionDispatcher;
import com.cjs.android.cjsbridge.web.ui.CJSWebDialog;
import com.cjs.android.cjsbridge.web.ui.CJSWebDialogClickListener;

/**
 * CJSWebChromeClient
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @createTime 2020/9/16 0016 14:45
 */
public class CJSWebChromeClient extends WebChromeClient {
    private static final String H5ConTag = "H5Console";
    private static final String H5AlertTag = "H5Alert";
    private static final String H5ConfirmTag = "H5Confirm";

    Activity activity;

    public boolean enableH5ConsoleLog = true;
    public boolean enableH5AlertLog = true;
    public boolean enableH5PromptLog = true;
    public boolean enableH5ConfirmLog = true;

    private boolean replaceWithNativeAlert = true;
    private CJSActionDispatcher cjsActionDispatcher;

    private CJSWebDialog dialog;

    public CJSWebChromeClient(Activity activity) {
        this.activity = activity;
    }

    /**
     * 拦截处理H5端的console
     *
     * @param consoleMessage
     * @return
     */
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (enableH5ConsoleLog) {
            String logFmt = "%2$s[lineNum:%1$s]-%3$s";//转换成logCat的日志格式
            ConsoleMessage.MessageLevel level = consoleMessage.messageLevel();
            int lineNumber = consoleMessage.lineNumber();
            String msg = consoleMessage.message();
            switch (level) {
                case WARNING:
                    L.w(H5ConTag, String.format(logFmt, lineNumber, level, msg));
                    break;
                case ERROR:
                    L.e(H5ConTag, String.format(logFmt, lineNumber, level, msg));
                    break;
                case TIP:
                    L.i(H5ConTag, String.format(logFmt, lineNumber, level, msg));
                    break;
                default:
                    L.d(H5ConTag, String.format(logFmt, lineNumber, level, msg));
                    break;
            }
        }
        return super.onConsoleMessage(consoleMessage);
    }

    /**
     * 拦截H5的alert弹窗
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        if (enableH5AlertLog) {
            String fmt = "[源:%1$s";
            String fmt2 = "msg:%1$s]";
            L.d(H5AlertTag, String.format(fmt, url));
            L.d(H5AlertTag, String.format(fmt2, message));
        }
        if (dialog != null) {
            dialog.setMsg(message);
            dialog.setDialogClickListener(new CJSWebDialogClickListener() {
                @Override
                public void onConfirm() {
                    result.confirm();
                }
            });
            dialog.show();
            return true;
        } else {
            return super.onJsAlert(view, url, message, result);
        }
    }


    /**
     * 拦截H5的confirm弹窗
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        if (enableH5ConfirmLog) {
            String fmt = "[源:%1$s";
            String fmt2 = "msg:%1$s]";
            L.d(H5ConfirmTag, String.format(fmt, url));
            L.d(H5ConfirmTag, String.format(fmt2, message));
        }
        return super.onJsConfirm(view, url, message, result);
    }
}
