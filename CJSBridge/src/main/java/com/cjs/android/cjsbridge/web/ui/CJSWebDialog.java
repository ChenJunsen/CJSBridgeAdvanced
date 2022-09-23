package com.cjs.android.cjsbridge.web.ui;

/**
 * 用于替换网页弹窗的模板 自定义网页的alert弹窗
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @date 2022/9/22 16:15
 */
public interface CJSWebDialog {
    /**
     * 设置弹窗文字
     *
     * @param msg 弹窗文字
     */
    void setMsg(String msg);

    /**
     * 显示弹窗
     */
    void show();

    /**
     * 设置弹窗按钮点击监听器
     *
     * @param dialogClickListener 弹窗按钮单击监听器
     */
    void setDialogClickListener(CJSWebDialogClickListener dialogClickListener);
}
