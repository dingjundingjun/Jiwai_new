package com.sounuo.jiwai.guide.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sounuo.jiwai.guide.GuideListener;
import com.sounuo.jiwai.guide.slidingtutorial.PageFragment;
import com.sounuo.jiwai.guide.slidingtutorial.SimplePagerFragment;

public class CustomPresentationPagerFragment extends SimplePagerFragment {

    private GuideListener listener;

    public void setGuideListener(GuideListener l) {
        this.listener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getPagesCount() {
        return 3;
    }

    @Override
    protected PageFragment getPage(int position) {
        position %= 3;
        if (position == 0) {
            return new FirstCustomPageFragment();
        }
        if (position == 1) {
            return new SecondCustomPageFragment();
        }
        if (position == 2) {
            ThirdCustomPageFragment pageFragment = new ThirdCustomPageFragment();
            pageFragment.setGuideListener(new GuideListener() {
                @Override
                public void needFinish() {
                    if (listener != null) {
                        listener.needFinish();
                    }
                }
            });
            return pageFragment;
        }

        throw new IllegalArgumentException("Unknown position: " + position);
    }

    //设置引导页背景色
    @ColorInt
    @Override
    protected int getPageColor(int position) {
        if (position == 0) {
            return Color.parseColor("#95DFC2");
        }
        if (position == 1) {
            return Color.parseColor("#849AE0");
        }
        if (position == 2) {
            return Color.parseColor("#94DDF4");
        }
        return Color.TRANSPARENT;
    }

    //    设置引导页的指示点
    @Override
    protected int getPointFillColor(int position) {
        if (position == 0) {
            return Color.parseColor("#EEEEEE");
        }
        if (position == 1) {
            return Color.parseColor("#EEEEEE");
        }
        if (position == 2) {
            return Color.parseColor("#EEEEEE");
        }
        return Color.TRANSPARENT;
    }

    @Override
    protected int getDefaultPointFillColor() {
        return Color.parseColor("#DDDDDD");
    }

    @Override
    protected boolean isInfiniteScrollEnabled() {
        return false;
    }

    @Override
    protected boolean onSkipButtonClicked(View skipButton) {
        Toast.makeText(getContext(), "Skip button clicked", Toast.LENGTH_SHORT).show();
        return true;
    }
}
