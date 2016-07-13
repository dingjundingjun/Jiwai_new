package com.sounuo.jiwai.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.sounuo.jiwai.LoginActivity;
import com.sounuo.jiwai.SettingActivity;
import com.sounuo.jiwai.updateUserInfoActivity;
import com.sounuo.jiwai.MainActivity;
import com.sounuo.jiwai.MainCatalogActivity;
import com.sounuo.jiwai.RegisterInformActivity;

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
    
    public static void enterMainCatalog(Context context) {
        Intent intent = new Intent(context, MainCatalogActivity.class);
        context.startActivity(intent);
    }

    public static void enterRegisterInform(Context context) {
        Intent intent = new Intent(context, RegisterInformActivity.class);
        context.startActivity(intent);
    }
    
    public static void enterRegisterLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    
    public static void enterEdit(Context context) {
    	Intent intent = new Intent(context, updateUserInfoActivity.class);
    	context.startActivity(intent);
    }

	public static void enterSetting(Context context) {
		// TODO Auto-generated method stub
    	Intent intent = new Intent(context, SettingActivity.class);
    	context.startActivity(intent);
	}

}
