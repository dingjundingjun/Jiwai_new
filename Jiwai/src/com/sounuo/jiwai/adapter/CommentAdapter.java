package com.sounuo.jiwai.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
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
import com.umeng.socialize.bean.UMComment;

import java.util.List;

/**
 * Created by dj on 2015/8/12.
 * email:dingjun0225@gmail.com
 */
public class CommentAdapter extends BaseAdapter
{
    private List<UMComment> mUMCommentList;
    private ImageLoader mLoader;
    private DisplayImageOptions mOptions;
    private Context mContext;
    public CommentAdapter(Context context) {
    	mContext = context;
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        mOptions = builder.displayer(new FadeInBitmapDisplayer(200, true, true, true)).build();
        mLoader = ImageLoader.getInstance();
        mLoader.init(ImageLoaderConfiguration.createDefault(mContext));
    }

    @Override
    public int getCount() {
        if(mUMCommentList != null)
        {
//            Debug.d("mUMCommentList.size() = " + mUMCommentList.size());
            return mUMCommentList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mUMCommentList.get(position);
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
            convertView = View.inflate(mContext, R.layout.comment_layout_item, null);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }

        UMComment umData = (UMComment) getItem(position);
        viewHolder.content.setText(umData.mText);
        viewHolder.date.setText(DateFormat.format("yyyy-MM-dd kk:mm", umData.mDt).toString());
        viewHolder.name.setText(umData.mUname);
        String pUrl = umData.mUserIcon;
        if(pUrl != null && !pUrl.equals("")&&!pUrl.equals("http://fake_icon"))
        {
        	mLoader.displayImage(pUrl, viewHolder.avatar, mOptions, new SimpleImageLoadingListener());
        }
        else
        {
        	viewHolder.avatar.setImageResource(R.drawable.default_avatar);
        }
        return convertView;
    }

    static class ViewHolder
    {
        TextView content;
        TextView date;
        TextView name;
        ImageView avatar;
    }

    public void setList(List<UMComment> list)
    {
        mUMCommentList = list;
    }
}
