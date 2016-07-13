package com.sounuo.jiwai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.utils.ActivityHelper;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.CommonUtil;
import com.sounuo.jiwai.utils.JsonTools;
import com.sounuo.jiwai.utils.MD5;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.SharedPrefUtil;
import com.sounuo.jiwai.utils.StatusBarUtil;
import com.sounuo.jiwai.utils.Util;

/**
 * 登录界面
 */
public class LoginActivity extends Activity implements OnClickListener {

	private Button mLoginBtn;
	
	private TextView mLoginForgetTextView, mLoginRegisterTextView;
	
	private EditText mLoginPhoneEditText;
	
	private EditText mLoginPasswordEditText;
	
	private ProgressBar mLoginProgressBar;
	
	private Toast mToast;

	private HttpClient httpClient;
	
	private HttpParams httpParams;

	private String name;
	
	private String password;

	private boolean isFillPhone;
	
	private boolean isFillPwd;

	private static final String TAG = "RegisterLoginActivity";
	
	private PersonalInfoPojo mPersonInfo;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_in);
		initView();
		initData();
	}

	private void initView() {
		mLoginPhoneEditText = (EditText) findViewById(R.id.login_phone_et);
		mLoginPasswordEditText = (EditText) findViewById(R.id.login_password_et);
		mLoginBtn = (Button) findViewById(R.id.btn_login);
		mLoginForgetTextView = (TextView) findViewById(R.id.login_forget_password);
		mLoginRegisterTextView = (TextView) findViewById(R.id.login_register);
		
		
        /*给TextView中文字设置下划线，颜色*/
        SpannableString spannableString = new SpannableString("创建账号");
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FFA589"));
        spannableString.setSpan(colorSpan, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mLoginRegisterTextView.setText(spannableString);
		
		
		mLoginProgressBar = (ProgressBar) findViewById(R.id.login_progressbar);
		CommonUtil.setTextChangedListener(mLoginPasswordEditText,CommonUtil.ENGLISH_NUMBER);
		mLoginBtn.setOnClickListener(this);
		mLoginForgetTextView.setOnClickListener(this);
		mLoginRegisterTextView.setOnClickListener(this);
		mLoginPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){  
					//处理事件
					toLogin();
					return true;
				}
				return false;
			}
		});
		
		mLoginPhoneEditText.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){  
					//处理事件
					toLogin();
					return true;
				}
				return false;
			}
		});
		
		
		mLoginPasswordEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
		
		mLoginPhoneEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
	
	
	
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		setStatusBar();
	}
	
    protected void setStatusBar() {
            StatusBarUtil.setTranslucent(this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }
	
	private void checkIsFillAll() {

		if (isFillPhone && isFillPwd ) {
			mLoginBtn.setEnabled(true);
		} else {
			mLoginBtn.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			toLogin();
			break;
		case R.id.login_forget_password:
			toForgetPwdActivity();
			break;
		case R.id.login_register:
			toRegisterActivity();
			break;
		default:
			break;
		}
	}

	private void toLogin() {
		name = mLoginPhoneEditText.getText().toString();
		password = mLoginPasswordEditText.getText().toString();
		if (CommonUtil.isEmptyString(name) || name.length() < 11|| !CommonUtil.checkMobile(name)) {
			doToast(this, "请输入正确手机号码，请检查...",Toast.LENGTH_SHORT);
			return;
		}

		if (CommonUtil.isEmptyString(password) || password.length() < 6) {
			doToast(this, "请至少输入6位密码！",Toast.LENGTH_SHORT);
			return;
		}

		if (!Util.checkWifiConnected(this)) {
			doToast(this, "没有网络",Toast.LENGTH_SHORT);
			return;
		}

		showProgress(true);
		
		UserLoginTask loginTask=new UserLoginTask();
		loginTask.execute();
	}
	
	/** 填充用户名 */
	private void initData() {
		if (SharedPrefUtil.getString(getApplicationContext(), AppConstant.PHONE_NUM, "")!= null) {
			String system_phone = SharedPrefUtil.getString(getApplicationContext(), AppConstant.PHONE_NUM, "");
			if(system_phone!=null&&!system_phone.equals("")){
				mLoginPhoneEditText.setText(system_phone);
			}
		}
	}
	
	private void showProgress(boolean show) {
		if(show){
//			mLoginProgressBar.setVisibility(View.VISIBLE);
//			修改登录按钮状态，避免重复点击
			mLoginBtn.setEnabled(false);
			mLoginBtn.setText("登录中...");
		}else{
//			mLoginProgressBar.setVisibility(View.GONE);
			mLoginBtn.setEnabled(true);
			mLoginBtn.setText("登录");
		}
	}

    public void doToast(Context context,String text,int duration) {

        if (mToast == null) {

            mToast = Toast.makeText(context, text,duration);

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

	private void toRegisterActivity() {
		Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
		startActivityForResult(intent, 0);
	}

	private void toForgetPwdActivity() {
		Intent intent = new Intent(LoginActivity.this,CallBackPasswordActivity.class);
		intent.putExtra(AppConstant.EXTRA_PHONE_NUM, mLoginPhoneEditText.getText().toString().trim());
		startActivityForResult(intent, 0);
	}

	
	public class UserLoginTask extends AsyncTask<String, Void, String> {
		


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
			params.add(new BasicNameValuePair("userName", name));
			HttpPost httpPost = new HttpPost(AppConstant.LOGIN_URL);
	        httpParams = new BasicHttpParams();  
	        // 设置连接超时和 Socket 超时，以及 Socket 缓存大小  
	        HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);  
	        HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);  
	        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);  
	        // 设置重定向，缺省为 true  
	        HttpClientParams.setRedirecting(httpParams, true);  
	        // 设置 user agent  
	        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";  
	        HttpProtocolParams.setUserAgent(httpParams, userAgent); 
			Log.i("===========", params.toString());
			HttpResponse httpResponse = null;
			try {
				// 设置httpPost请求参数
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient(httpParams).execute(httpPost);
				int resultCode = httpResponse.getStatusLine().getStatusCode();
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					/* 读返回数据 */
					if (httpResponse.getEntity() == null) {
						System.out.println("JIWAII  ++++   空的");
						Log.e("JIWAI", "JIWAII  ++++    空的");
						return null;
					}
					// 解决中文显示乱码问题
					String strResult = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
					System.out.println("JIWAII  ++++   " + strResult);
					return strResult;
				} else {
					System.out.println("JIWAII  链接失败........."+ httpResponse.getStatusLine().getStatusCode());
					Log.e("JIWAI", "链接失败.........");
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
				doToast(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT);
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
					PersonalUtil.savePersonInfo(LoginActivity.this, mPersonInfo);
					
//					登录成功后应该将用户的登录密码用MD5保存，退出登录后返回再恢复
					
					doToast(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT);
					ActivityHelper.enterMainActivity(LoginActivity.this);
				} else {
					System.out.println("JIWAII  ++   验证失败"+code);
					Log.e(TAG, "onPostExecute 验证失败");
					doToast(LoginActivity.this, "帐号密码不正确！", Toast.LENGTH_SHORT);
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
