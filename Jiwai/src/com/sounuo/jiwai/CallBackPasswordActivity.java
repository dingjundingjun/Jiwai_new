/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.sounuo.jiwai;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * description :
 * 
 * @version 1.0
 * @author zhoufeng
 * @createtime : 2015-2-1 上午10:40:54
 * 
 *             修改历史: 修改人 修改时间 修改内容 --------------- -------------------
 *             ----------------------------------- zhoufeng 2015-2-1 上午10:40:54
 * 
 */
public class CallBackPasswordActivity extends Activity implements
		OnClickListener {
	/**
	 * 标题
	 */
	private TextView title;
	/**
	 * 获取验证码的布局
	 */
	private LinearLayout get_code_linearlayout;
	/**
	 * 手机号码的editText组件
	 */
	private EditText back_password_username;
	/**
	 * 获取验证码组件
	 */
	private Button get_back_password_code;
	/**
	 * 提交验证码的布局
	 */
	private LinearLayout set_code_linearlayout;
	/**
	 * 验证码的editText组件
	 */
	private EditText back_password_code;
	/**
	 * 提交验证码组件
	 */
	private Button set_back_password_code;
	/**
	 * 确定修改密码的布局
	 */
	private RelativeLayout find_password_ok_linearlayout;
	/**
	 * 新密码的editText组件
	 */
	private EditText back_new_password;
	/**
	 * 重新输入新密码的editText组件
	 */
	private EditText back_new_repassword;
	/**
	 * 确定修改密码组件
	 */
	private Button find_password_ok;
	/**
	 * 返回登录组件
	 */
	private TextView back_login;
	/**
	 * handler
	 */
	private Handler handler;
	/**
	 * 解析json对象
	 */
	private JSONObject jsonObject;
	private final int PROGRESS_DIALOG = 0x112;
	private String dataJson;// json数据
	private ArrayList<NameValuePair> params;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_back_password);
		initView();

		back_login.setOnClickListener(this);
		get_back_password_code.setOnClickListener(this);
		set_back_password_code.setOnClickListener(this);
		find_password_ok.setOnClickListener(this);
	}

	/**
	 * 
	 * @author Owater
	 * @createtime 2015-1-13 下午6:04:53
	 * @Decription 初始化界面
	 * 
	 */
	public void initView() {
		title = (TextView) findViewById(R.id.activity_title);
		title.setText("找回密码");
		back_password_username = (EditText) findViewById(R.id.back_password_username);
		get_back_password_code = (Button) findViewById(R.id.get_back_password_code);
		back_password_code = (EditText) findViewById(R.id.back_password_code);
		set_back_password_code = (Button) findViewById(R.id.set_back_password_code);
		get_code_linearlayout = (LinearLayout) findViewById(R.id.get_code_linearlayout);
		set_code_linearlayout = (LinearLayout) findViewById(R.id.set_code_linearlayout);
		find_password_ok_linearlayout = (RelativeLayout) findViewById(R.id.find_password_ok_linearlayout);
		back_new_password = (EditText) findViewById(R.id.back_new_password);
		back_new_repassword = (EditText) findViewById(R.id.back_new_repassword);
		find_password_ok = (Button) findViewById(R.id.find_password_ok);
		back_login = (TextView) findViewById(R.id.back_login);
		pd = new ProgressDialog(this);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					Toast.makeText(CallBackPasswordActivity.this,
							AppConstant.NO_INTENT_TIPS, Toast.LENGTH_LONG).show();
					break;
				case 1:
					Toast.makeText(CallBackPasswordActivity.this,
							"验证码已发送,请耐心等待", Toast.LENGTH_LONG).show();
					get_code_linearlayout.setVisibility(View.GONE);
					set_code_linearlayout.setVisibility(View.VISIBLE);
					break;
				case 2:
					try {
						Toast.makeText(CallBackPasswordActivity.this,
								jsonObject.getString("msg").toString(),
								Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case 3:
					set_code_linearlayout.setVisibility(View.GONE);
					find_password_ok_linearlayout.setVisibility(View.VISIBLE);
					break;
				case 4:
					try {
						Toast.makeText(CallBackPasswordActivity.this,
								jsonObject.getString("msg").toString(),
								Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case 5:
					try {
						Toast.makeText(CallBackPasswordActivity.this,
								jsonObject.getString("msg").toString(),
								Toast.LENGTH_LONG).show();
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					startActivity(new Intent(CallBackPasswordActivity.this,RegisterLoginActivity.class));
					finish();
					break;
				case 6:
					try {
						Toast.makeText(CallBackPasswordActivity.this,
								jsonObject.getString("msg").toString(),
								Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		};
	}

	/**
	 * 
	 * 重写方法: onCreateDialog|描述:
	 * 
	 * @param id
	 * @param status
	 * @return
	 * @see android.app.Activity#onCreateDialog(int, android.os.Bundle)
	 */
	protected Dialog onCreateDialog(int id, Bundle status) {
		if (id == PROGRESS_DIALOG) {
			pd.setMessage("正在提交...");
			pd.setProgressStyle(TRIM_MEMORY_RUNNING_CRITICAL);
		}

		return pd;
	}

//	public void getCode() {
//		String phone = back_password_username.getText().toString().trim();
//		params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("phone", phone));
//		if (phone.length() == 11) {
//			/**
//			 * 访问服务端获取验证码的
//			 */
//			Thread thread = new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					dataJson = Util.request(params,
//							AppConstant.FING_PASSWORD_GET_CORD);
//					Message msg = new Message();
//					if (dataJson.length() != 0 && !dataJson.equals("0x110")) {
//						try {
//							jsonObject = new JSONObject(dataJson);
//							if (jsonObject.getBoolean("success")) {
//								msg.what = 1;
//							} else {
//								msg.what = 2;
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					} else {
//						msg.what = 0;
//					}
//					handler.sendMessage(msg);
//				}
//			});
//			thread.start();
//		} else {
//			Toast.makeText(CallBackPasswordActivity.this, "手机号码填写有误，请重新检查",
//					Toast.LENGTH_SHORT).show();
//		}
//	}

//	public void setCode() {
//		String phone = back_password_username.getText().toString().trim();
//		String code = back_password_code.getText().toString().trim();
//		params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("phone", phone));
//		params.add(new BasicNameValuePair("code", code));
//
//		Thread thread = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				dataJson = Util.request(params,
//						AppConstant.FING_PASSWORD_CHECK_CODE);
//				Message msg = new Message();
//				if (dataJson.length() != 0 && !dataJson.equals("0x110")) {
//					try {
//						jsonObject = new JSONObject(dataJson);
//						if (jsonObject.getBoolean("success")) {
//							msg.what = 3;
//						} else {
//							msg.what = 4;
//						}
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				} else {
//					msg.what = 0;
//				}
//				handler.sendMessage(msg);
//			}
//		});
//		thread.start();
//	}

	public void find_password() {
		String phone = back_password_username.getText().toString().trim();
		String new_password = back_new_password.getText().toString().trim();
		String new_repassword = back_new_repassword.getText().toString().trim();
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("password", new_password));

		if (new_password.equals("") || new_repassword.equals("")) {
			Toast.makeText(CallBackPasswordActivity.this, "密码不能为空",
					Toast.LENGTH_SHORT).show();
		}else if (new_password.length()<6) {
			Toast.makeText(CallBackPasswordActivity.this, "密码不能少于6位数",
					Toast.LENGTH_SHORT).show();
		}else{
			if (new_password.equals(new_repassword)) {
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						dataJson = Util.request(params,
								AppConstant.FING_PASSWORD);
						Message msg = new Message();
						if (dataJson.length() != 0 && !dataJson.equals("0x110")) {
							try {
								jsonObject = new JSONObject(dataJson);
								if (jsonObject.getBoolean("success")) {
									msg.what = 5;
								} else {
									msg.what = 6;
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
							msg.what = 0;
						}
						handler.sendMessage(msg);
					}
				});
				thread.start();
			}else{
				Toast.makeText(CallBackPasswordActivity.this, "输入两次密码不一致",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			// 返回按钮
			finish();
			break;
		case R.id.back_login:
			startActivity(new Intent(CallBackPasswordActivity.this,
					RegisterLoginActivity.class));
			break;
		case R.id.get_back_password_code:
			get_code_linearlayout.setVisibility(View.GONE);
			set_code_linearlayout.setVisibility(View.VISIBLE);
//			getCode();
			break;
		case R.id.set_back_password_code:
			set_code_linearlayout.setVisibility(View.GONE);
			find_password_ok_linearlayout.setVisibility(View.VISIBLE);
//			setCode();
			break;
		case R.id.find_password_ok:
			find_password();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); 
		MobclickAgent.onResume(this); 
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); 
		MobclickAgent.onPause(this);
	}
}
