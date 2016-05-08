package com.sounuo.jiwai.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存SharedPreferences工具类
 */
public class SharedPrefUtil {
	//应用保存SharedPreferences文件的名字
	private static final String PREFERENCES_NAME = "great_world";

	public static void putString(Context context, String key,String value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value).commit();
	}
	
	public static void putInt(Context context, String key,int value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value).commit();
	}
	
	public static void putFloat(Context context, String key,float value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putFloat(key, value).commit();
	}
	
	public static void putLong(Context context, String key,long value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong(key, value).commit();
	}
	
	public static void putBoolean(Context context, String key,boolean value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value).commit();
	}
	
	public static int getInt(Context context, String key,int defaultValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, defaultValue);
	}
	
	public static boolean getBoolean(Context context, String key,boolean defaultValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defaultValue);
	}
	
	public static float getFloat(Context context, String key,float defaultValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getFloat(key, defaultValue);
	}
	
	public static long getLong(Context context, String key,long defaultValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getLong(key, defaultValue);
	}
	
	public static String getString(Context context, String key,String defaultValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, defaultValue);
	}
}
