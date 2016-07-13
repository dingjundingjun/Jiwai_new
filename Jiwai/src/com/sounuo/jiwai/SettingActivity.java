package com.sounuo.jiwai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.utils.ActivityHelper;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.Util;

public class SettingActivity extends Activity implements OnClickListener {
	public static final String TAG = "JIWAI";
	private Button mChangePassword;
	private Button mLoginOut;
	private String phone;
	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		init();
	}

	private void init() {
		mChangePassword = (Button) findViewById(R.id.chage_password);
		mLoginOut = (Button) findViewById(R.id.login_out);
		mChangePassword.setOnClickListener(this);
		mLoginOut.setOnClickListener(this);
		PersonalInfoPojo personInfo = PersonalUtil.getPersonInfo(this);
		phone = personInfo.getPhone();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chage_password:
			startChangePasword();
			break;
		case R.id.login_out:
			startLoginOut();
			break;

		}
	}

	private void startLoginOut() {
		AlertDialog.Builder builder=new Builder(this);
		builder.setMessage("确认退出吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				JiWaiApplication.isGo=false;
				PersonalUtil.delPersonInfo(SettingActivity.this);
//				启动登录窗口，并获取到保存的密码，
				ActivityHelper.enterRegisterLogin(SettingActivity.this);
				SettingActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void startChangePasword() {
		Intent intent = new Intent();
		intent.setClass(SettingActivity.this, ChangePasswordActivity.class);
		startActivity(intent);
		// overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
	}
	
	public void doToast(Context context, String text, int duration) {

		if (mToast == null) {

			mToast = Toast.makeText(context, text, duration);

		} else {

			mToast.setText(text);

			mToast.setDuration(Toast.LENGTH_SHORT);

		}

		mToast.show();

	}
	
	
}
