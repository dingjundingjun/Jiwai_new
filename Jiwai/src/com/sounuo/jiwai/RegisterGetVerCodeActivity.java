package com.sounuo.jiwai;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.CommonUtil;
import com.sounuo.jiwai.utils.MD5;
import com.sounuo.jiwai.utils.SharedPrefUtil;
import com.sounuo.jiwai.utils.Util;

public class RegisterGetVerCodeActivity extends Activity implements OnClickListener {

	private static final String TAG = "JIWAI";

	// 手机号输入框
	private EditText getCodePhoneEt;

	// 注册按钮
	private Button nextBtn;

	private TextView mRegisterTitle;

	private boolean isFillPhone;


	private String phone;
	
	private Toast mToast;
	
	private ProgressBar mLoginProgressBar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_get_code);
		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		getCodePhoneEt = (EditText) findViewById(R.id.get_code_phone_et);
		mLoginProgressBar = (ProgressBar) findViewById(R.id.login_progressbar);
		nextBtn = (Button) findViewById(R.id.btn_next);
		mRegisterTitle = (TextView) findViewById(R.id.activity_title);
		mRegisterTitle.setText("手机注册");
		nextBtn.setEnabled(false);
		nextBtn.setOnClickListener(this);
		getCodePhoneEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 11) {
					isFillPhone = true;
				} else {
					isFillPhone = false;
				}

				checkIsFillAll();
			}
		});
	}

	private void checkIsFillAll() {
		if (isFillPhone ) {
			nextBtn.setEnabled(true);
		} else {
			nextBtn.setEnabled(false);
		}
	}
	
	

	@Override
	public void onClick(View v) {
		String phoneNums = getCodePhoneEt.getText().toString();
		switch (v.getId()) {
		case R.id.btn_next:
			getCheckCode();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	public static void hideSoftInput(Context context) {
		View view = ((Activity) context).getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
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



	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	private void showProgress(boolean show) {
		if (show) {
			mLoginProgressBar.setVisibility(View.VISIBLE);
		} else {
			mLoginProgressBar.setVisibility(View.GONE);
		}
	}


	/**
	 * 点击按钮获取验证码
	 */
	public void getCheckCode() {

		phone = getCodePhoneEt.getText().toString().trim();
		
		TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		
		long timestamp=System.currentTimeMillis();
		
		
		String sign=deviceId+timestamp+"sounuo"+phone;
		System.out.println("JIWAII  ++   MD5 前 " + sign);

		sign = MD5.getMD5(sign);

		System.out.println("JIWAII  ++   MD5 后 " + sign);

		System.out.println("url  ::  " + AppConstant.CHECK_PHONE);
		
		if (CommonUtil.isEmptyString(phone) || phone.length() < 11|| !CommonUtil.checkMobile(phone)) {
			System.out.println("JIWAI  ==  请输入正确手机号码，请检查...");
			doToast(this, "请输入正确手机号码，请检查...", Toast.LENGTH_SHORT);
			return;
		}
		
		if (!Util.checkWifiConnected(this)) {
			doToast(this, "没有网络", Toast.LENGTH_SHORT);
			return;
		}

		GetVerCodeTask getTassk = new GetVerCodeTask(phone, sign,deviceId,timestamp+"");
		getTassk.execute();

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
				doToast(RegisterGetVerCodeActivity.this, "网络异常", Toast.LENGTH_SHORT);
				return;
			}
			Log.e("JIWAI", "打印看看返回码   :  " + code);
			if (code.equals("200")) {
				Log.d(TAG, "onPostExecute  验证成功");
				System.out.println("JIWAII  ++   验证成功");
				doToast(RegisterGetVerCodeActivity.this, "获取验证码成功", Toast.LENGTH_SHORT);
				long currentTimeMillis = System.currentTimeMillis();
				SharedPrefUtil.putLong(RegisterGetVerCodeActivity.this, AppConstant.LAST_GET_CODE_TIME, currentTimeMillis);
				toRegisterActivity();
				finish();
			} else {
				System.out.println("JIWAII  ++   验证失败"+code);
				Log.e(TAG, "onPostExecute 验证失败");
				doToast(RegisterGetVerCodeActivity.this, "该号码已经注册", Toast.LENGTH_SHORT);
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
	public String checkRegInfo( String Phone, String sign, String deviceId, String timestamp) {
		HttpPost httpPost = new HttpPost(AppConstant.CHECK_PHONE);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", Phone));
		params.add(new BasicNameValuePair("mid", deviceId));
		params.add(new BasicNameValuePair("timestamp", timestamp));
		params.add(new BasicNameValuePair("sign", sign));

		System.out.println("JIWAII  ++   "+params.toString());
		System.out.println("JIWAII  ++   "+AppConstant.CHECK_PHONE);
		
		Log.d(TAG, "getLockpaperSSLHttpPost url: " + AppConstant.CHECK_PHONE);
		Log.d(TAG, "getLockpaperSSLHttpPost params: " + params.toString());

		HttpResponse httpResponse = null;
		try {
			// 设置httpPost请求参数
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			int resultCode = httpResponse.getStatusLine().getStatusCode();

			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				if (httpResponse.getEntity() == null) {
					System.out.println("JIWAII  ++++   空的");
					Log.e("JIWAI", "JIWAII  ++++    空的");
					return null;
				}
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("JIWAII  ++++   " + strResult);
				Log.e("JIWAI", "JIWAII  ++++   " + strResult);
				JSONObject jsonObject = new JSONObject(strResult);
				return jsonObject.getString("code");
			} else {
				System.out.println("JIWAII  链接失败........."+ httpResponse.getStatusLine().getStatusCode());
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
	
	private void toRegisterActivity() {
		Intent intent = new Intent(RegisterGetVerCodeActivity.this,RegisterActivity.class);
		intent.putExtra(AppConstant.EXTRA_PHONE_NUM, getCodePhoneEt.getText().toString().trim());
		startActivity(intent);
	}

}
