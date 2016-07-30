package com.sounuo.jiwai.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sounuo.jiwai.R;
import com.sounuo.jiwai.utils.Debug;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.Util;
import com.umeng.socialize.bean.Gender;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;

/**
 * Created by Administrator on 2015/8/30.
 */
public class CommentDialog extends Dialog implements View.OnClickListener
{
    private EditText mCommentEdit;
    private Button mCommentBtn;
    private Button mCommentCancelBtn;
    private CommentListener mCommentlistener;
    private Context mContext;
    private UMSocialService mSocialService;
    private String mCommentStr;
    private ProgressDialog mCommentProgress;
    private String mLognTitle = "";
    public CommentDialog(Context context, int theme) {
        super(context, R.style.comment_dialog_style);
        mContext = context;
    }

    public CommentDialog(Context context) {
        super(context);
        mContext = context;
    }

    public void setSocialService(UMSocialService ums)
    {
        mSocialService = ums;
    }

    public void setCommentListener(CommentListener cl)
    {
        mCommentlistener = cl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.commnet_dialog_layout);
        init();
    }

    private void init()
    {
        mCommentEdit = (EditText)this.findViewById(R.id.comment_edit);
        this.setTitle(R.string.comment_dialog_title);
        mCommentBtn = (Button)this.findViewById(R.id.comment_btn);
        mCommentBtn.setOnClickListener(this);
        mCommentCancelBtn =(Button)findViewById(R.id.cancel_btn);
        mCommentCancelBtn.setOnClickListener(this);
    }

    public void clear()
    {
        mCommentEdit.setText("");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.comment_btn:
            {
                if(mCommentEdit.getText().toString() != null && !mCommentEdit.getText().toString().equals(""))
                {
                    mCommentStr = mCommentEdit.getText().toString();
                    postComment();
                }
                else
                {
                    Toast.makeText(mContext,R.string.comment_can_not_null,Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.cancel_btn: {
            	dismiss();
            	break;
            }
        }
    }

    public interface CommentListener
    {
        public void OnCommentComplete();
    }

    private void postComment() {

        final UMComment socializeComment = new UMComment();
        socializeComment.mUid = PersonalUtil.mSnsAccount.getUsid();
        socializeComment.mUname = PersonalUtil.mSnsAccount.getUserName();
//        if(PersonalUtil.mSnsAccount.getAccountIconUrl() != null)
//        {
//        	socializeComment.mUserIcon = PersonalUtil.mSnsAccount.getAccountIconUrl();
//        }
        socializeComment.mText = mCommentStr;
        Debug.d("mUid = " + socializeComment.mUid + " mUname = " + socializeComment.mUname);
        if (TextUtils.isEmpty(socializeComment.mText)) {

            Toast.makeText(mContext, "童鞋，你想说点啥？", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mCommentProgress == null)
        {
        	mCommentProgress = new ProgressDialog(mContext,AlertDialog.THEME_HOLO_LIGHT);
        	mCommentProgress.setMessage(mContext.getString(R.string.comment_sending));
        }
        mCommentProgress.show();
        mSocialService.login(mContext, PersonalUtil.mSnsAccount, new SocializeListeners.SocializeClientListener(){
            @Override
            public void onStart() {
            	Debug.d("mSocialService onStart");
            }
            @Override
            public void onComplete(int arg0, SocializeEntity arg1) {
            	Debug.d("mSocialService onComplete");
            	comment(socializeComment);
            }
        } );
    }

    private void comment(UMComment socializeComment)
    {
    	mSocialService.postComment(mContext, socializeComment,
                new SocializeListeners.MulStatusListener() {
                    @Override
                    public void onStart() {
                    	Debug.d("mSocialService postComment onStart");
                    }

                    @Override
                    public void onComplete(MultiStatus multiStatus, int status,
                                           SocializeEntity entity) {
                        if (status == 200) {
                            Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                            mCommentlistener.OnCommentComplete();
                            CommentDialog.this.dismiss();
                            clear();
                        } else {
                            Toast.makeText(mContext, "评论失败", Toast.LENGTH_SHORT).show();
                        }
                        mCommentProgress.dismiss();
                    }
                }, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE);
    }
    
	public void setCatalogTitle(String string) {
		mLognTitle = string;
	}

	@Override
	public void show() {
		super.show();
		Util.showSoftKeyboard(mContext, mCommentEdit);
	}
	
	
	
	
}
