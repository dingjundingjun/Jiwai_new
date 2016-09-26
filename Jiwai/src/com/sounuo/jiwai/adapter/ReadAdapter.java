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
	private String[] testTitleData = { "懂得感恩和珍惜，才能获得人生最大的收获", "人生有度，要懂进退取舍之道" };
	private String[] testTextData = {
			"懂得感恩和珍惜，才能获得人生最大的收获——快乐和幸福，学会忘记，让身心轻松；懂得舍得，让生活变得更加和谐美丽。忘记是一种风度，舍得是一种智慧。更要懂得舍得的真谛，懂得忘记的心灵升华，让精神得到提升，懂得舍得会活得很精彩",
			"“过犹不及”，出自《论语·先进》，简言之，即做事超过或不够，都是不合适的。“过犹不及”的处世之道，就在于讲究一个“度”字，它不仅渗透于人与人之间的相处，更在于个人的修养。说话做事也好，学习享乐也罢，如果不能做到进退有度，取舍有度，就很可能陷入物极必反的桎梏之境。" };
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
            viewHolder.catalogPicLeft = (ImageView) convertView.findViewById(R.id.catalog_pic_left);
            viewHolder.catalogPicCenter = (ImageView) convertView.findViewById(R.id.catalog_pic_center);
            viewHolder.catalogPicRight = (ImageView) convertView.findViewById(R.id.catalog_pic_right);
            viewHolder.catalogTime = (TextView)convertView.findViewById(R.id.catalog_time_text);
            viewHolder.catalogBrief = (TextView)convertView.findViewById(R.id.brief);
            convertView.setTag(viewHolder);
        }
        ReadCatalogPojo cp = mCatalogList.get(position);
        viewHolder.catalogText.setText(cp.getTitle());
//        viewHolder.catalogText.setText(testTitleData[position%2]);
        viewHolder.catalogBrief.setText(cp.getPreview());
        String time = cp.getCreatetime();
        Debug.d("time = " + time);
        time = time.substring(0, time.indexOf("T"));
//        viewHolder.catalogTime.setText(cp.getCreateTime());
//        viewHolder.catalogTime.setVisibility(View.GONE);
        viewHolder.catalogTime.setText(time);
//        viewHolder.catalogBrief.setText(cp.getBrief());
        cp.splitPic(cp.getSnapshots());
        String pUrl[] = cp.getPicUrl();
//        viewHolder.catalogPic.getLayoutParams().height = mDisplayWidth/4;
        viewHolder.catalogPicCenter.setVisibility(View.GONE);
    	viewHolder.catalogPicRight.setVisibility(View.GONE);
        if(pUrl != null && pUrl.length > 0)
        {
        	convertView.findViewById(R.id.catalog_pic_layout).setVisibility(View.VISIBLE);
        	if(pUrl.length >= 3) {
        		viewHolder.catalogPicCenter.setVisibility(View.VISIBLE);
            	viewHolder.catalogPicRight.setVisibility(View.VISIBLE);
            	mLoader.displayImage(pUrl[0], viewHolder.catalogPicLeft, mOptions, new SimpleImageLoadingListener());
            	mLoader.displayImage(pUrl[1], viewHolder.catalogPicCenter, mOptions, new SimpleImageLoadingListener());
            	mLoader.displayImage(pUrl[2], viewHolder.catalogPicRight, mOptions, new SimpleImageLoadingListener());
        	} else if(pUrl.length >= 2) {
        		viewHolder.catalogPicCenter.setVisibility(View.VISIBLE);
            	mLoader.displayImage(pUrl[0], viewHolder.catalogPicLeft, mOptions, new SimpleImageLoadingListener());
            	mLoader.displayImage(pUrl[1], viewHolder.catalogPicCenter, mOptions, new SimpleImageLoadingListener());
        	} else if(pUrl.length >= 1) {
        		mLoader.displayImage(pUrl[0], viewHolder.catalogPicLeft, mOptions, new SimpleImageLoadingListener());
                viewHolder.catalogPicCenter.setVisibility(View.GONE);
                viewHolder.catalogPicRight.setVisibility(View.GONE);
        	} 
//        	mLoader.displayImage(pUrl, viewHolder.catalogPic, mOptions, new SimpleImageLoadingListener());
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
//        	viewHolder.catalogPic.setVisibility(View.VISIBLE);
        }
        else
        {
        	convertView.findViewById(R.id.catalog_pic_layout).setVisibility(View.GONE);
        }
//        if(position%3 == 0) {    //3张
//        	viewHolder.catalogPicCenter.setVisibility(View.VISIBLE);
//        	viewHolder.catalogPicRight.setVisibility(View.VISIBLE);
//        	mLoader.displayImage("drawable://" + R.drawable.pic_1, viewHolder.catalogPicLeft, mOptions, new SimpleImageLoadingListener());
//        	mLoader.displayImage("drawable://" + R.drawable.pic_2, viewHolder.catalogPicCenter, mOptions, new SimpleImageLoadingListener());
//        	mLoader.displayImage("drawable://" + R.drawable.pic_3, viewHolder.catalogPicRight, mOptions, new SimpleImageLoadingListener());
//        } else if(position%3 == 1) {  //2张
//        	viewHolder.catalogPicCenter.setVisibility(View.VISIBLE);
//        	mLoader.displayImage("drawable://" + R.drawable.pic_1, viewHolder.catalogPicLeft, mOptions, new SimpleImageLoadingListener());
//        	mLoader.displayImage("drawable://" + R.drawable.pic_2, viewHolder.catalogPicCenter, mOptions, new SimpleImageLoadingListener());
//            viewHolder.catalogPicRight.setVisibility(View.GONE);
//        } else if(position%3 == 2) {
//        	mLoader.displayImage("drawable://" + R.drawable.pic_1, viewHolder.catalogPicLeft, mOptions, new SimpleImageLoadingListener());
//            viewHolder.catalogPicCenter.setVisibility(View.GONE);
//            viewHolder.catalogPicRight.setVisibility(View.GONE);
//        }
        
        return convertView;
    }
    
    static class ViewHolder
    {
        TextView catalogText;
        ImageView catalogPicLeft;
        ImageView catalogPicCenter;
        ImageView catalogPicRight;
        TextView catalogTime;
        TextView catalogBrief;
    }

}
