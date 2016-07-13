package com.sounuo.jiwai.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class UploadUtil {
	private static UploadUtil uploadUtil;

	private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
																			// 随机生成

	private static final String PREFIX = "--";

	private static final String LINE_END = "\r\n";

	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

	private UploadUtil() {

	}

	/**
	 * 单例模式获取上传工具类
	 * 
	 * @return
	 */
	public static UploadUtil getInstance() {
		if (null == uploadUtil) {
			uploadUtil = new UploadUtil();
		}
		return uploadUtil;
	}

	private static final String TAG = "UploadUtil";

	private int readTimeOut = 20 * 1000; // 读取超时

	private int connectTimeout = 20 * 1000; // 超时时间

	/***
	 * 请求使用多长时间
	 */
	private static int requestTime = 0;

	private static final String CHARSET = "utf-8"; // 设置编码

	/***
	 * 上传成功
	 */
	public static final int UPLOAD_SUCCESS_CODE = 1;
	/**
	 * 文件不存在
	 */
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	/**
	 * 服务器出错
	 */
	public static final int UPLOAD_SERVER_ERROR_CODE = 3;

	/**
	 * android上传文件到服务器
	 * 
	 * @param filePath
	 *            需要上传的文件的路径
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public void uploadFile(String filePath, String fileKey, String RequestURL, Map<String, String> param) {
		if (filePath == null) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
			return;
		} 
		try {
			File file = new File(filePath);
			uploadFile(file, fileKey, RequestURL, param);
		} catch (Exception e) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
			e.printStackTrace();
			return;
		}
	}

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public void uploadFile(final File file, final String fileKey,
			final String RequestURL, final Map<String, String> param) {
		if (file == null || (!file.exists())) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
			return;
		}

		Log.e(TAG, "请求的URL=" + RequestURL);
		Log.e(TAG, "请求的URL=" + param);
		Log.e(TAG, "请求的fileName=" + file.getName());
		Log.e(TAG, "请求的fileKey=" + fileKey);
		new Thread(new Runnable() { // 开启线程上传文件
					@Override
					public void run() {
						toUploadFile(file, fileKey, RequestURL, param);
					}
				}).start();

	}

	private void toUploadFile(File file, String fileKey, String RequestURL,
			Map<String, String> param) {

		RequestParams params = new RequestParams();

		params.addBodyParameter(fileKey, file);
		/***
		 * 以下是用于上传参数
		 */
		if (param != null && param.size() > 0) {
			Iterator<String> it = param.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = param.get(key);
				params.addHeader(key, value);
			}
		}

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, RequestURL, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败   msg  "+msg+"  error  "+error);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						int res = responseInfo.statusCode;
						Log.e(TAG, "response code:" + res);
						if (res == 200) {
							String strResult = responseInfo.result;
							Log.e(TAG, "result : " + strResult);
							JSONObject jsonObject;
							try {
								jsonObject = new JSONObject(strResult);
								if (jsonObject.getString("code").equals("200")) {
									Log.e(TAG, "result : " + jsonObject.getString("data"));
									sendMessage(UPLOAD_SUCCESS_CODE,jsonObject.getString("data"));
									return;
								} else {
									sendMessage(UPLOAD_SERVER_ERROR_CODE,"请上传正确的文件！code="+ jsonObject.getString("code"));
									return;
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								sendMessage(UPLOAD_SERVER_ERROR_CODE, "JSON  解析失败");
							}
							return;
						} else {
							Log.e(TAG, "request error");
							sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：statusCode ="+ res);
							return;
						}

					}
				});

	}

	/**
	 * 发送上传结果
	 * 
	 * @param responseCode
	 * @param responseMessage
	 */
	private void sendMessage(int responseCode, String responseMessage) {
		onUploadProcessListener.onUploadDone(responseCode, responseMessage);
	}

	/**
	 * 下面是一个自定义的回调函数，用到回调上传文件是否完成
	 * 
	 * @author shimingzheng
	 * 
	 */
	public static interface OnUploadProcessListener {
		/**
		 * 上传响应
		 * 
		 * @param responseCode
		 * @param message
		 */
		void onUploadDone(int responseCode, String message);

		/**
		 * 上传中
		 * 
		 * @param uploadSize
		 */
		void onUploadProcess(int uploadSize);

		/**
		 * 准备上传
		 * 
		 * @param fileSize
		 */
		void initUpload(int fileSize);
	}

	private OnUploadProcessListener onUploadProcessListener;

	public void setOnUploadProcessListener(
			OnUploadProcessListener onUploadProcessListener) {
		this.onUploadProcessListener = onUploadProcessListener;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 获取上传使用的时间
	 * 
	 * @return
	 */
	public static int getRequestTime() {
		return requestTime;
	}

	public static interface uploadProcessListener {

	}

}
