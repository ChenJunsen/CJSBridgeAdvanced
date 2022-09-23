package com.cjs.android.cjsbridge.plugin;

import android.app.Activity;
import android.content.DialogInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cjs.android.cjsbridge.core.CJSBCallBack;
import com.cjs.android.cjsbridge.core.CJSBH5Plugin;

import java.util.ArrayList;

import cjsbridge_ui.dialog.MsgDialog;

/**
 * web调用原生的UI插件
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @date 2022/9/23 8:53
 */
public class CJSBUIPlugin implements CJSBH5Plugin {

    @Override
    public ArrayList<String> registeredAction() {
        ArrayList<String> actions = new ArrayList<>();
        actions.add(JSAction.SHOW_TOAST);
        actions.add(JSAction.SHOW_DIALOG);
        return actions;
    }

    @Override
    public boolean interceptAction(WebView webView, String action, JSONObject params, CJSBCallBack cjsbCallBack) {
        return false;
    }

    @Override
    public void dispatchAction(WebView webView, String action, JSONObject params, CJSBCallBack cjsbCallBack) {
        switch (action) {
            case JSAction.SHOW_TOAST:
                String msg0 = params.getString("msg");
                Toast.makeText(webView.getContext(), msg0, Toast.LENGTH_LONG).show();
//                MsgDialog.show1((Activity) webView.getContext(),msg0);
                cjsbCallBack.apply(true, "normalToast", null);
                break;
            case JSAction.SHOW_DIALOG:
                String title = "提示";
                if (params.containsKey("title")) {
                    title = params.getString("title");
                }
                String submitText = "确定";
                if (params.containsKey("submitText")) {
                    submitText = params.getString("submitText");
                }
                String cancelText = "取消";
                if (params.containsKey("cancelText")) {
                    cancelText = params.getString("cancelText");
                }
                String msg = params.getString("msg");
                int buttons = 1;
                if (params.containsKey("buttons")) {
                    buttons = params.getIntValue("buttons");
                }
                if (buttons == 2) {
                    MsgDialog.show2(
                            (Activity) webView.getContext(),
                            msg,
                            title,
                            submitText,
                            cancelText,
                            new MsgDialog.DialogListener() {

                                @Override
                                public void onSubmit(DialogInterface dialog) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("click", "submit");
                                    cjsbCallBack.apply(true, jsonObject);
                                }

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("click", "cancel");
                                    cjsbCallBack.apply(true, jsonObject);
                                }
                            }
                    );
                } else {
                    MsgDialog.show1(
                            (Activity) webView.getContext(),
                            msg, title, submitText,
                            new MsgDialog.DialogListenerSimple() {
                                @Override
                                public void onSubmit(DialogInterface dialog) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("click", "submit");
                                    cjsbCallBack.apply(true, jsonObject);
                                }
                            }
                    );
                }
                break;
        }
    }
}
