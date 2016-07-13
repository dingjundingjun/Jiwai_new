package com.sounuo.jiwai.fragments;

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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.sounuo.jiwai.R;
import com.sounuo.jiwai.RegisterInformActivity;
import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.data.ReadTitleData;
import com.sounuo.jiwai.utils.ActivityHelper;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.FileUtil;
import com.sounuo.jiwai.utils.MD5;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.SharedPrefUtil;
import com.sounuo.jiwai.utils.UploadUtil;
import com.sounuo.jiwai.utils.UploadUtil.OnUploadProcessListener;
import com.sounuo.jiwai.utils.Util;
import com.sounuo.jiwai.views.ChangeColorTextView;
import com.sounuo.jiwai.views.CircleImageView;
import com.sounuo.jiwai.views.ItemLinearLayout;
import com.sounuo.jiwai.views.MyCameraDialog;

public class MeFragment extends Fragment implements OnClickListener, OnUploadProcessListener{
    private static final int REQUEST_CODE_CAMERA_WITH_DATA = 1001;
    
    private static final int REQUEST_CODE_PHOTO_PICKED_WITH_DATA = 1002;
    
    private static final int REQUEST_CROP_PHOTO = 1003;

	private static final String TAG = "JIWAI";
    
	private static String ICON_REAL_PATH = null;

	private static String ICON_PATH;
	
	private static String ICON_TEMP_PATH;
	
	private Uri mCurrentPhotoUri;

	private Uri mTempPhotoUri;

	private Uri mCroppedPhotoUri;
    
	private ItemLinearLayout itemPic;
	private ItemLinearLayout itemCollection;
	private GridView gvCollection;
	private List<ReadTitleData> readTitleDatas;
	private Boolean isTest=true;
    private List<ItemLinearLayout> mItemList = new ArrayList<ItemLinearLayout>();
	private ImageView enterSetting;
    public List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private int mFrontFragment = -1;
    private final int PIC = 1;
    private final int COLLECTION = 2;
    private FragmentManager mFragmentManager = null;
    private FragmentTransaction mTransaction = null;
	private MeCollectionFragment mCollectionFragment;
	private MePicFragment mPicFragment;
	private FrameLayout frameContent;

	private Toast mToast;

	private CircleImageView headImageView;

	private PersonalInfoPojo personInfo;

	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mFragmentManager = this.getChildFragmentManager();
		// 获取头像保存路径
		ICON_PATH = pathForTempPhoto(activity, "head.png");
		// 获取头像缓存保存路径
		ICON_TEMP_PATH = pathForTempPhoto(activity, "head_temp.png");

        mTempPhotoUri = generateTempImageUri(activity);
        
        mCroppedPhotoUri = generateTempCroppedImageUri(activity);
	}
	
    public static Uri generateTempImageUri(Context context) {
        return Uri.fromFile(new File(ICON_TEMP_PATH));
    }
    
    public static Uri generateTempCroppedImageUri(Context context) {
    	return Uri.fromFile(new File( ICON_PATH));
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
	
	public void setCrop() {
		
		MyCameraDialog dialog=new MyCameraDialog(getActivity());
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        
		View inflate = inflater.inflate(R.layout.catalog_me_fragment_content, null);
		
		initViews(inflate);
		
		mCollectionFragment = new MeCollectionFragment();
		mPicFragment = new MePicFragment();
		mFragmentList.add(mCollectionFragment);
		mFragmentList.add(mPicFragment);
		
		personInfo = PersonalUtil.getPersonInfo(getActivity());
		BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
		String imageUrl=personInfo.getPhotoPath();
		bitmapUtils.display(headImageView , imageUrl);
		
		setDefaultFragment();
		showPicNums();
		showCollectionNums();
		progressDialog = new ProgressDialog(getActivity());
		return inflate;
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
            doToast(getActivity(), "无法截图", Toast.LENGTH_LONG);
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
	
//    public void onPhotoSelected() {
//    	headImageView.setImageBitmap(BitmapFactory.decodeFile(ICON_PATH));
//    }
    
    public void onPhotoSelected() {
    	context = getActivity();
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
		String token=PersonalUtil.getPersonInfo(context).token;
		String phone=PersonalUtil.getPersonInfo(context).phone;
		HashMap<String, String> accountHeaders = Util.getAccountHeaders(context, token, phone);
		uploadUtil.uploadFile(ICON_PATH, "img", AppConstant.UPLOAD_HEADIMG, accountHeaders);
		progressDialog.setMessage("正在上传文件...");
		progressDialog.show();
    }
    
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		handlePhotoActivityResult(requestCode, resultCode, data);
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
						savePhotoFromUriToUri(getActivity(), uri, toCrop, false);
					} catch (SecurityException e) {
						Log.d(TAG, "Did not have read-access to uri : " + uri);
						return false;
					}
				}

				doCropPhoto(toCrop, mCroppedPhotoUri);
				return true;
			}
		}else{
			doToast(getActivity(), "选择头像出错", Toast.LENGTH_SHORT);
		}
		return false;
	}
	
	
	private void setDefaultFragment() {
        mFrontFragment = PIC;
        changeFragment();
    }

	private void showCollectionNums() {
		// TODO Auto-generated method stub
		itemCollection.setItemNumText("12");
	}

	private void showPicNums() {
		// TODO Auto-generated method stub
		itemPic.setItemNumText("11");
	}


	private void initViews(View inflate) {
		itemCollection = (ItemLinearLayout) inflate.findViewById(R.id.il_collection);
		itemPic = (ItemLinearLayout) inflate.findViewById(R.id.il_pic);
		enterSetting = (ImageView) inflate.findViewById(R.id.enter_setting);
		headImageView = (CircleImageView) inflate.findViewById(R.id.civ_user_head);
		frameContent = (FrameLayout) inflate.findViewById(R.id.frame_content);
		enterSetting.setOnClickListener(this);
		itemCollection.setOnClickListener(this);
		itemPic.setOnClickListener(this);
		headImageView.setOnClickListener(this);
        mItemList.add(itemCollection);
        mItemList.add(itemPic);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.il_pic:
			// 获取收藏的照片信息
            if (mFrontFragment != PIC) {
                mFrontFragment = PIC;
                changeFragment();
            }
			break;
		case R.id.il_collection:
			// 获取用户收藏信息，展示应该是一个listview之类的，需要等待设计
            if (mFrontFragment != COLLECTION) {
                mFrontFragment = COLLECTION;
                changeFragment();
            }
			break;
		case R.id.enter_setting:
			ActivityHelper.enterSetting(getActivity());
			break;
		case R.id.civ_user_head:
			//仿联系人代码
			setCrop();
			break;
		default:
			break;
		}
        changeTabViewStatus(id);
	}
	
    private void changeFragment() {
        mTransaction = mFragmentManager.beginTransaction();
        if (mFrontFragment == COLLECTION) {
            if (mCollectionFragment == null) {
            	mCollectionFragment = new MeCollectionFragment();
            }
            mTransaction.replace(R.id.frame_content, mCollectionFragment);
//            mReadBtn.setSelected(true);
        } 
        else if(mFrontFragment == PIC)
        {
            if (mPicFragment == null) {
            	mPicFragment = new MePicFragment();
            }
            mTransaction.replace(R.id.frame_content, mPicFragment);
        }
        mTransaction.commit();
    }
	
    public void changeTabViewStatus(int id) {
        int len = mItemList.size();
        for (int i = 0; i < len; i++) {
            boolean bMatchId = mItemList.get(i).getId() == id;
            mItemList.get(i).setSelector(bMatchId);
        }
    }
	protected static final int UPLOAD_INIT_PROCESS = 0;

	protected static final int UPLOAD_PROCESSING = 1;

	protected static final int UPLOAD_FILE_DONE = 2;

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
					doToast(getActivity(), "头像保存成功",Toast.LENGTH_SHORT);
					PersonalInfoPojo personInfo = PersonalUtil.getPersonInfo(context);
					String imageUrl=(String) msg.obj;
					personInfo.photoPath=imageUrl;
					System.out.println("JIWAII  imageUrl  ==  "+imageUrl);
					PersonalUtil.savePersonInfo(context, personInfo);
					BitmapUtils bitmapUtils = new BitmapUtils(context);
					bitmapUtils.display(headImageView , imageUrl);
				    BitmapDisplayConfig config = new BitmapDisplayConfig();  
				    config.setLoadingDrawable(getResources().getDrawable(R.drawable.default_avatar));  
				    config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.default_avatar));  
				    bitmapUtils.display(headImageView, imageUrl, config);
					break;
				case UploadUtil.UPLOAD_FILE_NOT_EXISTS_CODE:
					doToast(context, "文件不存在",Toast.LENGTH_SHORT);
					break;
				case UploadUtil.UPLOAD_SERVER_ERROR_CODE:
					doToast(context, (String) msg.obj,Toast.LENGTH_SHORT);
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

	private Context context;
	

}
