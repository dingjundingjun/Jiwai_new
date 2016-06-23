package com.sounuo.jiwai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sounuo.jiwai.R;
import com.sounuo.jiwai.data.ReadCatalogPojo;
import com.sounuo.jiwai.utils.Debug;

import java.io.File;
import java.util.List;

/**
 * Created by dj on 2015/7/31.
 * email:dingjun0225@gmail.com
 */
public class ReadAdapter extends BaseAdapter
{
    private ImageLoader mLoader;
    private DisplayImageOptions mOptions;
    private List<ReadCatalogPojo> mCatalogList;
    private int mDisplayWidth;
    private Context mContext;
    public void setList(List<ReadCatalogPojo> list)
    {
        mCatalogList = list;
    }

    public ReadAdapter(Context c) {
    	mContext = c;
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        mOptions = builder.displayer(new FadeInBitmapDisplayer(200, true, true, true)).build();
        mLoader = ImageLoader.getInstance();
        mLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        DisplayMetrics display = mContext.getResources().getDisplayMetrics();
        mDisplayWidth = display.widthPixels;
    }

    @Override
    public int getCount() {
        return mCatalogList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder viewHolder;
        if(convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.read_list_item_layout, null);
            viewHolder.catalogText = (TextView) convertView.findViewById(R.id.catalog_text);
            viewHolder.catalogPic = (ImageView) convertView.findViewById(R.id.catalog_pic);
            viewHolder.catalogTime = (TextView)convertView.findViewById(R.id.catalog_time_text);
            viewHolder.catalogBrief = (TextView)convertView.findViewById(R.id.brief);
            convertView.setTag(viewHolder);
        }
        ReadCatalogPojo cp = mCatalogList.get(position);
        viewHolder.catalogText.setText(cp.getTitle());
        String time = cp.getCreatetime();
        Debug.d("time = " + time);
        time = time.substring(0, time.indexOf("T"));
//        viewHolder.catalogTime.setText(cp.getCreateTime());
//        viewHolder.catalogTime.setVisibility(View.GONE);
        viewHolder.catalogTime.setText(time);
//        viewHolder.catalogBrief.setText(cp.getBrief());
        String pUrl = cp.getSnapshots();
        viewHolder.catalogPic.getLayoutParams().height = mDisplayWidth/4;
        if(pUrl != null)
        {
        	mLoader.displayImage(pUrl, viewHolder.catalogPic, mOptions, new SimpleImageLoadingListener());
        	//先判断本地是否有缓存，有则从缓存加载，没有则从网络加载并且下载
//        	File file = new File("/mnt/sdcard/叽歪/目录/" + cp.getTitle() + ".jpg");
//        	if(!file.exists())
//        	{
//        		mLoader.displayImage(pUrl, viewHolder.catalogPic, mOptions, new SimpleImageLoadingListener());
//        		Util.downloadCatalogPic(mContext, cp.getTitle()+".jpg", pUrl);
//        	}
//        	else
//        	{
//        		mLoader.displayImage("file:///mnt/sdcard/叽歪/目录/" + cp.getTitle() + ".jpg", viewHolder.catalogPic,mOptions);
//        	}
        	viewHolder.catalogPic.setVisibility(View.VISIBLE);
        }
        else
        {
        	viewHolder.catalogPic.setVisibility(View.GONE);
        }
        return convertView;
    }
    
    static class ViewHolder
    {
        TextView catalogText;
        ImageView catalogPic;
        TextView catalogTime;
        TextView catalogBrief;
    }

}
