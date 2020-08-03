package com.liuge.ximalaya.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * FileName: ToastUtil
 * Author: LiuGe
 * Date: 2020/7/29 18:15
 * Description: Toast工具类
 */
public class ToastUtil {

    public static void showShort(Context context, CharSequence message) {

        Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);

        toast.setText(message);

        toast.show();

    }

    public static void showLong(Context context, CharSequence message) {

        Toast toast = Toast.makeText(context, null, Toast.LENGTH_LONG);

        toast.setText(message);

        toast.show();

    }
}
