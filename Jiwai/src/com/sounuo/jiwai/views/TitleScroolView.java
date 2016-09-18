package com.sounuo.jiwai.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.sounuo.jiwai.R;
import com.sounuo.jiwai.utils.Debug;

/**
 * Created by dj on 2015/7/19.
 * email:dingjun0225@gmail.com
 */
public class TitleScroolView extends LinearLayout{
    private Context mContext;
    private View mParentView;
    private LinearLayout mContentLayout;
    private List<String> mTitleList;
    private List<TextView> mTitleViewList = new ArrayList<TextView>();
    private HorizontalScrollView mScroolView;
    private OnTitleClickListener mOnTitleClickListener;
    private final int ANIM_DURATION = 500;
    private final int LABEL_STATUS_NORMAL = 0;
    private final int LABEL_STATUS_PRESS = 100;
    private float mTextSize;
    private int mColorNormal;
    private int mColorPress;
    private Handler mHandler =new Handler();
    private int mScroolX = 0;
    public TitleScroolView(Context context) {
        super(context);
        mContext = context;
    }

    public TitleScroolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void init(){
        mTextSize = mContext.getResources().getDimension(R.dimen.title_scrool_label_text_size);
        Debug.d("mTextSize = " + mTextSize);
        mColorNormal = mContext.getResources().getColor(R.color.title_scrool_text_color_normal);
        mColorPress = mContext.getResources().getColor(R.color.title_scrool_text_color_press);
        mParentView = View.inflate(mContext, R.layout.title_scroll_layout, null);
        mScroolView = (HorizontalScrollView)mParentView.findViewById(R.id.title_scroll_view);
        mContentLayout = (LinearLayout)mParentView.findViewById(R.id.content_layout);
        this.addView(mParentView);
    }

    public void setOnTitleClickListener(OnTitleClickListener otc)
    {
        mOnTitleClickListener = otc;
    }

    public void setTitleList(List<String> list)
    {
        mTitleList = list;
        updateTitle();
    }

    private void updateTitle()
    {
        if(mTitleList == null || mTitleList.size() <= 0)
        {
            return;
        }
        mTitleViewList.clear();
        int len = mTitleList.size();
        Debug.d("mTitleList.size() = " + mTitleList.size());
        for(int i = 0; i < len;i++)
        {
    		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    				LinearLayout.LayoutParams.WRAP_CONTENT,
    				LinearLayout.LayoutParams.WRAP_CONTENT);
            Debug.d("title = " + mTitleList.get(i) + " textsize = " + mTextSize);
            TextView textView = new TextView(mContext);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
            textView.setText(mTitleList.get(i));
//            textView.setMinWidth(textViewMinWidth);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(mColorNormal);
            textView.setId(i);
            textView.setTag(LABEL_STATUS_NORMAL);
            if(i != 0) {
            	params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.title_scrool_label_text_margin_left);
            } else {
            	params.leftMargin = 0;
            }
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if(mOnTitleClickListener != null)
                    {
                        for(int j = 0;j < mTitleViewList.size();j++)
                        {
                            TextView tempView = mTitleViewList.get(j);
                            if(tempView.getId() != id)
                            {
//                                tempView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
                                tempView.setTextColor(mColorNormal);
//                                if(((Integer)tempView.getTag()) == LABEL_STATUS_PRESS)
//                                {
//                                	Animation am;
//                                	am = new ScaleAnimation(1.2f,1.0f,1.2f,1.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                                    am.setFillAfter(true);
//                                    am.setDuration(ANIM_DURATION);
//                                    tempView.setAnimation(am);
//                                    tempView.setTag(LABEL_STATUS_NORMAL);
//                                }
                            }
                            else 
                            {
								Debug.d("textview x = " + tempView.getX()
										+ " mScroolView.getScrollX() = "
										+ mScroolView.getScrollX());
								if (tempView.getX() > mScroolView.getWidth() / 2) {
									int x = (int)(tempView.getX() - mScroolView.getWidth() / 2);
									scrollTo(x);
								}else {
									scrollTo(0);
								}
//                            	scrollTo(mScroolView.get)
                                mOnTitleClickListener.onClick(id);
//                                tempView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.title_scrool_label_text_press_size));
                                tempView.setTextColor(mColorPress);
//                                Animation am;
//                                am = new ScaleAnimation(1.0f,1.2f,1.0f,1.2f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                                am.setFillAfter(true);
//                                am.setDuration(ANIM_DURATION);
//                                tempView.setAnimation(am);
//                                tempView.setTag(LABEL_STATUS_PRESS);
                            }
                        }
                    }
                }
            });
            mContentLayout.addView(textView,params);
            mTitleViewList.add(textView);
        }
    }

    public interface OnTitleClickListener
    {
        public abstract void onClick(int id);
    }

    public void setTitlePressed(int id) {
            for (int j = 0; j < mTitleViewList.size(); j++) {
                TextView tempView = mTitleViewList.get(j);
                if (tempView.getId() != id) {
//                    tempView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
                    tempView.setTextColor(mColorNormal);
//                    if (((Integer) tempView.getTag()) == LABEL_STATUS_PRESS) {
//                        Animation am = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                        am.setFillAfter(true);
//                        am.setDuration(ANIM_DURATION);
//                        tempView.setAnimation(am);
//                        tempView.setTag(LABEL_STATUS_NORMAL);
//                    }
                } else {
//                    tempView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.title_scrool_label_text_press_size));
                    tempView.setTextColor(mColorPress);
                    if (tempView.getX() > mScroolView.getWidth() / 2) {
						int x = (int)(tempView.getX() - mScroolView.getWidth() / 2);
						scrollTo(x);
					}else {
						scrollTo(0);
					}
//                    Animation am = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    am.setFillAfter(true);
//                    am.setDuration(ANIM_DURATION);
//                    tempView.setAnimation(am);
//                    tempView.setTag(LABEL_STATUS_PRESS);
                }
            }
    }
    
    private void scrollTo(int x) {
    	mScroolX = x;
    	if(mScroolView != null) {
    		mHandler.post(runnable);
    	}
    }
    
    private Runnable runnable = new Runnable(){
    	public void run() {
    		mScroolView.smoothScrollTo(mScroolX, 0);
    	}
    	};

    	
}
