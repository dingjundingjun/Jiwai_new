package com.sounuo.jiwai.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import com.sounuo.jiwai.views.FragmentViewPaper;
import com.sounuo.jiwai.views.TitleScroolView;

/**
 * Created by dj on 2015/7/17.
 * email:dingjun0225@gmail.com
 */
public class BaseFragment extends Fragment {
    private View mParentView;
    public Activity mBaseActivity;
    public FragmentViewPaper mFragmentViewPaper;
    public List<SeeWorldAndJokeChildBaseFragment> mFragmentList = new ArrayList<SeeWorldAndJokeChildBaseFragment>();
    public List<String> mTitleListStr = new ArrayList<String>();
    private TitleScroolView mTitleScroolView;
    public int mCurrentIndex = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseActivity = getActivity();
        View view = LayoutInflater.from(mBaseActivity).inflate(R.layout.catalog_read_fragment_content.xml, null);
        mParentView = view;
        return mParentView;
    }

    public void updateFragmentList()
    {
        init();
    }

    private void init()
    {
        mTitleScroolView = (TitleScroolView)mParentView.findViewById(R.id.titlescrool);
        mTitleScroolView.setTitleList(mTitleListStr);
        mTitleScroolView.init();
        mTitleScroolView.setOnTitleClickListener(new TitleScroolView.OnTitleClickListener() {
            @Override
            public void onClick(int id) {
                mFragmentViewPaper.setCurrentItem(id);
            }
        });
        mFragmentViewPaper = (FragmentViewPaper)mParentView.findViewById(R.id.viewpager);
        mFragmentViewPaper.setAdapter(new ContentPaperAdapter(getChildFragmentManager(), mFragmentList));
        mFragmentViewPaper.setCurrentItem(0);
        mTitleScroolView.setTitlePressed(0);
        mFragmentViewPaper.setOnPageChangeListener(new BaseOnPageChangeListener());
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
}
