package com.sounuo.jiwai;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.sounuo.jiwai.data.PersonalInfoPojo;
import com.sounuo.jiwai.fragments.ReadFragment;
import com.sounuo.jiwai.fragments.MeFragment;
import com.sounuo.jiwai.utils.Debug;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.Util;
import com.sounuo.jiwai.views.TabView;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.Source;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.simplify.ui.fragments.CommunityMainFragment;

@SuppressLint("NewApi")
public class MainCatalogActivity extends FragmentActivity implements View.OnClickListener {
	/**发现fragment*/
    private ReadFragment mReadFragment;
    /**个人设置fragment*/
    private MeFragment mMeFragment;
    private TabView mReadBtn;
    private TabView mMeBtn;
    private TabView mCommunionBtn;
    private List<TabView> mTabViewList = new ArrayList<TabView>();
    private FragmentManager mFragmentManager = null;
    private FragmentTransaction mTransaction = null;
    private CommunityMainFragment mCommunityMainFragment = null;
    private int mFrontFragment = -1;
    private final int READ = 0;
    private final int ME = 1;
    private final int COMMU = 2;
//    private CommunitySDK mCommSDK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catalog_main);
		init();
	}
	
	private void init() {
//		续签token，失败则跳转回登录界面
        mFragmentManager = this.getSupportFragmentManager();
        mReadBtn = (TabView) findViewById(R.id.btn_read);
        mMeBtn = (TabView) findViewById(R.id.btn_me);
        mCommunionBtn = (TabView)findViewById(R.id.btn_communion);
        
        mReadBtn.setOnClickListener(this);
        mMeBtn.setOnClickListener(this);
        mCommunionBtn.setOnClickListener(this);
        
        mTabViewList.add(mReadBtn);
        mTabViewList.add(mMeBtn);
        mTabViewList.add(mCommunionBtn);
//        CommunitySDK mCommSDK = CommunityFactory.getCommSDK(getApplicationContext());
        // 初始化sdk，请传递ApplicationContext
//        mCommSDK.initSDK(getApplicationContext());        
        mCommunityMainFragment = new CommunityMainFragment();
        //设置Feed流页面的返回按钮不可见
        mCommunityMainFragment.setBackButtonVisibility(View.INVISIBLE);
        //添加并显示Fragment
        setDefaultFragment();
        loginCommunity();
        
    }
	
	private void loginCommunity() {
		CommunitySDK mCommSDK = CommunityFactory.getCommSDK(this);
	    CommUser user = new CommUser();
	    PersonalInfoPojo pi = PersonalUtil.getPersonInfo(this);
	    user.name = pi.nickName;
	    user.id = pi.getAccountId();
	    Debug.d("loginCommunity nickName = " + user.name + " id = " + user.id);
	    mCommSDK.loginToUmengServerBySelfAccount(this, user, new LoginListener() {
	        @Override
	        public void onStart() {

	        }

	        @Override
	        public void onComplete(int stCode, CommUser commUser) {
	        	Debug.d("loginCommunity stCode = " + stCode);
	            if (ErrorCode.NO_ERROR==stCode) {
	                //在此处可以跳转到任何一个你想要的activity
	            }

	       }
	    });
	}
	
	private void setDefaultFragment() {
        mFrontFragment = READ;
        changeFragment();
    }

    private void changeFragment() {
        mTransaction = mFragmentManager.beginTransaction();
        if (mFrontFragment == READ) {
            if (mReadFragment == null) {
            	mReadFragment = new ReadFragment();
            }
            mTransaction.replace(R.id.content, mReadFragment);
            mReadBtn.setSelected(true);
        }
        else if (mFrontFragment == ME) {
            if (mMeFragment == null) {
                mMeFragment = new MeFragment();
            }
            mTransaction.replace(R.id.content, mMeFragment);
            mMeBtn.setSelected(true);
        }
        else if(mFrontFragment == COMMU)
        {
        	mTransaction.replace(R.id.content, mCommunityMainFragment);
            mCommunionBtn.setSelected(true);
        }
        mTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkWifi();
    }

    private void checkWifi()
    {
        if(!Util.checkWifiConnected(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.tip_title)
                    .setMessage(R.string.please_connect_network)
                    .setPositiveButton(R.string.tip_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_read: {
                if (mFrontFragment != READ) {
                    mFrontFragment = READ;
                    changeFragment();
                }
                break;
            }
            case R.id.btn_me: {
                if (mFrontFragment != ME) {
                    mFrontFragment = ME;
                    changeFragment();
                }
                break;
            }
            case R.id.btn_communion:
            {
            	final CommunitySDK mCommSDK = CommunityFactory.getCommSDK(this);
            	mCommSDK.openCommunity(MainCatalogActivity.this);
//            	if(mFrontFragment != COMMU) {
//            		mFrontFragment = COMMU;
//            	    changeFragment();
//            	}
            	break;
            }
        }
        changeTabViewStatus(id);
    }

    public void changeTabViewStatus(int id) {
        int len = mTabViewList.size();
        for (int i = 0; i < len; i++) {
            boolean bMatchId = mTabViewList.get(i).getId() == id;
            mTabViewList.get(i).setSelector(bMatchId);
        }
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
}
