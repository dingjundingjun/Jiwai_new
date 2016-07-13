package com.sounuo.jiwai;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.utils.ActivityHelper;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.CommonUtil;
import com.sounuo.jiwai.utils.JsonTools;
import com.sounuo.jiwai.utils.MD5;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.StatusBarUtil;
import com.sounuo.jiwai.utils.Util;

public class RegisterActivity extends Activity implements OnClickListener {

	private static final int MSG_UPDATE_TIME = 2;

	// 验证码输入框
	private EditText registerVerCodeEt;

	private EditText registerPhoneEt;
	
	// 获取验证码按钮
	private Button get_regirster_code;

	// 密码输入框
	private EditText registerPasswordEt;

//	// 是否显示密码
//	private CheckBox showPasswordCb;

	// 注册按钮
	private Button nextBtn;



	private ProgressBar mLoginProgressBar;

	/**
	 * 60秒的计时数
	 */
	private int recLen = 60;

	private TextView mRegisterTitle;

	private boolean isFillVerCode;

	private boolean isFillPwd;
	
	private boolean isFillPhone;

	private boolean isFillUserName;

	private String phone;

	private String password;

	private String vercode;

	private EditText registerUserNameEt;

	private String userName;


	protected static final String TAG = "JIWAII";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		get_regirster_code = (Button) findViewById(R.id.get_regirster_code);
		get_regirster_code.setTextColor(getResources().getColor(R.color.text_light));
		init();

	}
	
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		setStatusBar();
	}
	
    protected void setStatusBar() {
            StatusBarUtil.setTranslucent(this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }

	/**
	 * 初始化控件
	 */
	private void init() {
		registerVerCodeEt = (EditText) findViewById(R.id.register_ver_code_et);
		registerPhoneEt = (EditText) findViewById(R.id.register_phone_et);
		registerUserNameEt = (EditText) findViewById(R.id.register_username_et);
		registerPasswordEt = (EditText) findViewById(R.id.register_password_et);
//		showPasswordCb = (CheckBox) findViewById(R.id.register_show_password);
		mLoginProgressBar = (ProgressBar) findViewById(R.id.login_progressbar);
		nextBtn = (Button) findViewById(R.id.btn_next);
		mRegisterTitle = (TextView) findViewById(R.id.activity_title);
		mRegisterTitle.setText("注册账号");
		// 屏蔽进入下一界面的按钮
//		showPasswordCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView,
//							boolean isChecked) {
//						// TODO Auto-generated method stub
//						if (isChecked) {
//							// 设置EditText的密码为可见的
//							registerPasswordEt
//									.setTransformationMethod(HideReturnsTransformationMethod
//											.getInstance());
//						} else {
//							// 设置密码为隐藏的
//							registerPasswordEt
//									.setTransformationMethod(PasswordTransformationMethod
//											.getInstance());
//						}
//						// 光标在最后显示
//						registerPasswordEt.setSelection(registerPasswordEt
//								.length());
//
//					}
//				});
		
		
		registerPasswordEt.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){  
					//处理事件
					register();
					return true;
				}
				return false;
			}
		});
		
		get_regirster_code.setOnClickListener(this);
		nextBtn.setOnClickListener(this);

		// 监听输入字数，用以改变登陆按钮状态
		registerPasswordEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() >= 6) {
					isFillPwd = true;
				} else {
					isFillPwd = false;
				}
			}
		});
		
		// 监听输入字数，用以改变登陆按钮状态
		registerPhoneEt.addTextChangedListener(new TextWatcher() {



			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 11) {
					isFillPhone = true;
				} else {
					isFillPhone = false;
				}
				checkFill();
			}


		});
		
		

		registerUserNameEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					isFillUserName = true;
				} else {
					isFillUserName = false;
				}
			}
		});

		registerVerCodeEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 4) {
					isFillVerCode = true;
				} else {
					isFillVerCode = false;
				}
			}
		});
	}


	protected void checkFill() {
		// TODO Auto-generated method stub
		if (isFillPhone) {
			get_regirster_code.setClickable(true);
			get_regirster_code.setTextColor(getResources().getColor(R.color.text_light));
		}else{
			get_regirster_code.setClickable(false);
			get_regirster_code.setTextColor(getResources().getColor(R.color.text_hint));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_regirster_code:
			getCheckCode();
			break;
		case R.id.btn_next:
			register();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	public static void hideSoftInput(Context context) {
		View view = ((Activity) context).getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}



	private void register() {
		userName = registerUserNameEt.getText().toString().trim();
		phone = registerPhoneEt.getText().toString().trim();
		password = registerPasswordEt.getText().toString().trim();
		vercode = registerVerCodeEt.getText().toString().trim();

		hideSoftInput(this);

		if (CommonUtil.isEmptyString(phone) || phone.length() < 11|| !CommonUtil.checkMobile(phone)) {
			doToast(this, "请输入正确手机号码，请检查...", Toast.LENGTH_SHORT);
			return;
		}

		if (CommonUtil.isEmptyString(password) || password.length() < 6) {
			doToast(this, "请至少输入6位密码！", Toast.LENGTH_SHORT);
			return;
		}

		if (CommonUtil.isEmptyString(vercode) || vercode.length() != 4) {
			doToast(this, "请输入4位验证码！", Toast.LENGTH_SHORT);
			return;
		}

		if (!Util.checkWifiConnected(this)) {
			doToast(this, "没有网络", Toast.LENGTH_SHORT);
			return;
		}

		UserRegisterTask registerTask = new UserRegisterTask(this,password, phone,vercode);
		registerTask.execute();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private Toast mToast;
	
	public void doToast(Context context, String text, int duration) {

		if (mToast == null) {

			mToast = Toast.makeText(context, text, duration);

		} else {

			mToast.setText(text);

			mToast.setDuration(Toast.LENGTH_SHORT);

		}

		mToast.show();

	}

	public class UserRegisterTask extends AsyncTask<String, Void, String> {
		Context context = null;
		String password = null;
		String phone = null;
		String vercode = null;

		public UserRegisterTask(Context papramContext,String papramPassword, String paramPhone,String paramVercode) {
			password = papramPassword;
			phone = paramPhone;
			vercode = paramVercode;
			context=papramContext;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showProgress(true);
		}

		@Override
		protected String doInBackground(String... strings) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// 进行简单的MD5加密
			params.add(new BasicNameValuePair("password", MD5.getMD5(password)));
			params.add(new BasicNameValuePair("phone", phone));
			params.add(new BasicNameValuePair("nickName", userName));
			params.add(new BasicNameValuePair("code", vercode));
			String url = AppConstant.CHECK_REG_CODE;
			Log.d(TAG, params.toString());
			Log.d(TAG, url);
			return Util.AccoutRequest(params, url, null);
		}

		@Override
		protected void onPostExecute(String code) {
			showProgress(false);

			if (code == null) {
				doToast(context, "网络异常，请检查网络", Toast.LENGTH_SHORT);
				return;
			}
			if (code.equals("200")) {
				doToast(context, "注册成功", Toast.LENGTH_SHORT);
				UserLoginTask loginTask=new UserLoginTask();
				loginTask.execute();
				
			} else if (code.equals("500")) {
				doToast(context, "验证码错误！请再次确认！",Toast.LENGTH_SHORT);
			} else if (code.equals("404")) {
				doToast(context, "该手机号码已被抢先注册了！",Toast.LENGTH_SHORT);
			}
		}

		@Override
		protected void onCancelled() {
			showProgress(false);
		}
	}

	private void showProgress(boolean show) {
		if (show) {
			mLoginProgressBar.setVisibility(View.VISIBLE);
		} else {
			mLoginProgressBar.setVisibility(View.GONE);
		}
	}

	public static void savePersonInfo(Context context,PersonalInfoPojo personalInfo) {
		SharedPreferences preferences;
		String jsonStr = null;
		Editor prefsEditor;
		preferences = context.getSharedPreferences("personalinfo",Context.MODE_PRIVATE);
		prefsEditor = preferences.edit();
		jsonStr = JsonTools.objToGson(personalInfo);
		Log.d("savePersonInfo", "jsonStr = " + jsonStr);
		prefsEditor.putString("personalinfo", jsonStr);
		prefsEditor.commit();
	}

	/**
	 * 点击按钮获取验证码
	 */
	public void getCheckCode() {
		phone = registerPhoneEt.getText().toString().trim();
		
		if (!Util.checkWifiConnected(this)) {
			doToast(this, "没有网络", Toast.LENGTH_SHORT);
			return;
		}

		String mId = Util.getMid(this);

		long timestamp = System.currentTimeMillis();

		String sign = Util.getSign(mId, phone, timestamp + "");

		System.out.println("url  ::  " + AppConstant.CHECK_PHONE);
		if (CommonUtil.isEmptyString(phone) || phone.length() < 11
				|| !CommonUtil.checkMobile(phone)) {
			System.out.println("JIWAI  ==  请输入正确手机号码，请检查...");
			doToast(this, "请输入正确手机号码，请检查...", Toast.LENGTH_SHORT);
			return;
		}

		GetVerCodeTask getTassk = new GetVerCodeTask(phone, sign, mId,timestamp + "");
		getTassk.execute();

	}

	private GetVerCodeHandler mGetVerCodeHandler = new GetVerCodeHandler(this);

	public static class GetVerCodeHandler extends Handler {

		private final WeakReference<RegisterActivity> mActivity;

		public GetVerCodeHandler(RegisterActivity activity) {
			mActivity = new WeakReference<RegisterActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			final RegisterActivity registerActivity = mActivity.get();
			if (registerActivity == null) {
				return;
			}
			switch (msg.what) {

			case MSG_UPDATE_TIME:
				// 更新get_regirster_code显示
				registerActivity.recLen--;
				TextView get_code = registerActivity.get_regirster_code;
				if (registerActivity.recLen == 0) {
					get_code.setText("获取验证码");
					get_code.setClickable(true);
					get_code.setTextColor(registerActivity.getResources()
							.getColor(R.color.text_light));
					registerActivity.recLen = 60;
				} else {
					get_code.setTextColor(registerActivity.getResources()
							.getColor(R.color.text_hint));
					get_code.setText(registerActivity.recLen + "秒后重发");
				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 获取验证码的task
	 */
	private class GetVerCodeTask extends AsyncTask<String, Integer, String> {

		String phone = null;

		String sign = null;

		String deviceId = null;

		String timestamp = null;

		public GetVerCodeTask(String paramPhone, String paramSign,
				String paramDeviceId, String paramTimestamp) {

			phone = paramPhone;
			sign = paramSign;
			deviceId = paramDeviceId;
			timestamp = paramTimestamp;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			get_regirster_code.setClickable(false);
			get_regirster_code.setTextColor(getResources().getColor(
					R.color.text_hint));
			showProgress(true);
		}

		@Override
		protected String doInBackground(String... strings) {

			Log.e(TAG, "doInBackground phone:" + phone);

			return checkRegInfo(phone, sign, deviceId, timestamp);
		}

		@Override
		protected void onPostExecute(String code) {
			showProgress(false);
			if (code == null) {
				doToast(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT);
				return;
			}
			Log.e("JIWAI", "打印看看返回码   :  " + code);
			if (code.equals("200")) {
				Log.d(TAG, "onPostExecute  验证成功");
				doToast(RegisterActivity.this, "获取验证码成功", Toast.LENGTH_SHORT);
				/**
				 * 验证码计时60秒的线程
				 */
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						for (int i = 0; i < 60; i++) {
							try {
								Thread.sleep(1000); // sleep 1000ms
								Message message = new Message();
								message.what = MSG_UPDATE_TIME;
								mGetVerCodeHandler.sendMessage(message);
							} catch (Exception e) {
							}
						}
					}
				});
				thread.start();
			} else {
				Log.e(TAG, "onPostExecute 验证失败");
				doToast(RegisterActivity.this, "该号码已经注册", Toast.LENGTH_SHORT);
			}
			super.onPostExecute(code);
		}
	}

	/**
	 * 获取验证码
	 * 
	 * @param Phone
	 * @param sign
	 * @param timestamp
	 * @param deviceId
	 * @return
	 */
	public String checkRegInfo(String Phone, String sign, String deviceId,
			String timestamp) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", phone));
		String url = AppConstant.CHECK_PHONE;
		Log.d(TAG, "getLockpaperSSLHttpPost url: " + url);
		Log.d(TAG, "getLockpaperSSLHttpPost params: " + params.toString());
		HashMap<String, String> headers = Util.getAccountHeaders(this,"", phone);
		return Util.AccoutRequest(params, url, headers);
	}
	
	public class UserLoginTask extends AsyncTask<String, Void, String> {

		private PersonalInfoPojo mPersonInfo;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showProgress(true);
		}

		@Override
		protected String doInBackground(String... strings) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// 进行简单的MD5加密
			params.add(new BasicNameValuePair("password", MD5.getMD5(password)));
			params.add(new BasicNameValuePair("userName", phone));
			HttpPost httpPost = new HttpPost(AppConstant.LOGIN_URL);
			HttpResponse httpResponse = null;
			try {
				// 设置httpPost请求参数
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				int resultCode = httpResponse.getStatusLine().getStatusCode();
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					/* 读返回数据 */
					if (httpResponse.getEntity() == null) {
						return null;
					}
					String strResult = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
					return strResult;
				} else {
					return null;
				}
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	            return null;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
		}

		@Override
		protected void onPostExecute(String strResult) {
			showProgress(false);
			if (strResult == null) {
				doToast(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT);
				return;
			}
			Log.e("JIWAI", "打印看看返回码   :  " + strResult);
			
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(strResult);
				String code= jsonObject.getString("code");
				if (code.equals("200")) {
					Log.d(TAG, "onPostExecute  登录成功");
					System.out.println("JIWAII  ++  登录成功！");
					String data=jsonObject.getString("data");;
					mPersonInfo = JsonTools.GsonToObj(data, PersonalInfoPojo.class);
					PersonalUtil.savePersonInfo(RegisterActivity.this, mPersonInfo);
//					登录成功后应该将用户的登录密码用MD5保存，退出登录后返回再恢复
					doToast(RegisterActivity.this, "登录成功！", Toast.LENGTH_SHORT);
					ActivityHelper.enterRegisterInform(RegisterActivity.this);
				} else {
					System.out.println("JIWAII  ++   验证失败"+code);
					Log.e(TAG, "onPostExecute 验证失败");
					doToast(RegisterActivity.this, "帐号密码不正确！", Toast.LENGTH_SHORT);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		protected void onCancelled() {
			showProgress(false);
		}
	}


}
