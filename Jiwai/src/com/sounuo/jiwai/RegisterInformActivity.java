package com.sounuo.jiwai;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.database.ColumnBean;
import com.sounuo.jiwai.utils.ActivityHelper;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.Debug;
import com.sounuo.jiwai.utils.FileUtil;
import com.sounuo.jiwai.utils.MD5;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.SharedPrefUtil;
import com.sounuo.jiwai.utils.UploadUtil;
import com.sounuo.jiwai.utils.UploadUtil.OnUploadProcessListener;
import com.sounuo.jiwai.utils.Util;
import com.sounuo.jiwai.views.ChangeColorTextView;
import com.sounuo.jiwai.views.CircleImageView;
import com.sounuo.jiwai.views.MyCameraDialog;

/**
 * 获取用户头像，以及用户关注的栏目
 */
public class RegisterInformActivity extends Activity implements
		View.OnClickListener, OnUploadProcessListener {

    private static final int REQUEST_CODE_CAMERA_WITH_DATA = 1001;
    
    private static final int REQUEST_CODE_PHOTO_PICKED_WITH_DATA = 1002;
    
    private static final int REQUEST_CROP_PHOTO = 1003;
	
	private static final boolean TEST = false;

	private static final String TAG = "RegisterInformActivity";

	protected static final int UPLOAD_INIT_PROCESS = 0;

	protected static final int UPLOAD_PROCESSING = 1;

	protected static final int UPLOAD_FILE_DONE = 2;



	// 本地测试数据
	private List<ColumnBean> columnBeans = new ArrayList<ColumnBean>();

	private boolean isEditMode = false;

	private CircleImageView mPhotoView;
	
	private static String ICON_REAL_PATH = null;

	private static String ICON_PATH;
	
	private static String ICON_TEMP_PATH;

	private Button mEnterGreatWorld;

	private GridView gridView;

	private ColumnAdapter mColumnAdapter;

//	private PersonalInfoPojo personInfo;

	private TextView tvNickName;

	private TextView tvIntroduce;

	private LinearLayout enterEditBaseInfo;

	private ImageView ivEnterEdit;

	private Intent takePhotoIntent;

	private Uri mCurrentPhotoUri;

	private Uri mTempPhotoUri;

	private Uri mCroppedPhotoUri;

	private ListPopupWindow mPopup;

	private ProgressDialog progressDialog;

	private Toast mToast;

	private PersonalInfoPojo personInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register_inform);

		personInfo = PersonalUtil.getPersonInfo(this);
		
		personInfo.getPhotoPath();
		
		Log.e("JIWAI"," 查看下保存的PhotoPath   "+personInfo.getPhotoPath());

		// 获取头像保存路径
		ICON_PATH = pathForTempPhoto(this, "head.png");
		// 获取头像缓存保存路径
		ICON_TEMP_PATH = pathForTempPhoto(this, "head_temp.png");

        mTempPhotoUri = generateTempImageUri(this);
        
        mCroppedPhotoUri = generateTempCroppedImageUri(this);
		
		// 初始化视图界面
		initviews();

		initAdapter(gridView);

	}
	
    public static Uri generateTempImageUri(Context context) {
        return Uri.fromFile(new File(ICON_TEMP_PATH));
    }
    
    public static Uri generateTempCroppedImageUri(Context context) {
    	return Uri.fromFile(new File( ICON_PATH));
    }

	private void initviews() {
		gridView = (GridView) findViewById(R.id.column_grid_view);
		tvNickName = (TextView) findViewById(R.id.user_name_tv);
		enterEditBaseInfo = (LinearLayout) findViewById(R.id.enter_edit_base_info);
		tvIntroduce = (TextView) findViewById(R.id.introduce_tv);
		ivEnterEdit = (ImageView) findViewById(R.id.iv_enter_edit);
		mPhotoView = (CircleImageView) findViewById(R.id.civ_user_head);
		mEnterGreatWorld = (Button) findViewById(R.id.btn_enter_great_world);
		mEnterGreatWorld.setOnClickListener(this);
		mPhotoView.setOnClickListener(this);
		progressDialog = new ProgressDialog(this);
//		tvNickName.setText(personInfo.nickName);
		enterEditBaseInfo.setOnClickListener(this);
		BitmapUtils bitmapUtils = new BitmapUtils(RegisterInformActivity.this);
		String imageUrl=personInfo.getPhotoPath();
		bitmapUtils.display(mPhotoView , imageUrl);
	}

	private void initAdapter(GridView gridView) {
		initData();
		mColumnAdapter = new ColumnAdapter(columnBeans, this);
		gridView.setAdapter(mColumnAdapter);
	}

	private void initData() {
		columnBeans.add(new ColumnBean("看世界", true,
				"http://121.40.93.89:13090/article/getWatchWorldList"));
		columnBeans.add(new ColumnBean("笑一校", true,
				"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("奇闻趣事", true,
				"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("校园轶事", true,
				"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("美景记事", true,
				"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("历史典故", true,
				"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("奇闻趣事", true,
				"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("校园轶事", true,
				"http://121.40.93.89:13090/article/getWatchWorldList"));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		handlePhotoActivityResult(requestCode, resultCode, data);
	}

	public void setCrop() {
		
		MyCameraDialog dialog=new MyCameraDialog(this);
		dialog.setListener(new MyCameraDialog.dialogListener() {
			
			@Override
			public void onTakePhotoChose() {
				// TODO Auto-generated method stub
				startTakePhotoActivity(mTempPhotoUri);
			}
			
			@Override
			public void onSelectFromGalleryChose() {
				// TODO Auto-generated method stub
				startPickFromGalleryActivity(mTempPhotoUri);
			}
		});
		dialog.show();
		
	}
	
    public void startPhotoActivity(Intent intent, int requestCode, Uri photoUri) {
        mCurrentPhotoUri = photoUri;
        this.startActivityForResult(intent, requestCode);
    }
    
    private void startPickFromGalleryActivity(Uri photoUri) {
        final Intent intent = getPhotoPickIntent(photoUri);
        startPhotoActivity(intent, REQUEST_CODE_PHOTO_PICKED_WITH_DATA, photoUri);
    }
    
    private void startTakePhotoActivity(Uri photoUri) {
        final Intent intent = getTakePhotoIntent(photoUri);
        startPhotoActivity(intent, REQUEST_CODE_CAMERA_WITH_DATA, photoUri);
    }
    
    
	public boolean handlePhotoActivityResult(int requestCode, int resultCode,Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			// Cropped photo was returned
			case REQUEST_CROP_PHOTO: {
				final Uri uri;
				if (data != null && data.getData() != null) {
					uri = data.getData();
				} else {
					uri = mCroppedPhotoUri;
				}
				FileUtil.deleteFile(ICON_TEMP_PATH);
				onPhotoSelected();
				return true;
			}

			// Photo was successfully taken or selected from gallery, now crop
			// it.
			case REQUEST_CODE_PHOTO_PICKED_WITH_DATA:
			case REQUEST_CODE_CAMERA_WITH_DATA:
				final Uri uri;
				boolean isWritable = false;
				if (data != null && data.getData() != null) {
					uri = data.getData();
				} else {
					uri = mCurrentPhotoUri;
					isWritable = true;
				}
				final Uri toCrop;
				if (isWritable) {
					// Since this uri belongs to our file provider, we know that
					// it is writable
					// by us. This means that we don't have to save it into
					// another temporary
					// location just to be able to crop it.
					toCrop = uri;
				} else {
					toCrop = mTempPhotoUri;
					try {
						savePhotoFromUriToUri(this, uri, toCrop, false);
					} catch (SecurityException e) {
						Log.d(TAG, "Did not have read-access to uri : " + uri);
						return false;
					}
				}

				doCropPhoto(toCrop, mCroppedPhotoUri);
				return true;
			}
		}else{
			doToast(RegisterInformActivity.this, "选择头像出错", Toast.LENGTH_SHORT);
		}
		return false;
	}
	
	
    /**
     * Given an input photo stored in a uri, save it to a destination uri
     */
    public static boolean savePhotoFromUriToUri(Context context, Uri inputUri, Uri outputUri,
            boolean deleteAfterSave) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = context.getContentResolver()
                    .openAssetFileDescriptor(outputUri, "rw").createOutputStream();
            inputStream = context.getContentResolver().openInputStream(
                    inputUri);

            final byte[] buffer = new byte[16 * 1024];
            int length;
            int totalLength = 0;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
                totalLength += length;
            }
            Log.v(TAG, "Wrote " + totalLength + " bytes for photo " + inputUri.toString());
        } catch (IOException e) {
            Log.e(TAG, "Failed to write photo: " + inputUri.toString() + " because: " + e);
            return false;
        } finally {
        	try {
				inputStream.close();
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (deleteAfterSave) {
                context.getContentResolver().delete(inputUri, null, null);
            }
        }
        return true;
    }
    
    public void onPhotoSelected() {
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(this); // 设置监听器监听上传状态
//		long timestamp=System.currentTimeMillis();
//		String deviceId=Util.getMid(this);
//		String sign=deviceId+timestamp+"sounuo"+phone;
//		System.out.println("JIWAII  ++   MD5 前 " + sign);
//		sign = MD5.getMD5(sign);
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("timestamp", timestamp+"");
//		params.put("mid", deviceId);
//		params.put("sign", sign);
//		params.put("token", PersonalUtil.getPersonInfo(this).token);
		String token=PersonalUtil.getPersonInfo(this).token;
		String phone=PersonalUtil.getPersonInfo(this).phone;
		HashMap<String, String> accountHeaders = Util.getAccountHeaders(this, token, phone);
		uploadUtil.uploadFile(ICON_PATH, "img", AppConstant.UPLOAD_HEADIMG, accountHeaders);
		progressDialog.setMessage("正在上传文件...");
		progressDialog.show();
    }
    
    
    
    private static String pathForTempPhoto(Context context, String fileName) {
        final File dir = new File(Environment.getExternalStorageDirectory() + File.separator+ "新奇葩");
        dir.mkdirs();
        final File f = new File(dir, fileName);
        return f.getAbsolutePath();
    }
    


    private Intent getTakePhotoIntent(Uri outputUri) {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        addPhotoPickerExtras(intent, outputUri);
        return intent;
    }
	
	private Intent getPhotoPickIntent(Uri outputUri) {
		Intent intent = new Intent("android.intent.action.PICK");
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,"image/*");
		addPhotoPickerExtras(intent, outputUri);
		return intent;
	}

    private Intent getCropImageIntent(Uri inputUri, Uri outputUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        Log.e("JIWAII", "  inputUri  ==  "+inputUri.toString());
        Log.e("JIWAII", "  outputUri  ==  "+outputUri.toString());
        intent.setDataAndType(inputUri, "image/*");
        addPhotoPickerExtras(intent, outputUri);
        addCropExtras(intent, 200);
        return intent;
    }
    
    public static void addPhotoPickerExtras(Intent intent, Uri photoUri) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION |Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, photoUri));
    }
    
    public static void addCropExtras(Intent intent, int photoSize) {
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", photoSize);
        intent.putExtra("outputY", photoSize);
    }
    
    private void doCropPhoto(Uri inputUri, Uri outputUri) {
        try {
            // Launch gallery to crop the photo
            final Intent intent = getCropImageIntent(inputUri, outputUri);
            startPhotoActivity(intent, REQUEST_CROP_PHOTO, inputUri);
        } catch (Exception e) {
            Log.e(TAG, "Cannot crop image", e);
            Toast.makeText(this, "无法截图", Toast.LENGTH_LONG).show();
        }
    }
    
    
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.civ_user_head:
			//仿联系人代码
			setCrop();
			break;
		case R.id.btn_enter_great_world:
			Gson gson = new Gson();
			String jsonString = gson.toJson(columnBeans);
			SharedPrefUtil.putString(this, AppConstant.CHANNEL_CONTROL,jsonString);
			SharedPrefUtil.putBoolean(getApplicationContext(),AppConstant.IS_FIRST_ENTER_APP, false);
			SharedPrefUtil.putInt(getApplicationContext(),AppConstant.ATTENTION_SIZE,mColumnAdapter.getAttentionSize());
			ActivityHelper.enterMainCatalog(this);
			finish();
			break;
		case R.id.enter_edit_base_info:
			ivEnterEdit.setImageResource(R.drawable.myspace_edit_pressed);
			ActivityHelper.enterEdit(this);
			break;
		default:
			break;
		}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ivEnterEdit.setImageResource(R.drawable.myspace_edit_normal);
	}

	class ColumnAdapter extends BaseAdapter {
		List<ColumnBean> mColumnBeans;
		Context mContext;
		String[] colors = new String[] { "#FFD6ECF0", "#FFFFA631", "#FF9ed046",
				"#FFFF7500", "#FFD6ECF0", "#FFFFA631", "#FF9ed046", "#FFFF7500" };

		private SparseBooleanArray checkSparseBooleanArray;

		public SparseBooleanArray getCheckSparseBooleanArray() {
			return checkSparseBooleanArray;
		}

		public void setCheckSparseBooleanArray(
				SparseBooleanArray checkSparseBooleanArray) {
			this.checkSparseBooleanArray = checkSparseBooleanArray;
		}

		public ColumnAdapter(List<ColumnBean> readTitleDatas, Context context) {
			this.mColumnBeans = readTitleDatas;
			this.mContext = context;
			checkSparseBooleanArray = new SparseBooleanArray();
			// 将全部的栏目设置为true
			configCheckMap(true);
		}

		public void configCheckMap(boolean bool) {
			Debug.d("调用了configCheckMap来设置checkbox整个状态");
			for (int i = 0; i < mColumnBeans.size(); i++) {
				checkSparseBooleanArray.put(i, bool);
			}
		}

		@Override
		public int getCount() {
			return mColumnBeans.size();
		}

		@Override
		public Object getItem(int position) {
			return mColumnBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		/**
		 * 获取到当前关注的数量值
		 */
		public int getAttentionSize() {
			int count = 0;
			for (int i = 0; i < checkSparseBooleanArray.size(); i++) {
				if (checkSparseBooleanArray.get(i)) {
					count++;
				}
			}
			return count;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ColumnBean columnBean = mColumnBeans.get(position);
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			View view = layoutInflater.inflate(R.layout.column_grid_view_item,null);
			final ChangeColorTextView columnInfoTextView = (ChangeColorTextView) view.findViewById(R.id.column_info_tv);
			columnInfoTextView.setText(columnBean.getTitle());
			columnInfoTextView.setIconColor(Color.parseColor(colors[position]));
			columnInfoTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (getAttentionSize() <= 1) {
						Toast.makeText(RegisterInformActivity.this,"最少需要关注一个栏目哦", Toast.LENGTH_SHORT).show();
						return;
					}
					columnInfoTextView.changeSelectColor();
					mColumnBeans.get(position).setIsChecked(columnInfoTextView.isSelected());
					checkSparseBooleanArray.put(position,columnInfoTextView.isSelected());
				}
			});
			return view;
		}
	}
	
	public void doToast(Context context, String text, int duration) {

		if (mToast == null) {

			mToast = Toast.makeText(context, text, duration);

		} else {

			mToast.setText(text);

			mToast.setDuration(Toast.LENGTH_SHORT);

		}

		mToast.show();

	}

	@Override
	public void onUploadProcess(int uploadSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_PROCESSING;
		msg.arg1 = uploadSize;
		handler.sendMessage(msg );
	}

	@Override
	public void initUpload(int fileSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_INIT_PROCESS;
		msg.arg1 = fileSize;
		handler.sendMessage(msg );
	}
	
	/**
	 * 上传服务器响应回调
	 */
	@Override
	public void onUploadDone(int responseCode, String message) {
		progressDialog.dismiss();
		Message msg = Message.obtain();
		msg.what = UPLOAD_FILE_DONE;
		msg.arg1 = responseCode;
		msg.obj = message;
		handler.sendMessage(msg);
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_INIT_PROCESS:
				//初始化上传参数
//				progressBar.setMax(msg.arg1);
				break;
			case UPLOAD_PROCESSING:
				//更新上传进度
//				progressBar.setProgress(msg.arg1);
				break;
			case UPLOAD_FILE_DONE:
				//上传完成打印，根据上传结果显示上传图片
				switch (msg.arg1) {
				case UploadUtil.UPLOAD_SUCCESS_CODE:
					// 1.上传成功，则保存上传图片并显示
					doToast(RegisterInformActivity.this, "头像保存成功",Toast.LENGTH_SHORT);
					PersonalInfoPojo personInfo = PersonalUtil.getPersonInfo(RegisterInformActivity.this);
					String imageUrl=(String) msg.obj;
					personInfo.photoPath=imageUrl;
					System.out.println("JIWAII  imageUrl  ==  "+imageUrl);
					PersonalUtil.savePersonInfo(RegisterInformActivity.this, personInfo);
					BitmapUtils bitmapUtils = new BitmapUtils(RegisterInformActivity.this);
					bitmapUtils.display(mPhotoView , imageUrl);
				    BitmapDisplayConfig config = new BitmapDisplayConfig();  
				    config.setLoadingDrawable(getResources().getDrawable(R.drawable.default_avatar));  
				    config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.default_avatar));  
				    bitmapUtils.display(mPhotoView, imageUrl, config);
					break;
				case UploadUtil.UPLOAD_FILE_NOT_EXISTS_CODE:
					doToast(RegisterInformActivity.this, "文件不存在",Toast.LENGTH_SHORT);
					break;
				case UploadUtil.UPLOAD_SERVER_ERROR_CODE:
					doToast(RegisterInformActivity.this, (String) msg.obj,Toast.LENGTH_SHORT);
					break;
				}
				//删除预备上传图并显示
//				File file=new File(ICON_PATH);
//				if (file.exists()) {
//					file.delete();
//				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

}
