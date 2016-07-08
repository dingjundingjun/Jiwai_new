package com.sounuo.jiwai;

import android.app.Application;

import com.sounuo.jiwai.data.db.PersonalInfoManager;

public class JiWaiApplication extends Application {

	private PersonalInfoManager mPersonalInfoManager;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mPersonalInfoManager = PersonalInfoManager.getInstance(getApplicationContext());
	}
	
}
