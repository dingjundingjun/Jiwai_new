package com.sounuo.jiwai.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.android.agoo.net.async.AsyncHttpClient;
import org.android.agoo.net.async.AsyncHttpResponseHandler;
import org.apache.http.Header;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sounuo.jiwai.R;
import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.umeng.socialize.bean.Gender;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;

public class PersonalUtil {
	
	public static SnsAccount mSnsAccount = new SnsAccount(Build.SERIAL,Gender.MALE,"","");
	public static void savePersonInfo(Context context, PersonalInfoPojo personalInfo)
	{
		SharedPreferences preferences;
		String jsonStr = null;
		Editor prefsEditor;
		
		preferences = context.getSharedPreferences("personalinfo", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		jsonStr = JsonTools.objToGson(personalInfo);
		Log.d("savePersonInfo","jsonStr = " + jsonStr);
		prefsEditor.putString("personalinfo", jsonStr);
		prefsEditor.commit();
	}
	
	
	public static void delPersonInfo(Context context)
	{
		SharedPreferences preferences;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("personalinfo", Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		prefsEditor.clear();
		prefsEditor.commit();
	}
	
	/**
	 * 从配置中读取个人信息，包括登录名、密码、昵称、头像地址等
	 * @param context
	 * @return
	 */
	public static PersonalInfoPojo getPersonInfo(Context context)
	{
		PersonalInfoPojo personnalInfo = null;
		String personInfoStr = null;
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("personalinfo", Context.MODE_PRIVATE);
		personInfoStr = preferences.getString("personalinfo", "");
		if( !TextUtils.isEmpty(personInfoStr)){
			personnalInfo = JsonTools.GsonToObj(personInfoStr,PersonalInfoPojo.class);
		}
		return personnalInfo;
	}
	
	
	public static String getPersonInfoStr(Context context)
	{
		String personInfoStr = null;
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("personalinfo", Context.MODE_PRIVATE);
		personInfoStr = preferences.getString("personalinfo", "");
		return personInfoStr;
	}
	
	public static void login(Context context,UMSocialService socialService)
	{
		socialService.login(context, mSnsAccount, new SocializeListeners.SocializeClientListener() {
            @Override
            public void onStart() {

            }
            @Override
            public void onComplete(int arg0, SocializeEntity arg1) {
            }
        });
		mSnsAccount.setUserName("haha");
		mSnsAccount.setUsid(""+System.currentTimeMillis());
//		mSnsAccount.setAccountIconUrl(pi.getPhotoPath());
	}
	
}
