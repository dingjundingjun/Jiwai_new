package com.sounuo.jiwai.fragments;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sounuo.jiwai.R;
import com.sounuo.jiwai.adapter.ContentPaperAdapter;
import com.sounuo.jiwai.data.ReadBaseCatalogPojo;
import com.sounuo.jiwai.data.ReadCatalogPojo;
import com.sounuo.jiwai.data.ReadTitleData;
import com.sounuo.jiwai.data.ReadTitleDataPojo;
import com.sounuo.jiwai.utils.Debug;
import com.sounuo.jiwai.utils.Util;
import com.sounuo.jiwai.views.FragmentViewPaper;
import com.sounuo.jiwai.views.TitleScroolView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by dj on 2015/7/17.
 * email:dingjun0225@gmail.com
 */
public class ReadFragment extends Fragment{
	private final String TAG = "ReadFragment";
	private boolean TEST = false;
	private View mParentView;
    public Activity mBaseActivity;
    public FragmentViewPaper mFragmentViewPaper;
    public List<Fragment> mFragmentList = new ArrayList<Fragment>();
    public List<String> mTitleListStr = new ArrayList<String>();
    private TitleScroolView mTitleScroolView;
    public int mCurrentIndex = 0;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseActivity = getActivity();
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.catalog_read_fragment_content, null);
        mParentView = view;
        init();
        return mParentView;
    }

    public void updateFragmentList()
    {
        mTitleScroolView.setTitleList(mTitleListStr);
        mFragmentViewPaper.setAdapter(new ContentPaperAdapter(getChildFragmentManager(), mFragmentList));
        mFragmentViewPaper.setCurrentItem(0);
        mTitleScroolView.setTitlePressed(0);
        mFragmentViewPaper.setOnPageChangeListener(new BaseOnPageChangeListener());
    }

    private void init()
    {
    	Debug.d("init ReadFragment");
        mTitleScroolView = (TitleScroolView)mParentView.findViewById(R.id.titlescrool);
        mTitleScroolView.init();
        mTitleScroolView.setOnTitleClickListener(new TitleScroolView.OnTitleClickListener() {
            @Override
            public void onClick(int id) {
                mFragmentViewPaper.setCurrentItem(id);
            }
        });
        mFragmentViewPaper = (FragmentViewPaper)mParentView.findViewById(R.id.viewpager);
        mFragmentViewPaper.setOnPageChangeListener(new BaseOnPageChangeListener());
        getReadTitle();
    }

    public class BaseOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageSelected(int arg0) {
            mTitleScroolView.setTitlePressed(arg0);
            mCurrentIndex = arg0;
        }
    }
    
	public void getReadTitle() {
		if(!TEST) {
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpRequest.HttpMethod.GET, Util.READ_CATALOG_CALSSIFY_URL,
		// params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						Debug.d("result = " + result);
						if(!Util.isEmpty(result))
		                {
		                    Gson gson = new Gson();
		                    ReadTitleDataPojo pojo = gson.fromJson(result, ReadTitleDataPojo.class);
		                    if(pojo.getStatus().equals("success"))
		                    {
		                        Debug.d("json = " + result);
		                        ArrayList<ReadTitleData> tempList = pojo.getMessage();
		                        if(tempList != null )
		                        {
		                        	mTitleListStr.clear();
		                        	for(int i = 0; i < tempList.size();i++) {
			                			ReadBaseFragment fragment = new ReadBaseFragment();
			                			fragment.setReadTitleData(tempList.get(i));
			                			mFragmentList.add(fragment);
			                			mTitleListStr.add(fragment.getTitle());
		                        	}
		                        	updateFragmentList();
		                        }
		                    }
		                }
					}

					@Override
					public void onFailure(HttpException error, String msg) {
					}
				});
		} else {
			ReadTitleData seeWorld = new ReadTitleData();
			seeWorld.setTitle("看世界");
			seeWorld.setUrl("http://121.40.93.89:13090/article/getWatchWorldList");
			ReadBaseFragment seeWorldFragment = new ReadBaseFragment();
			seeWorldFragment.setReadTitleData(seeWorld);
			mFragmentList.add(seeWorldFragment);
			
			ReadTitleData joke = new ReadTitleData();
			joke.setTitle("笑一校");
			joke.setUrl("http://121.40.93.89:13090/article/getJokeList");
			ReadBaseFragment jokeFragment = new ReadBaseFragment();
			jokeFragment.setReadTitleData(joke);
			mFragmentList.add(jokeFragment);
			
			mTitleListStr.clear();
	        mTitleListStr.add(seeWorld.getTitle());
	        mTitleListStr.add(joke.getTitle());
	        updateFragmentList();
		}
	}
}
