package com.sounuo.jiwai.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sounuo.jiwai.R;
import com.sounuo.jiwai.ReadActivity;
import com.sounuo.jiwai.adapter.ReadAdapter;
import com.sounuo.jiwai.data.ReadBaseCatalogPojo;
import com.sounuo.jiwai.data.ReadCatalogPojo;
import com.sounuo.jiwai.data.ReadTitleData;
import com.sounuo.jiwai.utils.Debug;
import com.sounuo.jiwai.utils.Util;
import com.sounuo.jiwai.views.AutoListView;
import com.sounuo.jiwai.views.AutoListView.OnRefreshListener;
import com.sounuo.jiwai.views.Titanic;
import com.sounuo.jiwai.views.TitanicTextView;
/**
 * 该类是每个分类的内容，比如（看世界、笑话）
 * 
 * */
public class ReadBaseFragment extends Fragment{
	private ReadTitleData mReadTitleData;
	public ArrayList<ReadCatalogPojo> mCatalogPojo;
    public ReadAdapter mAdapter;
    public Activity mBaseActivity;
    public String mTitle = "";
//    public AsyncHttpClient mAsyncHttpClient;
    private Titanic mTitanic;
    public AutoListView mAutoListView;
    private TitanicTextView mTitanicTextView;
    public RelativeLayout mMainLayout;
    protected String mCatalogUrl;
    public Button mReloadBtn;
    private final int GET_CATALOG_NUM = 10;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mBaseActivity = getActivity();
//        mAsyncHttpClient = new AsyncHttpClient();
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.reflash_list_layout, null);
        init(view);
        return view;
    }

    public void setReadTitleData(ReadTitleData rtd) {
    	mReadTitleData = rtd;
    	mTitle = mReadTitleData.getTitle();
    }
    
    public String getTitle()
    {
    	Debug.d("getTitle = " + mTitle);
        return mTitle;
    }

    public void showLoading(TitanicTextView view)
    {
        if(mTitanic == null)
        {
            mTitanic = new Titanic();
        }
        mTitanic.start(view);
        view.setVisibility(View.VISIBLE);
    }

    public void hideLoading(TitanicTextView view)
    {
        if(mTitanic == null)
        {
            return;
        }
        mTitanic.cancel();
        view.setVisibility(View.GONE);
    }

    private void init(View layout)
    {
        mTitanicTextView = (TitanicTextView)layout.findViewById(R.id.titanic_text);
        mReloadBtn = (Button)layout.findViewById(R.id.reload);
        mAutoListView = (AutoListView)layout.findViewById(R.id.auto_list);
        mMainLayout = (RelativeLayout)layout.findViewById(R.id.main_layout);
        mReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReloadBtn.setVisibility(View.GONE);
//                getCatalogListFromServer();
                getCatalogListFromHead();
                loading();
            }
        });
        mAdapter = new ReadAdapter(getActivity());
        loading();
//        getCatalogListFromServer();
        getCatalogListFromHead();
        mAutoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", mCatalogPojo);
                bundle.putInt("index_id",position);
                Intent intent = new Intent(mBaseActivity, ReadActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        
        mAutoListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getCatalogListFromHead();
//				getCatalogListFromServer();
			}
		});
    }

    
    
    public void loading()
    {
        mAutoListView.setVisibility(View.GONE);
        showLoading(mTitanicTextView);
    }

    public void loadingComplete()
    {
    	Debug.d("loadingComplete");
        mAutoListView.setVisibility(View.VISIBLE);
        mAutoListView.onLoadComplete();
        mAutoListView.onRefreshComplete();
        hideLoading(mTitanicTextView);
        if(mReloadBtn.isShown())
        {
            mReloadBtn.setVisibility(View.GONE);
        }
    }

    public void loadingFailed()
    {
        hideLoading(mTitanicTextView);
        mReloadBtn.setVisibility(View.VISIBLE);
    }
    
    private int mergeList(ArrayList<ReadCatalogPojo> srcList,ArrayList<ReadCatalogPojo> sourceList) {
    	int srcCount = srcList.size();
    	int sourceCount = sourceList.size();
    	int mergeNum = 0;
    	boolean bAdd = true;
    	for(int j = 0; j < sourceCount; j++) {
    	    ReadCatalogPojo p = sourceList.get(j);
    		for(int i = 0 ; i < srcCount; i++) {
    		    ReadCatalogPojo srcPojo = sourceList.get(i);
                if(srcPojo != null && p != null && srcPojo.getTitle() != null && p.getTitle() != null) {
                	if(srcPojo.getTitle().equals(p.getTitle())) {
                		bAdd = false;
                		break;
                	}
                }
          	}
    		if(bAdd == false) {
    			bAdd = true;
    			continue;
    		} else {
    			srcList.add(p);
    			mergeNum++;
    		}
    	}
    	return mergeNum;
    }
    
    private void getCatalogListFromHead() {
    	if(mReadTitleData == null || mReadTitleData.getUrl().isEmpty())
        {
            return;
        }
        Debug.d("mReadTitleData = " + mReadTitleData.getUrl());
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // HH:mm:ss
        String time = formatter.format(Calendar.getInstance().getTime());
        HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		String dataUrl = "http://watchworld2.sounuo.net/article/getOld";//mReadTitleData.getUrl() + "//1//" + GET_CATALOG_NUM;
		Debug.d("dataUrl = " + dataUrl + " time = " + time + "mReadTitleData.getClassify_id() = " + mReadTitleData.getClassify_id());
		RequestParams params = new RequestParams();
		params.addBodyParameter("lastTime", time);
		params.addBodyParameter("classifyId", ""+mReadTitleData.getClassify_id());
		params.addBodyParameter("batchCount", ""+10);
		http.send(HttpRequest.HttpMethod.POST, dataUrl,params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						Debug.d("onStart");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						Debug.d("onLoading");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						String status = "";
						Debug.d("result = " + result);
						try {
							JSONObject jsono = new JSONObject(result);
							status = jsono.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						if(!Util.isEmpty(result) && status.equals("success"))
		                {
		                    Gson gson = new Gson();
		                    ReadBaseCatalogPojo pojo = gson.fromJson(result, ReadBaseCatalogPojo.class);
		                    if(pojo.getStatus().equals("success"))
		                    {
		                        Debug.d("json = " + result);
		                        ArrayList<ReadCatalogPojo> tempList = pojo.getmsg();
		                        if(tempList != null )
		                        {
		                        	if(mCatalogPojo != null && tempList.size() == mCatalogPojo.size())
		                        	{
		                        		mAdapter.setList(mCatalogPojo);
		                                mAutoListView.setAdapter(mAdapter);
		                                mAdapter.notifyDataSetChanged();
		                        		Debug.d("there is no new news ");
		                        		Toast.makeText(mBaseActivity, R.string.there_is_no_new_catalog_seeworld, Toast.LENGTH_SHORT).show();
		                        	}
		                        	else
		                        	{
		                        		mCatalogPojo = pojo.getmsg();
		                        		Debug.d("mCatalogPojo size =  " + mCatalogPojo.size());
		                        		mAdapter.setList(mCatalogPojo);
		                                mAutoListView.setAdapter(mAdapter);
		                                mAdapter.notifyDataSetChanged();
		                        	}
//		                        	Util.saveFreshTime(mBaseActivity);
//		                        	saveJson(arg2);
		                        }
		                        loadingComplete();
		                    }
		                }
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Debug.d("onLoading msg = " + msg);
						loadingFailed();
		                mAutoListView.setVisibility(View.GONE);
		                Toast.makeText(mBaseActivity, mBaseActivity.getResources().getString(R.string.get_list_failed), Toast.LENGTH_SHORT).show();
					}
				});
    }
    
    private void getCatalogListFromFoot() {
    	
    }
    
    /**
     * 根据分类ID，获取该分类的目录，每次获取固定的大小
     */
    public void getCatalogListFromServer()
    {
        if(mReadTitleData == null || mReadTitleData.getUrl().isEmpty())
        {
            return;
        }
        Debug.d("mReadTitleData = " + mReadTitleData.getUrl());
        HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		String dataUrl = mReadTitleData.getUrl() + "/1/1/10";
		http.send(HttpRequest.HttpMethod.GET, mReadTitleData.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						Debug.d("onStart");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						Debug.d("onLoading");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						if(!Util.isEmpty(result))
		                {
		                    Gson gson = new Gson();
		                    ReadBaseCatalogPojo pojo = gson.fromJson(result, ReadBaseCatalogPojo.class);
		                    if(pojo.getStatus().equals("success"))
		                    {
		                        Debug.d("json = " + result);
		                        ArrayList<ReadCatalogPojo> tempList = pojo.getmsg();
		                        if(tempList != null )
		                        {
		                        	if(mCatalogPojo != null && tempList.size() == mCatalogPojo.size())
		                        	{
		                        		Debug.d("there is no new news ");
		                        		mAdapter.setList(mCatalogPojo);
		                                mAutoListView.setAdapter(mAdapter);
		                                mAdapter.notifyDataSetChanged();
		                        		Toast.makeText(mBaseActivity, R.string.there_is_no_new_catalog_seeworld, Toast.LENGTH_SHORT).show();
		                        	}
		                        	else
		                        	{
		                        		mCatalogPojo = pojo.getmsg();
		                                mAdapter.setList(mCatalogPojo);
		                                mAutoListView.setAdapter(mAdapter);
		                                mAdapter.notifyDataSetChanged();
		                        	}
//		                        	Util.saveFreshTime(mBaseActivity);
//		                        	saveJson(arg2);
		                        }
		                        loadingComplete();
		                    }
		                }
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Debug.d("onLoading msg = " + msg);
						loadingFailed();
		                mAutoListView.setVisibility(View.GONE);
		                Toast.makeText(mBaseActivity, mBaseActivity.getResources().getString(R.string.get_list_failed), Toast.LENGTH_SHORT).show();
					}
				});
    }
    
    public void notifiCatalogRefresh(String json)
    {
    	Gson gson = new Gson();
        ReadBaseCatalogPojo pojo = gson.fromJson(json, ReadBaseCatalogPojo.class);
        if(pojo.getStatus().equals("success"))
        {
//            Debug.d("json = " + json);
            ArrayList<ReadCatalogPojo> tempList = pojo.getmsg();
            if(tempList != null )
            {
        		mCatalogPojo = pojo.getmsg();
                mAdapter.setList(mCatalogPojo);
                mAutoListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            loadingComplete();
            mAutoListView.noLoadDate();
        }
    }
    
//    abstract protected  void getDataFromLocalOrServer();
//    abstract public void getCatalogListFromLocal();
//    abstract protected void saveJson(String json);

}
