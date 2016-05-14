package com.sounuo.jiwai.data.db;

import android.content.ContentResolver;
import android.content.Context;

import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.data.db.api.IPersonalInfoReader;

/**
 * 
 * @author gq
 *
 */
public class PersonalInfoManager implements IPersonalInfoReader {
	public ContentResolver mContentResolver = null;
	private Context mContext;

	/** 上下文环境 */
	public PersonalInfoManager(Context context) {
		mContentResolver = context.getContentResolver();
		mContext = context;
	}

	/**
	 * 
	 * 获取用户信息数据管理实例
	 * 
	 * @param
	 * @return 用户信息数据管理实例
	 */
	public static PersonalInfoManager getInstance(Context context) {
		return new PersonalInfoManager(context);
	}
	

	@Override
	public boolean savePersonalInfo(PersonalInfoPojo personalInfoPojo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PersonalInfoPojo getPersonalInfoPojo() {
		// TODO Auto-generated method stub
		return null;
	}

}
