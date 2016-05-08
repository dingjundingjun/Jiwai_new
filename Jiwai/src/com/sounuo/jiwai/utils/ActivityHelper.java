package com.sounuo.jiwai.utils;

import android.content.Context;
import android.content.Intent;

import com.sounuo.jiwai.MainActivity;

/**
 * 启动Activity的公共类
 */
public class ActivityHelper {

    private ActivityHelper() {

    }

    public static void enterHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    public static void enterMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

//    public static void enterGuidActivity(Context context) {
//        Intent intent = new Intent(context, GuideActivity.class);
//        context.startActivity(intent);
//    }

}