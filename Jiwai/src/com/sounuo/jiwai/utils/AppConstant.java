package com.sounuo.jiwai.utils;

import java.net.URI;

/**
 * Created by gq on 2016/5/6.
 */
public class AppConstant {
	
	// 网络接口URL
	public final static String ACCOUNT_HOST = "account.sounuo.net";
	
	public final static String HTTP = "http://";
	
	private final static String URL_ACCOUNT_API_HOST = HTTP + ACCOUNT_HOST;
	
//	Headers:
//		token 空
//		timestamp 当前时间戳
//		mid 机器序列号
//		sign 与注册时使用的签名方法相同
	
	/**
	 * 注册时检查手机号码唯一性 提交方式：post 参数： phone 手机号码 mid 机器序列号 timestamp 时间戳 sign 签名
	 */
	public final static String CHECK_PHONE = URL_ACCOUNT_API_HOST+ "/account/checkPhone";
	
	/**
	 * 注册提交 提交方式 post 参数： phone 手机号码 password 密码 nickName 昵称 code 验证码
	 *
	 */
	public final static String CHECK_REG_CODE = URL_ACCOUNT_API_HOST+ "/account/checkRegCode";
	
	/**
	 * 登陆提交 提交方式 post 参数：userName 手机号码 password 密码
	 *
	 */
	public final static String LOGIN_URL = URL_ACCOUNT_API_HOST+ "/account/login";
	
	/**
	 * 找回密码－验证手机 提交方式 post 参数： phone 要找回密码的手机号码
	 *
	 */
	public final static String FORGET_PASSWORD = URL_ACCOUNT_API_HOST+ "/account/forgetPassword";

	/**
	 * 修改密码提交 提交方式 post 参数： phone 要找回密码的手机号码 password 新的密码 code 手机验证码
	 *
	 */
	public final static String CHANGE_PASSWORD_BY_MSG_CODE = URL_ACCOUNT_API_HOST+ "/account/changePasswordByMsgCode";
	/**
	 * 上传头像提交 提交方式 post 参数：phone 手机号码 password 密码
	 *
	 */
	public static final String UPLOAD_HEADIMG = URL_ACCOUNT_API_HOST+"/account/uploadHeadImg";
	
	/**
	 * 修改用户信息 提交方式 post 参数：// nickName 昵称 introduce 个人简介 sex 性别（“男”或“女”） grade
	 * 年级（直接填“一年级”、“二年级”等）
	 */
	public static final String UPDATE_USER_INFO = URL_ACCOUNT_API_HOST+"/account/updateUserInfo";
	
	/**
	 * 修改用户密码
	 */
	public static final String UPDATE_PASSWORD = URL_ACCOUNT_API_HOST+"/account/updatePassword";
	
	/**
	 * 注册时检查手机号码唯一性 提交方式：post 参数： phone 手机号码 mid 机器序列号 timestamp 时间戳 sign 签名
	 */
	public final static String LOGIN_OUT = URL_ACCOUNT_API_HOST+ "/account/logout";
	/**
	 * 续签 提交方式：post 参数：无
	 */
	public final static String UPDATE_TOKEN_EXPTIME = URL_ACCOUNT_API_HOST+ "/account/updateTokenExptime";
	
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

	public static final int JSESSIONID_TIME = 1000*3600;// SSIONID请求时间



	public static String NO_INTENT_TIPS = "与服务器断开连接，请重新检查一下网络";



	
}


