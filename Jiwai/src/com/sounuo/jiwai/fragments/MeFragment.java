package com.sounuo.jiwai.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
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

public class MeFragment extends Fragment implements OnClickListener{
	
	private ItemLinearLayout itemPic;
	private ItemLinearLayout itemCollection;
	private GridView gvCollection;
	private List<ReadTitleData> readTitleDatas;
	private Boolean isTest=true;
    private List<ItemLinearLayout> mItemList = new ArrayList<ItemLinearLayout>();
	private ImageView enterEdit;
    public List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private int mFrontFragment = -1;
    private final int PIC = 1;
    private final int COLLECTION = 2;
    private FragmentManager mFragmentManager = null;
    private FragmentTransaction mTransaction = null;
	private MeCollectionFragment mCollectionFragment;
	private MePicFragment mPicFragment;
	private FrameLayout frameContent;
	
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
		
		
		setDefaultFragment();
		
		
		showPicNums();
		
		showCollectionNums();

		return inflate;
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
		enterEdit = (ImageView) inflate.findViewById(R.id.enter_edit);
		frameContent = (FrameLayout) inflate.findViewById(R.id.frame_content);
		enterEdit.setOnClickListener(this);
		itemCollection.setOnClickListener(this);
		itemPic.setOnClickListener(this);
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
		case R.id.enter_edit:
			ActivityHelper.enterEdit(getActivity());
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
	

}
