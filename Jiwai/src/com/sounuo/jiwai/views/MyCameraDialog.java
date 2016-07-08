package com.sounuo.jiwai.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sounuo.jiwai.R;

/**
 * 为符合软件整体风格而自定义的选择照片来源dialog
 * 
 * @author ZhaoKaiQiang
 * 
 *         Time:2014年3月12日
 */
public class MyCameraDialog extends Dialog {

	private TextView tv_my_camera_take_photo;
	private TextView tv_my_camera_from_album;
	private Context mContext;
	private static int mTheme = R.style.CustomDialog;

	/**
	 * 自定义Dialog
	 * 
	 * @param context
	 *            上下文
	 * @param activity
	 *            Dialog所在的Activity，用于点击“确定”按钮后finish所在的Activity
	 * @param title
	 *            文本内容
	 * @param btnString
	 *            按钮上的文字
	 * @param theme
	 *            样式
	 */
	public MyCameraDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	public MyCameraDialog(Context context) {
		super(context, mTheme);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_select_photo);
		tv_my_camera_take_photo = (TextView) findViewById(R.id.tv_my_camera_take_photo);
		tv_my_camera_from_album = (TextView) findViewById(R.id.tv_my_camera_from_album);

		tv_my_camera_take_photo.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						listener.onTakePhotoChose();
						dismiss();
					}
				});

		tv_my_camera_from_album.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						listener.onSelectFromGalleryChose();
						dismiss();
					}
				});

	}

	private dialogListener listener;

	public dialogListener getListener() {
		return listener;
	}

	public void setListener(dialogListener listener) {
		this.listener = listener;
	}

	public interface dialogListener {
		void onTakePhotoChose();

		void onSelectFromGalleryChose();
	}

}