package com.sounuo.jiwai.data.db;

import android.net.Uri;
import android.provider.BaseColumns;
/**
 * 个人信息字段
 * @author gq
 *
 */
public class PersonalInfoColums implements BaseColumns {

	public static final String TABLE_NAME = "personalinfo";

	public static final String ACCOUNT_ID = "accountId";
	
	public static final String PASSWORD = "password";
	
	public static final String NICK_NAME = "nickName";
	
	public static final String SEX = "sex";
	
	public static final String GRADE = "grade";
	
	public static final String PHOTO_PATH = "photoPath";
	
	public static final String AUTOHORITY = "com.sounuo.jiwai";
	
	public static final int DB_VERSION = 1;
	
	public static final String DB_NAME = "jiwai.db";
	
	public static final int ITEM = 1;
	
	public static final int ITEM_ID = 2;
	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.sounuo.jiwai";
	
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.sounuo.jiwai";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTOHORITY+"/"+TABLE_NAME);

}
