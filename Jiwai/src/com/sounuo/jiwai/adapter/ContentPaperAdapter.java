package com.sounuo.jiwai.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;

/**
 * Created by dj on 2015/7/18.
 * email:dingjun0225@gmail.com
 */
public class ContentPaperAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList;
    public ContentPaperAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mList = list;
    }

    public Fragment getItem(int position) {
        return mList.get(position);
    }

    public int getCount() {
        return mList.size();
    }
}
