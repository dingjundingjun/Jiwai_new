package com.sounuo.jiwai;

import java.util.ArrayList;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * description :修改密码activity
 *
 * @version 1.0
 * @author zhoufeng
 * @createtime : 2015-1-27 下午5:49:03
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * zhoufeng        2015-1-27 下午5:49:03
 *
 */
public class ChangePasswordActivity extends Activity implements OnClickListener {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);

		initView();

		handler = new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					Toast.makeText(ChangePasswordActivity.this, AppConstant.NO_INTENT_TIPS,
							Toast.LENGTH_LONG).show();
					break;
				case 1:
					try {
						Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					finish();
					break;
					
				case 2:
					try {
						Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		};
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

		SharedPreferences userPreferences = getSharedPreferences(
				"umeng_general_config", Context.MODE_PRIVATE);
		ID = userPreferences.getString("ID", "").trim();

		
	}

	/**
	 * 
	 * 重写方法: onCreateDialog|描述:进度条的框
	 * 
	 * @param id
	 * @param status
	 * @return
	 * @see android.app.Activity#onCreateDialog(int, android.os.Bundle)
	 */
	protected Dialog onCreateDialog(int id, Bundle status) {
		if (id == PROGRESS_DIALOG) {
			pd.setMessage("正在提交...");
		}
		return pd;
	}
	
	public void ChangePassword(){
		String old_password = change_old_password.getText().toString().trim();
		String new_password = change_new_password.getText().toString().trim();
		String new_repassword = change_new_repassword.getText().toString().trim();
		
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("memberId", ID));
		params.add(new BasicNameValuePair("oldPassword", old_password));
		params.add(new BasicNameValuePair("newPassword", new_password));
		
		if(old_password.equals("")||new_password.equals("")||new_repassword.equals("")){
			Toast.makeText(ChangePasswordActivity.this, "填写不能为空", Toast.LENGTH_SHORT).show();
		}else if(!new_password.equals(new_repassword)){
			Toast.makeText(ChangePasswordActivity.this, "新密码输入两次不一致，请重新输入", Toast.LENGTH_SHORT).show();
		}else if(new_password.length()<6){
			Toast.makeText(ChangePasswordActivity.this, "密码不能少于6位数", Toast.LENGTH_SHORT).show();
		}else{
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					Message msg = new Message();
					dataJson = Util.request(params, AppConstant.CHANGE_PASSWORD);
					if (dataJson.length() != 0 && !dataJson.equals("0x110")) {
						try {
							jsonObject = new JSONObject(dataJson);
							if (jsonObject.getString("success").equals("true")) {
								msg.what = 1;
							}else{
								msg.what = 2;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}else{
						msg.what = 0;
					}
					handler.sendMessage(msg);
				}
			});
			thread.start();
		}
	}

	/**
	 * 
	 * 重写方法: onClick|描述: 点击事件
	 * 
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
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
