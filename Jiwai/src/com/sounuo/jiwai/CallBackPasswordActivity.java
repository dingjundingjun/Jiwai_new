package com.sounuo.jiwai;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
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
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.sounuo.jiwai.RegisterActivity.UserRegisterTask;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.CommonUtil;
import com.sounuo.jiwai.utils.MD5;
import com.sounuo.jiwai.utils.StatusBarUtil;
import com.sounuo.jiwai.utils.Util;
import com.umeng.analytics.MobclickAgent;

public class CallBackPasswordActivity extends Activity implements OnClickListener {
	private static final String TAG = "JIWAII";
	private boolean isFillPwd;
	private boolean isFillUserName;
	private boolean isFillVerCode;
	
	protected static final int MSG_UPDATE_TIME = 0;
	/**
	 * 60秒的计时数
	 */
	private int recLen = 60;
//	/**
//	 * 标题
//	 */
//	private TextView title;
	
	private ProgressBar mLoginProgressBar;
	/**
	 * 手机号码的editText组件
	 */
	private EditText back_password_username;
	/**
	 * 获取验证码组件
	 */
	private Button get_back_password_code;
	/**
	 * 验证码的editText组件
	 */
	private EditText back_password_code;
	/**
	 * 重新输入新密码的editText组件
	 */
	private EditText back_new_repassword;
	/**
	 * 确定修改密码组件
	 */
	private Button find_password_ok;
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
	private Toast mToast;
	private String phone;
	private String password;
	private String vercode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_back_password);
		initView();
		get_back_password_code.setOnClickListener(this);
		find_password_ok.setOnClickListener(this);
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
	 * 
	 * @author Owater
	 * @createtime 2015-1-13 下午6:04:53
	 * @Decription 初始化界面
	 * 
	 */
	public void initView() {
		TextView title = (TextView) findViewById(R.id.activity_title);
		title.setText("找回密码");
		get_back_password_code = (Button) findViewById(R.id.get_back_password_code);
		back_password_username = (EditText) findViewById(R.id.back_password_username);
		back_password_code = (EditText) findViewById(R.id.back_password_code);
		back_new_repassword = (EditText) findViewById(R.id.back_new_password);
		find_password_ok = (Button) findViewById(R.id.find_password_ok);
		mLoginProgressBar = (ProgressBar) findViewById(R.id.changing_progressbar);
//		find_password_ok.setEnabled(false);
		get_back_password_code.setOnClickListener(this);
		find_password_ok.setOnClickListener(this);

		// 监听输入字数，用以改变登陆按钮状态
		back_new_repassword.addTextChangedListener(new TextWatcher() {



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
				checkIsFillAll();
			}
		});
		
		back_password_username.addTextChangedListener(new TextWatcher() {



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
					isFillUserName = true;
				} else {
					isFillUserName = false;
				}
				checkIsFillAll();
			}
		});


		back_password_code.addTextChangedListener(new TextWatcher() {



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
				checkIsFillAll();
			}
		});
	}
	
	private void checkIsFillAll() {

//		if (isFillPwd && isFillVerCode && isFillUserName) {
//			find_password_ok.setEnabled(true);
//		} else {
//			find_password_ok.setEnabled(false);
//		}
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			// 返回按钮
			finish();
			break;
		case R.id.get_back_password_code:
			getCheckCode();
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
	
	public void doToast(Context context, String text, int duration) {

		if (mToast == null) {

			mToast = Toast.makeText(context, text, duration);

		} else {

			mToast.setText(text);

			mToast.setDuration(Toast.LENGTH_SHORT);

		}

		mToast.show();

	}
	
	public static void hideSoftInput(Context context) {
		View view = ((Activity) context).getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	
	private void find_password() {
		
		phone = back_password_username.getText().toString().trim();
		password = back_new_repassword.getText().toString().trim();
		vercode = back_password_code.getText().toString().trim();
		
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
		String mid = Util.getMid(this);
		
		long timestamp=System.currentTimeMillis();
		
		String sign = Util.getSign(mid, phone,timestamp+"");
		
		CallBackPasswordTask registerTask = new CallBackPasswordTask(password, phone, vercode, timestamp+"", mid, sign);
		registerTask.execute();

	}
	
	public class CallBackPasswordTask extends AsyncTask<String, Void, String> {

		String password = null;
		String phone = null;
		String vercode = null;
		String timestamp = null;
		String mid = null;
		String sign = null;

		public CallBackPasswordTask(String papramPassword, String paramPhone,String paramVercode, String paramTimestamp, String paramMid, String paramSign) {
			password = papramPassword;
			phone = paramPhone;
			vercode = paramVercode;
			timestamp = paramTimestamp;
			mid = paramMid;
			sign = paramSign;
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
			params.add(new BasicNameValuePair("code", vercode));
			Log.d(TAG, params.toString());
			Log.d(TAG, AppConstant.CHECK_REG_CODE);
			
			System.out.println("JIWAII  ++   "+params.toString());
			System.out.println("JIWAII  ++   "+AppConstant.CHANGE_PASSWORD_BY_MSG_CODE);
			
		    HttpPost httpPost = new HttpPost(AppConstant.CHANGE_PASSWORD_BY_MSG_CODE);
			HttpResponse httpResponse = null;
	        try {
	            Header header;
				// 设置httpPost请求参数
	        	httpPost.addHeader("token","");
	        	httpPost.addHeader("timestamp",timestamp);
	        	httpPost.addHeader("mid",mid);
	        	httpPost.addHeader("sign",sign);
	            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	            httpResponse = new DefaultHttpClient().execute(httpPost);
	            int resultCode = httpResponse.getStatusLine().getStatusCode();
	            Log.d(TAG, "返回码是   " + resultCode);
	            System.out.println("JIWAII  ..   返回码是   " + resultCode);
	            if (resultCode == 200) {
					/* 读返回数据 */
					String strResult = EntityUtils.toString(httpResponse.getEntity());
					System.out.println("JIWAII  ..   "+strResult);
					JSONObject jsonObject = new JSONObject(strResult);
					System.out.println("JIWAII  ..   "+jsonObject.getString("code"));
					return jsonObject.getString("code");
	            } else {
					System.out.println("JIWAII  链接失败.........");
					Log.d(TAG, "getLockpaperSSLHttpPost resultCode: " + resultCode);
					return null;
	            }
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String code) {
			showProgress(false);
			if (code == null) {
				doToast(CallBackPasswordActivity.this, "网络异常，请检查网络", Toast.LENGTH_SHORT);
				return;
			}
			if (code.equals("200")) {
				doToast(CallBackPasswordActivity.this, "重设密码成功", Toast.LENGTH_SHORT);
			} else if (code.equals("404")) {
				doToast(CallBackPasswordActivity.this, "验证码错误！请再次确认！",Toast.LENGTH_SHORT);
			} 
		}

		@Override
		protected void onCancelled() {
			showProgress(false);
		}
	}

	
	
	/**
	 * 点击按钮获取验证码
	 */
	public void getCheckCode() {
		phone = back_password_username.getText().toString().trim();
		if (!Util.checkWifiConnected(this)) {
			doToast(this, "没有网络", Toast.LENGTH_SHORT);
			return;
		}
		
		String mId = Util.getMid(this);
		
		long timestamp=System.currentTimeMillis();
		
		String sign = Util.getSign(mId, phone,timestamp+"");

		System.out.println("url  ::  " + AppConstant.CHECK_PHONE);
		if (CommonUtil.isEmptyString(phone) || phone.length() < 11|| !CommonUtil.checkMobile(phone)) {
			System.out.println("JIWAI  ==  请输入正确手机号码，请检查...");
			doToast(this, "请输入正确手机号码，请检查...", Toast.LENGTH_SHORT);
			return;
		}

		GetVerCodeTask getTassk = new GetVerCodeTask(phone, sign,mId,timestamp+"");
		getTassk.execute();



	}

	private GetVerCodeHandler mGetVerCodeHandler = new GetVerCodeHandler(this);


	public static class GetVerCodeHandler extends Handler {

		private final WeakReference<CallBackPasswordActivity> mActivity;

		public GetVerCodeHandler(CallBackPasswordActivity activity) {
			mActivity = new WeakReference<CallBackPasswordActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			final CallBackPasswordActivity callBackPasswordActivity = mActivity.get();
			if (callBackPasswordActivity == null) {
				return;
			}
			switch (msg.what) {

			case MSG_UPDATE_TIME:
				// 更新get_regirster_code显示
				callBackPasswordActivity.recLen--;
				Button get_code = callBackPasswordActivity.get_back_password_code;
				if (callBackPasswordActivity.recLen == 0) {
					get_code.setText("获取验证码");
					get_code.setClickable(true);
					get_code.setTextColor(callBackPasswordActivity.getResources().getColor(R.color.text_light));
					callBackPasswordActivity.recLen = 60;
				} else {
					get_code.setTextColor(callBackPasswordActivity.getResources().getColor(R.color.text_hint));
					get_code.setText(callBackPasswordActivity.recLen + "秒后重发");
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

		public GetVerCodeTask( String paramPhone,String paramSign, String paramDeviceId, String paramTimestamp) {

			phone = paramPhone;
			sign = paramSign;
			deviceId = paramDeviceId;
			timestamp = paramTimestamp;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			get_back_password_code.setClickable(false);
			get_back_password_code.setTextColor(getResources().getColor(R.color.text_hint));
			showProgress(true);
		}

		@Override
		protected String doInBackground(String... strings) {

			Log.e(TAG, "doInBackground phone:" + phone);

			return checkRegInfo( phone, sign,deviceId,timestamp);
		}

		@Override
		protected void onPostExecute(String code) {
			showProgress(false);
			if (code == null) {
				doToast(CallBackPasswordActivity.this, "网络异常", Toast.LENGTH_SHORT);
				return;
			}
			Log.e("JIWAI", "打印看看返回码   :  " + code);
			if (code.equals("200")) {
				Log.d(TAG, "onPostExecute  验证成功");
				doToast(CallBackPasswordActivity.this, "获取验证码成功", Toast.LENGTH_SHORT);
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
				doToast(CallBackPasswordActivity.this, "不存在这个手机号，请检查", Toast.LENGTH_SHORT);
			}
			super.onPostExecute(code);
		}
	}
	
	private void showProgress(boolean show) {
		if (show) {
			mLoginProgressBar.setVisibility(View.VISIBLE);
		} else {
			mLoginProgressBar.setVisibility(View.GONE);
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
	public String checkRegInfo( String Phone, String sign, String deviceId, String timestamp) {
		HttpPost httpPost = new HttpPost(AppConstant.FORGET_PASSWORD);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", phone));
		System.out.println("JIWAII url: " + AppConstant.FORGET_PASSWORD);
		System.out.println("JIWAII params: " + params.toString());
		Log.d(TAG, "getLockpaperSSLHttpPost url: " + AppConstant.FORGET_PASSWORD);
		Log.d(TAG, "getLockpaperSSLHttpPost params: " + params.toString());

		HttpResponse httpResponse = null;
		try {
			// 设置httpPost请求参数
        	httpPost.addHeader("token","");
        	httpPost.addHeader("timestamp",timestamp);
        	httpPost.addHeader("mid",deviceId);
        	httpPost.addHeader("sign",sign);
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			int resultCode = httpResponse.getStatusLine().getStatusCode();

			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				if (httpResponse.getEntity() == null) {
					System.out.println("JIWAI  ++++   空的");
					Log.e("JIWAI", "JIWAI  ++++    空的");
					return null;
				}
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("JIWAI  ++++   " + strResult);
				Log.e("JIWAI", "JIWAI  ++++   " + strResult);
				JSONObject jsonObject = new JSONObject(strResult);
				return jsonObject.getString("code");
			} else {
				System.out.println("链接失败........."+ httpResponse.getStatusLine().getStatusCode());
				Log.e("JIWAI", "链接失败.........");
				return null;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
