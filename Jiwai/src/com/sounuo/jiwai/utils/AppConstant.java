package com.sounuo.jiwai.utils;

import java.net.URI;

/**
 * Created by gq on 2016/5/6.
 */
public class AppConstant {
	
	// 网络接口URL
	public final static String HOST = "www.sounuo.net";
	
	public final static String HTTP = "http://";
	
	private final static String URL_API_HOST = HTTP + HOST;
	
	/**
	 * 注册时检查手机号码唯一性 提交方式：post 参数： phone 手机号码 mid 机器序列号 timestamp 时间戳 sign 签名
	 */
	public final static String CHECK_PHONE = URL_API_HOST+ "/account/checkPhone";
	
	/**
	 * 注册提交 提交方式 post 参数： phone 手机号码 password 密码 nickName 昵称 code 验证码
	 *
	 */
	public final static String CHECK_REG_CODE = URL_API_HOST+ "/account/checkRegCode";
	
	/**
	 * 登陆提交 提交方式 post 参数：userName 手机号码 password 密码
	 *
	 */
	public final static String LOGIN_URL = URL_API_HOST+ "/account/login";
	
	/**
	 * 修改密码提交 提交方式 post 参数：phone 手机号码 password 密码
	 *
	 */
	public final static String CHANGE_PASSWORD = URL_API_HOST+ "/users/login";
	
	/**
	 * 上传头像提交 提交方式 post 参数：phone 手机号码 password 密码
	 *
	 */
	public static final String PORTRAIT_UPLOAD = URL_API_HOST+"";
	public static final String FING_PASSWORD_GET_CORD = URL_API_HOST+ "/users/login";
	public static final String FING_PASSWORD_CHECK_CODE = URL_API_HOST+ "/users/login";
	public static final String FING_PASSWORD = URL_API_HOST+ "/users/login";
	
	
	//本地文件保存   规则：sounuo/MD5（用户AccountID）/分支目录   例如sounuo/MD5（用户AccountID）/head
	
	
	
	//SharedPreferences 共用参数
	public static final String IS_FIRST_ENTER_APP = "isFirstEnterApp";
	public static final String CHANNEL_CONTROL = "channel_control";
	public static final String ATTENTION_SIZE = "ateention_size";
	public static final String PHONE_NUM = "phone_num";
	public static final String PASSWORD = "password";
	public static final String LAST_GET_CODE_TIME = "last_get_code_time";

	//SharedPreferences 私有参数
	
	// intent extra data 
	public static final String EXTRA_PHONE_NUM = "phone_num";

	public static String NO_INTENT_TIPS = "与服务器断开连接，请重新检查一下网络";



	
}


