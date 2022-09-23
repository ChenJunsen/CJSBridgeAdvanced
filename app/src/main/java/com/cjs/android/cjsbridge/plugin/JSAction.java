package com.cjs.android.cjsbridge.plugin;

/**
 *
 * Web与原生交互的动作指令集合
 * @email chenjunsen@outlook.com
 * @author JasonChen
 * @date 2022/9/23 8:52
*/
public interface JSAction {
    /**
     * 显示原生的Toast
     */
    String SHOW_TOAST = "toast";
    /**
     * 调用原生的弹窗
     */
    String SHOW_DIALOG = "dialog";
}
