package com.sounuo.jiwai;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.utils.CommonUtil;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.Util;

public class EditActivity extends Activity implements OnClickListener {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_userinfo);
		
		personInfo = PersonalUtil.getPersonInfo(this);
		
		initviews();
	}

	private void initviews() {
		updateUsername = (EditText) findViewById(R.id.update_username_et);
		updateIntroduce = (EditText) findViewById(R.id.update_introduce_et);
		updateGrade = (EditText) findViewById(R.id.update_grade_et);
		updateSex = (EditText) findViewById(R.id.update_sex_et);
		updatePhone = (TextView) findViewById(R.id.update_phone);
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

	private void register() {
		updateUsernameStr = updateUsername.getText().toString().trim();

		hideSoftInput(this);

		if (CommonUtil.isEmptyString(updateUsernameStr) ) {
			doToast(this, "昵称不能为空", Toast.LENGTH_SHORT);
			return;
		}
		
		if (updateUsernameStr.length() < 2) {
			doToast(this, "昵称太短了", Toast.LENGTH_SHORT);
			return;
		}
		
		if(!CommonUtil.isEmptyString(updateIntroduce.getText().toString().trim())){
			updateIntroduceStr = updateIntroduce.getText().toString().trim();
		}

		updateGradeStr = updateGrade.getText().toString().trim();
		
		updateSexStr = updateSex.getText().toString().trim();

		if (!Util.checkWifiConnected(this)) {
			doToast(this, "没有网络", Toast.LENGTH_SHORT);
			return;
		}


	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
			
		case R.id.btn_edit_finish:
			finish();
			break;

		default:
			break;
		}
	}

}
