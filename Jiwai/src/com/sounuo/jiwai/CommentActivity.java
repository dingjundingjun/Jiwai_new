package com.sounuo.jiwai;

import java.util.ArrayList;
import java.util.List;

import com.sounuo.jiwai.adapter.CommentAdapter;
import com.sounuo.jiwai.dialog.CommentDialog;
import com.sounuo.jiwai.utils.Debug;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.Util;
import com.sounuo.jiwai.views.AutoListView;
import com.sounuo.jiwai.views.AutoListView.OnLoadListener;
import com.sounuo.jiwai.views.AutoListView.OnRefreshListener;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends Activity implements OnClickListener{
	private String mTitle;
	private AutoListView mCommentListView;
	private CommentAdapter mCommentAdapter;
	private UMSocialService mSocialService;
	private int mCommentCount;
	private List<UMComment> mComments;
	private Button mCommentBtn;
	private final int COMMENT_DEFAULT_COUNT = 10;
	private CommentDialog mCommentDialog;
	private TextView mNoCommentsTip;
	private ProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		init();
	}
	
	private void init() {
		mCommentListView = (AutoListView)findViewById(R.id.comment_list);
		mCommentBtn = (Button)findViewById(R.id.show_comment_dialog);
		mNoCommentsTip = (TextView)findViewById(R.id.no_comments_tip);
		mCommentBtn.setOnClickListener(this);
		mCommentListView.setTransitionEnable(false);
		mProgressDialog = new ProgressDialog(this);
		mCommentAdapter = new CommentAdapter(this);
		mCommentListView.setAdapter(mCommentAdapter);
		if(getIntent() != null) {
			mTitle = getIntent().getStringExtra("title");
			if(mTitle != null) {
				mComments = new ArrayList<UMComment>();
				if (mSocialService == null) {
					mProgressDialog.show();
			        Debug.d("getUMSocial name = " + "JJYY_" + mTitle);
			        mSocialService = UMServiceFactory.getUMSocialService("JJYY_" + mTitle);
			        getCommentFromUM(-1);
			    }
			}
		}
		
		mCommentListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				getCommentFromUMLast();
				mCommentListView.onRefreshComplete();
			}
		});
		
		mCommentListView.setOnLoadListener(new OnLoadListener() {
			@Override
			public void onLoad() {
				mCommentListView.onLoadComplete();
				if(mComments.size() == 0) {
					getCommentFromUM(-1);
				} else {
					getCommentFromUM(mComments.get(mComments.size() - 1).mDt);
				}
			}
		});
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
                            if(mComments.size() < COMMENT_DEFAULT_COUNT) {
                            	mCommentListView.noLoadDate();
                            } else {
                            	mCommentListView.hasLoadDate();
                            }
                            showNoComments(false);
                        }
                    } else {
                        Toast.makeText(CommentActivity.this, R.string.no_more_comment, Toast.LENGTH_SHORT).show();
                        mCommentListView.noLoadDate();
                        if(mComments.size() == 0) {
                        	showNoComments(true);
                        }
                    }
                } else if (status == -104) {
                    getCommentFromUM(-1);
                    mCommentCount = 0;
                }
            }
        }, sinceTime);
    }
	
	private void getCommentFromUMLast()
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
                        	if(mComments.get(0).mDt == comments.get(0).mDt) {
                        		Toast.makeText(CommentActivity.this, R.string.comment_no_new, Toast.LENGTH_SHORT).show();
                        		return;
                        	}
                        	for(int i = 0; i < comments.size();i++) {
                        		if(comments.get(i).mDt != mComments.get(0).mDt) {
                        			mComments.add(0, comments.get(i));
                        		}
                        	}
                            updateComment();
                        } 
                    } 
                } else if (status == -104) {
                    getCommentFromUM(-1);
                    mCommentCount = 0;
                }
            }
        }, -1);
    }
	
	private void updateComment() {
	    mCommentAdapter.setList(mComments);
	    mCommentAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.show_comment_dialog: {
			showCommentDialog();
			break;
		}
		}
	}
	
	private void showCommentDialog() {
		if(mCommentDialog == null)
        {
            mCommentDialog = new CommentDialog(this,R.style.comment_dialog_style);
            mCommentDialog.setSocialService(mSocialService);
            mCommentDialog.setCatalogTitle("JJYY_" + mTitle);
            mCommentDialog.setCommentListener(new CommentDialog.CommentListener() {
                @Override
                public void OnCommentComplete() {
                	mComments.clear();
                	getCommentFromUM(-1);
                }
            });
        }
        mCommentDialog.show();
	}
	
	private void showNoComments(boolean show) {
		mProgressDialog.dismiss();
		mCommentListView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
		mNoCommentsTip.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}
}
