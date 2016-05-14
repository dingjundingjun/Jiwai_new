package com.sounuo.jiwai.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.sounuo.jiwai.R;
import com.sounuo.jiwai.data.ReadTitleData;
import com.sounuo.jiwai.utils.ActivityHelper;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.SharedPrefUtil;
import com.sounuo.jiwai.views.ChangeColorTextView;
import com.sounuo.jiwai.views.ItemLinearLayout;

/**
 * 展示关注的栏目
 * @author gq
 *
 */
public class MeAttentionFragment extends Fragment {
	
	
	private boolean Test=true;
	private ImageView ImageviewNoOne;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View inflate = inflater.inflate(R.layout.me_fragment_attention, null);
		
		initViews(inflate);
		
		getPicFromDb();

		return inflate;
	}


	private void getPicFromDb() {
		if (Test) {
			// 木有照片
			ImageviewNoOne.setVisibility(View.VISIBLE);
		}
		
	}

	private void initViews(View inflate) {
		ImageviewNoOne = (ImageView) inflate.findViewById(R.id.me_attentin_no_one);
	}
	
	
}
