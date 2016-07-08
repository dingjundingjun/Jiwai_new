package com.sounuo.jiwai.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class CommonUtil {

	// 正则过滤
	public static final String ENGLISH_NUMBER_CHINESE = "[^a-zA-Z0-9\u4E00-\u9FA5]";
	public static final String ENGLISH_NUMBER = "[^a-zA-Z0-9]";

	public static void showToast(Context context, String str) {

		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context context, String str, int gravity) {
		Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}


	// 为edittext设置正则过滤
	public static void setTextChangedListener(final EditText et, final String reg) {
		if (et == null) {
			return;
		}
		et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String editable = et.getText().toString();
				String str = stringFilter(editable, reg);
				if (!editable.equals(str)) {
					et.setText(str);
					if (et.getText().toString().length() > 0) {
						et.setSelection(et.getText().toString().length());
					}
				}
			}
		});
	}

	public static String stringFilter(String str, String reg) throws PatternSyntaxException {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static String[] getStrArrBySplite(String sourceStr, String splitStr) {
		if (!TextUtils.isEmpty(sourceStr)) {
			String[] strArr = sourceStr.split(splitStr);
			return strArr;
		}
		return null;
	}

	public static boolean isEmptyString(String str) {
		if (TextUtils.isEmpty(str)) {
			return true;
		} else {
			return false;
		}
	}

	public static Message getMessage(int msgWhat, String info) {
		Message msg = Message.obtain();
		msg.what = msgWhat;
		msg.obj = info;
		return msg;
	}


	public static boolean checkMobile(String mobiles) {
		boolean isValid = false;
		if (mobiles == null || mobiles.equals("")) {
			return isValid;
		}
		Pattern p = Pattern.compile("^((170)|(17[6-8])|(14[5,7])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		isValid = m.matches();
		return isValid;
	}

	public static boolean isGoodJson(String json) {
		if (TextUtils.isEmpty(json)) {
			return false;
		}
		try {
			new JsonParser().parse(json);
			return true;
		} catch (JsonParseException e) {
			Log.e("badjson", "bad json: " + json);
			return false;
		}
	}


	public static void sendStartBroadcast(Context context) {
		Intent intent = new Intent();
		intent.putExtra("start", "start");
		intent.setAction("com.bbk.personal.start");
		context.sendBroadcast(intent);
	}

}
