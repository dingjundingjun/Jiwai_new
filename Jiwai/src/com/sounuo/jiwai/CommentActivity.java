package com.sounuo.jiwai;

import java.util.ArrayList;
import java.util.List;

import com.sounuo.jiwai.adapter.CommentAdapter;
import com.sounuo.jiwai.utils.Debug;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.views.AutoListView;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class CommentActivity extends Activity{
	private String mTitle;
	private AutoListView mCommentListView;
	private CommentAdapter mCommentAdapter;
	private UMSocialService mSocialService;
	private int mCommentCount;
	private List<UMComment> mComments;
	private final int COMMENT_DEFAULT_COUNT = 20;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		init();
	}
	
	private void init() {
		mCommentListView = (AutoListView)findViewById(R.id.comment_list);
		mCommentListView.setTransitionEnable(false);
		mCommentAdapter = new CommentAdapter(this);
		mCommentListView.setAdapter(mCommentAdapter);
		if(getIntent() != null) {
			mTitle = getIntent().getStringExtra("title");
			if(mTitle != null) {
				mComments = new ArrayList<UMComment>();
				if (mSocialService == null) {
			        Debug.d("getUMSocial name = " + "JJYY_" + mTitle);
			        mSocialService = UMServiceFactory.getUMSocialService("JJYY_" + mTitle);
			        getCommentFromUM(-1);
			    }
			}
		}
	}
	
	/**this method only get 10 comment while sinceTime = -1*/
	private void getCommentFromUM(long sinceTime)
    {
        mSocialService.getComments(this, new SocializeListeners.FetchCommetsListener() {
        	
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(int status, List<UMComment> comments, SocializeEntity se) {
                if (status == 200)    //ok
                {
                	mCommentCount = se.getCommentCount();
                    Debug.d("comments size = " + comments.size());
                    if (!comments.isEmpty()) {
                        if (comments.size() > 0) {
                            mComments.addAll(comments);
                            if(mComments.size() < COMMENT_DEFAULT_COUNT && mCommentCount >= 10) {
                            	getCommentFromUM(mComments.get(mComments.size() - 1).mDt);
                            }
                            updateComment();
                        } else if (comments.size() == 0) {
                        }
                    } else {
                        Toast.makeText(CommentActivity.this, R.string.no_more_comment, Toast.LENGTH_SHORT).show();
                    }
                } else if (status == -104) {
                    getCommentFromUM(-1);
                    mCommentCount = 0;
                }
            }
        }, sinceTime);
    }
	
	private void updateComment() {
	    mCommentAdapter.setList(mComments);
	    mCommentAdapter.notifyDataSetChanged();
	}
}
