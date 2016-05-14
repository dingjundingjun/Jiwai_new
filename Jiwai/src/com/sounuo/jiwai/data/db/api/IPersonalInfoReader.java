package com.sounuo.jiwai.data.db.api;

import java.io.File;
import java.util.List;

import com.sounuo.jiwai.data.PersonalInfoPojo;


/**
 * 个人信息读取接口
 * 
 * @author 甘巧
 * @date: 2015-12-17
 */
public interface IPersonalInfoReader {
	public final static byte[] writeLock = new byte[011];
	
	//	1.保存用户信息
	public boolean savePersonalInfo(PersonalInfoPojo personalInfoPojo);

	//	2.获取用户信息
	public PersonalInfoPojo getPersonalInfoPojo() ;
}