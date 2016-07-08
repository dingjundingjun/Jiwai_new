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
import android.view.View;

import com.sounuo.jiwai.fragments.ReadFragment;
import com.sounuo.jiwai.fragments.MeFragment;
import com.sounuo.jiwai.utils.Util;
import com.sounuo.jiwai.views.TabView;

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
    private int mFrontFragment = -1;
    private final int READ = 0;
    private final int ME = 1;
    private final int COMMU = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catalog_main);
		init();
	}
	
	private void init() {
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
        setDefaultFragment();
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
//        	if(Util.getLogined(this) == false)
//        	{
//        		Toast.makeText(this, R.string.please_login, Toast.LENGTH_SHORT).show();
//        		return;
//        	}
//            mCommunionBtn.setSelected(true);
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
