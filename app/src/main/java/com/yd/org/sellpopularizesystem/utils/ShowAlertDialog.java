package com.yd.org.sellpopularizesystem.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by Administrator on 2016/2/23.
 * <p>
 * 集中弹出框提示
 */
public class ShowAlertDialog {
    /**
     * 含有标题和内容的对话框
     **/
    public static AlertDialog showAlertDialog(Activity activity, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle(title)
                .setMessage(message).show();
        return alertDialog;
    }

    /**
     * 含有标题、内容、两个按钮的对话框
     **/
    public static AlertDialog showAlertDialog(Activity activity, String title, String message,
                                              String positiveText,
                                              DialogInterface.OnClickListener onPositiveClickListener,
                                              String negativeText,
                                              DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();
        return alertDialog;
    }

    /**
     * 含有标题、内容、图标、两个按钮的对话框
     **/
    public static AlertDialog showAlertDialog(Activity activity, String title, String message,
                                              int icon, String positiveText,
                                              DialogInterface.OnClickListener onPositiveClickListener,
                                              String negativeText,
                                              DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle(title)
                .setMessage(message).setIcon(icon)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();
        return alertDialog;
    }

}
