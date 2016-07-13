package com.sounuo.jiwai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.MD5;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.Util;
import com.umeng.analytics.MobclickAgent;

public class ChangePasswordActivity extends Activity implements OnClickListener {
	public static final String TAG = "JIWAI";
	/**
	 * 标题
	 */
	private TextView title;
	/**
	 * 旧密码的编辑框
	 */
	private EditText change_old_password;
	/**
	 * 新密码的编辑框
	 */
	private EditText change_new_password;
	/**
	 * 重输密码的编辑框
	 */
	private EditText change_new_repassword;
	/**
	 * 确认修改密码的按钮
	 */
	private Button change_password_sumbit;
	/**
	 * handler
	 */
	private Handler handler;
	/**
	 * 进度条的标示
	 */
	private final int PROGRESS_DIALOG = 0x112;
	/**
	 * 进度条
	 */
	private ProgressDialog pd;
	/**
	 * 用户的ID号
	 */
	private String ID;
	/**
	 * 提交数据
	 */
	private ArrayList<NameValuePair> params;
	/**
	 * 解析服务器的json数据
	 */
	private JSONObject jsonObject;
	/**
	 * 服务器返回的json数据
	 */
	private String dataJson;
	private ProgressBar mLoadingProgressBar;
	protected boolean isFillNewPwd;
	protected boolean isFillOldPwd;
	protected boolean isFillNewRePwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		initView();
		change_password_sumbit.setOnClickListener(this);
	}

	/**
	 * 初始化组件
	 */
	@SuppressLint("NewApi")
	public void initView() {
		title = (TextView) findViewById(R.id.activity_title);
		title.setText("修改密码");
		change_old_password  = (EditText) findViewById(R.id.change_old_password);
		change_new_password= (EditText) findViewById(R.id.change_new_password);
		change_new_repassword = (EditText) findViewById(R.id.change_new_repassword);
		change_password_sumbit  = (Button) findViewById(R.id.change_password_sumbit);
		mLoadingProgressBar  = (ProgressBar) findViewById(R.id.loading_progressbar);
		// 监听输入字数，用以改变登陆按钮状态
		change_old_password.addTextChangedListener(new TextWatcher() {


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
					isFillOldPwd = true;
				} else {
					isFillOldPwd = false;
				}
				checkIsFillAll();
			}
		});

		change_new_password.addTextChangedListener(new TextWatcher() {

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
					isFillNewPwd = true;
				} else {
					isFillNewPwd = false;
				}
				checkIsFillAll();
			}
		});

		change_new_repassword.addTextChangedListener(new TextWatcher() {

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
					isFillNewRePwd = true;
				} else {
					isFillNewRePwd = false;
				}
				checkIsFillAll();
			}
		});
	}
	


	private void checkIsFillAll() {

		if (isFillNewPwd && isFillNewRePwd && isFillOldPwd) {
			change_password_sumbit.setEnabled(true);
		} else {
			change_password_sumbit.setEnabled(false);
		}
	}
	
	private void showProgress(boolean show) {
		if (show) {
			mLoadingProgressBar.setVisibility(View.VISIBLE);
		} else {
			mLoadingProgressBar.setVisibility(View.GONE);
		}
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

	public class changePasswordTask extends AsyncTask<String, Void, String> {
		Context context = null;
		String oldPassword = null;
		String newPassword = null;

		public changePasswordTask(Context papramContext,String papramOldPassword, String paramNewPassword) {
			oldPassword = papramOldPassword;
			newPassword = paramNewPassword;
			context=papramContext;
		}

		@Override
		protected void onPreExecute() {
			showProgress(true);
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... strings) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// 进行简单的MD5加密
			params.add(new BasicNameValuePair("oldPassword", MD5.getMD5(oldPassword)));
			params.add(new BasicNameValuePair("newPassword", MD5.getMD5(newPassword)));
			String url = AppConstant.UPDATE_PASSWORD;
			Log.d(TAG, params.toString());
			Log.d(TAG, url);
			PersonalInfoPojo personInfo = PersonalUtil.getPersonInfo(context);
			String phone = personInfo.getPhone();
			String token=personInfo.token;
			Log.d(TAG, phone);
			HashMap<String, String> headers = Util.getAccountHeaders(context,token, phone);
			return Util.AccoutRequest(params, url, headers);
		}

		@Override
		protected void onPostExecute(String code) {
			showProgress(false);
			if (code == null) {
				doToast(context, "网络异常，请检查网络", Toast.LENGTH_SHORT);
				return;
			}
			if (code.equals("200")) {
				doToast(context, "修改密码成功！", Toast.LENGTH_SHORT);
				finish();
			}  else if (code.equals("404")) {
				doToast(context, "原密码不正确！请确认后再试验！",Toast.LENGTH_SHORT);
			}
		}
	}
	
	
	public void ChangePassword(){
		String old_password = change_old_password.getText().toString().trim();
		String new_password = change_new_password.getText().toString().trim();
		String new_repassword = change_new_repassword.getText().toString().trim();
		
		if(old_password.equals("")||new_password.equals("")||new_repassword.equals("")){
			Toast.makeText(ChangePasswordActivity.this, "填写不能为空", Toast.LENGTH_SHORT).show();
		}else if(!new_password.equals(new_repassword)){
			Toast.makeText(ChangePasswordActivity.this, "新密码输入两次不一致，请重新输入", Toast.LENGTH_SHORT).show();
		}else if(new_password.length()<6){
			Toast.makeText(ChangePasswordActivity.this, "密码不能少于6位数", Toast.LENGTH_SHORT).show();
		}else{
			changePasswordTask task=new changePasswordTask(this, old_password, new_password);
			task.execute();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			// 返回按钮
			this.finish();
			break;
		case R.id.change_password_sumbit:
			ChangePassword();
			break;
		default:
			break;
		}
	}
}
