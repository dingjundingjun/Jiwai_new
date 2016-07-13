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
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.Util;

public class updateUserInfoActivity extends Activity implements OnClickListener {

	private EditText updateUsername;
	private EditText updateIntroduce;
	private EditText updateGrade;
	private EditText updateSex;
	private PersonalInfoPojo personInfo;
	private String updateUsernameStr;
	private String updateIntroduceStr;
	private String updateGradeStr;
	private String updateSexStr;
	private Toast mToast;
	private TextView updatePhone;
	private ProgressBar mLoginProgressBar;
	private String userPhone;
	private static final String TAG = "JIWAII";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_userinfo);
		
		personInfo = PersonalUtil.getPersonInfo(this);
		userPhone=personInfo.getPhone();
		
		initviews();
	}

	private void initviews() {
		updateUsername = (EditText) findViewById(R.id.update_username_et);
		updateIntroduce = (EditText) findViewById(R.id.update_introduce_et);
		updateGrade = (EditText) findViewById(R.id.update_grade_et);
		updateSex = (EditText) findViewById(R.id.update_sex_et);
		updatePhone = (TextView) findViewById(R.id.update_phone);
		mLoginProgressBar = (ProgressBar) findViewById(R.id.login_progressbar);
		updatePhone.setOnClickListener(this);
		
//		初始化数据
		updateUsername.setText(personInfo.nickName);
		if(personInfo.nickName!=null){
			updateIntroduce.setText(personInfo.nickName);
			updateIntroduceStr=personInfo.nickName;
		}
		if(personInfo.grade!=null){
			updateGrade.setText(personInfo.grade);
		}
		if(personInfo.sex!=null){
			updateSex.setText(personInfo.sex);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
			
		case R.id.btn_edit_finish:
			toUpdateInfo();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 点击按钮更新用户信息
	 */
	public void toUpdateInfo() {
		Log.d(TAG, "gotoDownloadLockpaper 下载壁纸");

		if (!Util.checkWifiConnected(this)) {
			doToast(this, "没有网络", Toast.LENGTH_SHORT);
			return;
		}
		
		String mId = Util.getMid(this);
		
		long timestamp=System.currentTimeMillis();
		
		String sign = Util.getSign(mId, userPhone,timestamp+"");
		
		String token=PersonalUtil.getPersonInfo(this).token;

		updateUsernameStr=updateUsername.getText().toString().trim();
		updateIntroduceStr=updateIntroduce.getText().toString().trim();
		updateGradeStr=updateGrade.getText().toString().trim();
		updateSexStr=updateSex.getText().toString().trim();
		
//		验证接口，暂时先不做条件限制
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("nickName", updateUsernameStr));
		params.add(new BasicNameValuePair("introduce", updateIntroduceStr));
		params.add(new BasicNameValuePair("sex", updateSexStr));
		params.add(new BasicNameValuePair("grade", updateGradeStr));

		UpdateUserInfoTask getTassk = new UpdateUserInfoTask(token,sign,mId,timestamp+"",params);
		getTassk.execute();

	}
	
	private void showProgress(boolean show) {
		if(show){
			mLoginProgressBar.setVisibility(View.VISIBLE);
		}else{
			mLoginProgressBar.setVisibility(View.GONE);
		}
	}
	
	
	/**
	 * 获取验证码的task
	 */
	private class UpdateUserInfoTask extends AsyncTask<String, Integer, String> {

		String sign = null;
		
		String deviceId = null;
		
		String timestamp = null;
		
		String token = null;

		private List<NameValuePair> nameValuePair;

		public UpdateUserInfoTask( String paramToken, String paramSign, String paramDeviceId, String paramTimestamp,List<NameValuePair> paramNameValuePair) {

			token = paramToken;
			sign = paramSign;
			deviceId = paramDeviceId;
			timestamp = paramTimestamp;
			nameValuePair=paramNameValuePair;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showProgress(true);
		}

		@Override
		protected String doInBackground(String... strings) {


			return updateUserInfo(token,sign,deviceId,timestamp,nameValuePair);
		}

		@Override
		protected void onPostExecute(String code) {
			showProgress(false);
			if (code == null) {
				doToast(updateUserInfoActivity.this, "网络异常", Toast.LENGTH_SHORT);
				return;
			}
			Log.e("JIWAI", "打印看看返回码   :  " + code);
			if (code.equals("200")) {
				doToast(updateUserInfoActivity.this, "账号信息更新完成！", Toast.LENGTH_SHORT);
				finish();
			} else {
				doToast(updateUserInfoActivity.this, "请检查更新数据是否正确", Toast.LENGTH_SHORT);
			}
			super.onPostExecute(code);
		}
	}

	
	/**
	 * 更新用户信息
	 * 
	 * @param token
	 * @param sign
	 * @param timestamp 
	 * @param deviceId 
	 * @param nameValuePair 
	 * @return
	 */
	public String updateUserInfo(String token,  String sign, String deviceId, String timestamp, List<NameValuePair> nameValuePair) {
		HttpPost httpPost = new HttpPost(AppConstant.UPDATE_USER_INFO);
		Log.d(TAG, "getLockpaperSSLHttpPost url: " + AppConstant.UPDATE_USER_INFO);
		Log.d(TAG, "getLockpaperSSLHttpPost params: " + nameValuePair.toString());

		HttpResponse httpResponse = null;
		try {
			// 设置httpPost请求参数
        	httpPost.addHeader("token",token);
        	httpPost.addHeader("timestamp",timestamp);
        	httpPost.addHeader("mid",deviceId);
        	httpPost.addHeader("sign",sign);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
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
