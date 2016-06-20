package com.sounuo.jiwai;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;
import com.sounuo.jiwai.data.ReadTitleData;
import com.sounuo.jiwai.database.ColumnBean;
import com.sounuo.jiwai.utils.ActivityHelper;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.Debug;
import com.sounuo.jiwai.utils.SharedPrefUtil;
import com.sounuo.jiwai.views.ChangeColorTextView;
import com.sounuo.jiwai.views.CircleImageView;

/**
 * 获取用户头像，以及用户关注的栏目
 */
public class RegisterInformActivity extends Activity implements
		View.OnClickListener {
	private static final boolean TEST = false;

	// 本地测试数据
	private List<ColumnBean> columnBeans = new ArrayList<ColumnBean>();

	private boolean isEditMode = false;

	private CircleImageView mPhotoView;

	private static String ICON_PATH;
	private Button mEnterGreatWorld;

	private GridView gridView;

	private ColumnAdapter mColumnAdapter;

	private TextView EtNickName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register_inform);
		
		// 获取头像保存路径
		ICON_PATH = getFilesDir().getAbsolutePath() + File.separator+ "head.png";
		
		Debug.d(ICON_PATH);
		
		// 初始化视图界面
		initviews();

		initAdapter(gridView);
		
	}

	private void initviews() {
		gridView = (GridView) findViewById(R.id.column_grid_view);
		EtNickName = (TextView) findViewById(R.id.edit_text_nickname);
		mPhotoView = (CircleImageView) findViewById(R.id.civ_user_head);
		mEnterGreatWorld = (Button) findViewById(R.id.btn_enter_great_world);
		mEnterGreatWorld.setOnClickListener(this);
		mPhotoView.setOnClickListener(this);
	}

	private void initAdapter(GridView gridView) {
		initData();
		mColumnAdapter = new ColumnAdapter(columnBeans, this);
		gridView.setAdapter(mColumnAdapter);
	}

	
	private void initData() {
		columnBeans.add(new ColumnBean("看世界",  true,"http://121.40.93.89:13090/article/getWatchWorldList"));
		columnBeans.add(new ColumnBean("笑一校",  true,"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("奇闻趣事",  true,"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("校园轶事",  true,"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("美景记事",  true,"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("历史典故",  true,"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("奇闻趣事",  true,"http://121.40.93.89:13090/article/getJokeList"));
		columnBeans.add(new ColumnBean("校园轶事",  true,"http://121.40.93.89:13090/article/getWatchWorldList"));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 111) {
			if (data != null) {
				beginCrop(data.getData());
			}
		} else if (requestCode == Crop.REQUEST_CROP) {
			handleCrop(resultCode, data);
		}
	}

	public void setCrop() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 111);
	}

	private void handleCrop(int resultCode, Intent result) {

		if (resultCode == RESULT_OK && result != null) {
			mPhotoView.setImageBitmap(BitmapFactory.decodeFile(ICON_PATH));
		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(this, Crop.getError(result).getMessage(),Toast.LENGTH_SHORT).show();
		}
	}

	private void beginCrop(Uri source) {
		Uri destination = Uri.fromFile(new File(ICON_PATH));
		Intent cropIntent = new Intent();
		cropIntent.setData(source);
		cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, destination);
		cropIntent.setClass(this, CropImageActivity.class);
		startActivityForResult(cropIntent, Crop.REQUEST_CROP);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.civ_user_head:
			setCrop();
			break;
		case R.id.btn_enter_great_world:
			Gson gson = new Gson();
			String jsonString = gson.toJson(columnBeans);
			SharedPrefUtil.putString(this, AppConstant.CHANNEL_CONTROL, jsonString);
	        SharedPrefUtil.putBoolean(getApplicationContext(), AppConstant.IS_FIRST_ENTER_APP, false);
	        SharedPrefUtil.putInt(getApplicationContext(), AppConstant.ATTENTION_SIZE, mColumnAdapter.getAttentionSize());
	        ActivityHelper.enterMainCatalog(this);
	        finish();
			break;
		}
	}

	class ColumnAdapter extends BaseAdapter {
		List<ColumnBean> mColumnBeans;
		Context mContext;
		String[] colors= new String[]{"#FFD6ECF0","#FFFFA631","#FF9ed046","#FFFF7500","#FFD6ECF0","#FFFFA631","#FF9ed046","#FFFF7500"};

	    private  SparseBooleanArray checkSparseBooleanArray;

	    public SparseBooleanArray getCheckSparseBooleanArray() {
	        return checkSparseBooleanArray;
	    }

	    public void setCheckSparseBooleanArray(SparseBooleanArray checkSparseBooleanArray) {
	        this.checkSparseBooleanArray = checkSparseBooleanArray;
	    }
		
		public ColumnAdapter(List<ColumnBean> readTitleDatas, Context context) {
			this.mColumnBeans = readTitleDatas;
			this.mContext = context;
			checkSparseBooleanArray=new SparseBooleanArray();
//			将全部的栏目设置为true
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
		public View getView(final int position, View convertView,ViewGroup parent) {
			ColumnBean columnBean = mColumnBeans.get(position);
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			View view = layoutInflater.inflate(R.layout.column_grid_view_item,null);
			final ChangeColorTextView columnInfoTextView = (ChangeColorTextView) view.findViewById(R.id.column_info_tv);
			columnInfoTextView.setText(columnBean.getTitle());
			columnInfoTextView.setIconColor(Color.parseColor(colors[position]));
			columnInfoTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(getAttentionSize()<=1){
						Toast.makeText(RegisterInformActivity.this, "最少需要关注一个栏目哦", Toast.LENGTH_SHORT).show();
						return;
					}
					columnInfoTextView.changeSelectColor();
					mColumnBeans.get(position).setIsChecked(columnInfoTextView.isSelected());
					checkSparseBooleanArray.put(position, columnInfoTextView.isSelected());
				}
			});
			return view;
		}
	}

}
