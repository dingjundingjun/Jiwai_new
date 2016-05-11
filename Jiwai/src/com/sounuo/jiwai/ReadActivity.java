package com.sounuo.jiwai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import com.sounuo.jiwai.data.ReadCatalogPojo;
import com.sounuo.jiwai.fragments.ReadBaseContentFragment;
import com.sounuo.jiwai.views.FragmentViewPaper;
import android.support.v4.view.ViewPager;
/**
 * Created by dj on 2015/7/31.
 * email:dingjun0225@gmail.com
 */
public class ReadActivity extends FragmentActivity
{
    private ArrayList<ReadCatalogPojo> mCatalogPojo;
    public FragmentViewPaper mFragmentViewPaper;
    private FragmentManager mFragmentManager = null;
    private int mIndexId = -1;
    private List<ReadBaseContentFragment> mBaseContentList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_parent_layout);
        init();
    }

    private void init()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mCatalogPojo = (ArrayList<ReadCatalogPojo>)bundle.getSerializable("list");
        mIndexId = bundle.getInt("index_id");
        mBaseContentList = new ArrayList<ReadBaseContentFragment>();
//        for(int i = 0;i < mCatalogPojo.size();i++)
//        {
//            Debug.d("catalog " + i + " = " + mCatalogPojo.get(i).getTitle());
//            BaseContentFragment base = new BaseContentFragment();
//            mBaseContentList.add(base);
//        }
        ReadCatalogPojo catalogPojo = mCatalogPojo.get(mIndexId);
        ReadBaseContentFragment base = new ReadBaseContentFragment();
        base.setCatalogPojo(catalogPojo);
        mBaseContentList.add(base);

        mFragmentManager = this.getSupportFragmentManager();
        mFragmentViewPaper = (FragmentViewPaper)findViewById(R.id.viewpager);
        mFragmentViewPaper.setAdapter(new PaperAdapter(mFragmentManager, mBaseContentList));
        mFragmentViewPaper.setCurrentItem(0);
        mFragmentViewPaper.setOnPageChangeListener(new BaseOnPageChangeListener());
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();

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
    }
}

    public class PaperAdapter extends FragmentPagerAdapter {
        private List<ReadBaseContentFragment> mList;
        public PaperAdapter(FragmentManager fm, List<ReadBaseContentFragment> list) {
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
}
