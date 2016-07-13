/**********************************************************
 * Copyright (C)2014, 步步高教育电子有限公司
 *
 * @Description: 壁纸数据库表列定义类
 * @Package: com.eebbk.apptype.database
 * @version: V1.0
 ***********************************************************/
package com.sounuo.jiwai.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteInfoColumns implements BaseColumns {

	/**
	 * 数据库名称
	 */
	public static final String TABLE_NAME = "collect";

	/**
	 * 文章标题
	 */
	public static final String TITLE = "title";
	/**
	 * 用户名
	 */
	public static final String USERNAME = "username";
	/**
	 * 描述信息
	 */
	public static final String DESCRIPTION = "description";
	/**
	 * 端口
	 */
	public static final String DOMAIN = "domain";
	/**
	 * 链接
	 */
	public static final String URL = "url";
	/**
	 * 创建时间
	 */
	public static final String DATELINE = "dateline";
	/**
	 * ID
	 */
	public static final String ID = "id";
	/**
	 * 分享数目
	 */
	public static final String SHARE = "share";
	/**
	 * 评论数目
	 */
	public static final String COMMENT = "comment";
	/**
	 * 点赞数目
	 */
	public static final String LIKE = "like";


    public static final int DB_VERSION = 1;
    
    public static final String DB_NAME = "jiwai.db";


}
