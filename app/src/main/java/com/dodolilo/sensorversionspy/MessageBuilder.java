package com.dodolilo.sensorversionspy;

import android.app.AlertDialog;
import android.content.Context;

/**
 * 一个用于提供简单的UI消息提醒方法的工具类.
 */
public class MessageBuilder {
    /**
     * 弹出提醒的消息窗口.
     * 只提供“确认”按钮，点击后不做任何事.
     * @param context 上下文
     * @param title   提示框标题
     * @param message 提示框内容
     */
    public static void showMessageWithOK(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

//    public static void showMessageFewSeconds(Context context, String titlem, String message, ) {
//
//    }

}
