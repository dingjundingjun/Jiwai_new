package com.sounuo.jiwai;

import java.util.HashMap;

import android.app.Application;
import android.util.Log;

import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.Util;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.sdkmanager.LoginSDKManager;
import com.umeng.common.ui.presenter.impl.LoginSimplify;
import com.umeng.simplify.ui.presenter.impl.LoginSuccessStrategory;

public class JiWaiApplication extends Application {

	/**
	 * 若退出，此变量需要设置成false
	 */
	public static boolean isGo = true;
	private PersonalInfoPojo personInfo;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		CommunitySDK mCommSDK = CommunityFactory.getCommSDK(this);
        mCommSDK.initSDK(this);     
        LoginSDKManager.getInstance().addAndUse(new LoginSimplify());
        CommConfig.getConfig().setLoginResultStrategy(new LoginSuccessStrategory());
		 personInfo = PersonalUtil.getPersonInfo(this);
		// 判断是否需要再重新登录
		if (personInfo != null) {
			rollLogin();
		}
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-1-14 上午10:13:06
	 * @Decription 轮询维护Token
	 * 
	 */
	public void rollLogin() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				int i = 0;
				while (isGo == true) {
					i++;
					try {
						 
						HashMap<String, String> headers=Util.getAccountHeaders(JiWaiApplication.this, personInfo.token, personInfo.phone);
						String result = Util.AccoutRequest(null, AppConstant.UPDATE_TOKEN_EXPTIME,headers);
						Log.i("Thread", "轮询次数  == " + i);
						Thread.sleep(AppConstant.JSESSIONID_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
}
