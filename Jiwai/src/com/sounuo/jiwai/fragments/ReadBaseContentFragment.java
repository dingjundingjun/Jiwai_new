package com.sounuo.jiwai.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sounuo.jiwai.R;
import com.sounuo.jiwai.adapter.CommentAdapter;
import com.sounuo.jiwai.data.ReadCatalogPojo;
import com.sounuo.jiwai.dialog.CommentDialog;
import com.sounuo.jiwai.utils.Debug;
import com.sounuo.jiwai.utils.PersonalUtil;
import com.sounuo.jiwai.utils.Util;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 2015/8/4.
 * email:dingjun0225@gmail.com
 *
 */
public class ReadBaseContentFragment extends Fragment implements View.OnClickListener
{
    private Activity mBaseActivity;
    private View mParentView;
    private WebView mWebView;
    private ReadCatalogPojo mCatalogPojo;
    private ProgressDialog mProgressDialog;
    private UMSocialService mSocialService;
    private List<UMComment> mComments;
//    private TextView mMoreComment;
    private TextView mLikeTextView;
    private TextView mShareTextView;
    private TextView mUserCommentNumber;
    private TextView mCommentTextView;
//    private ListView mCommentList;
//    private CommentAdapter mCommentAdapter;
    private CommentDialog mCommentDialog;
    private final int MORE_COMMENT_ID = 0;
    private int mLastCommentExpandNum = 0;
    private String mImgurl = "";
    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Util.Constants.DESCRIPTOR);
    public ReadBaseContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseActivity = getActivity();
        mParentView = LayoutInflater.from(mBaseActivity).inflate(R.layout.find_content_layout, null);
        init();
        return mParentView;
    }

    public void setCatalogPojo(ReadCatalogPojo cp) {
        mCatalogPojo = cp;
    }

    private View initCommentListHead()
    {
        View view = View.inflate(mBaseActivity,R.layout.comment_list_head,null);
        mWebView = (WebView)view.findViewById(R.id.webview);
        return view;
    }

    private TextView initCommentListFoot()
    {
        TextView view = new TextView(mBaseActivity);
        view.setId(MORE_COMMENT_ID);
        view.setBackgroundColor(Color.LTGRAY);
        view.setText(mBaseActivity.getString(R.string.more_comments));
        view.setTextSize(mBaseActivity.getResources().getDimension(R.dimen.more_comment_text_size));
        view.setGravity(Gravity.CENTER);
        return view;
    }

    private void init()
    {
//        mMoreComment = initCommentListFoot();
        mShareTextView = (TextView) mParentView.findViewById(R.id.share);
        mLikeTextView = (TextView)mParentView.findViewById(R.id.like);
        mCommentTextView = (TextView)mParentView.findViewById(R.id.comment);
        mWebView = (WebView)mParentView.findViewById(R.id.webview);
        mShareTextView.setOnClickListener(this);
        mLikeTextView.setOnClickListener(this);
        mCommentTextView.setOnClickListener(this);
//        mCommentList = (ListView)mParentView.findViewById(R.id.comment_list);
        mUserCommentNumber = (TextView)mParentView.findViewById(R.id.user_comment_number);
//        mCommentAdapter = new CommentAdapter(mBaseActivity);
//        mMoreComment.setOnClickListener(this);
//        mCommentList.addHeaderView(initCommentListHead());
//        mCommentList.addFooterView(mMoreComment);
//        mCommentList.setAdapter(mCommentAdapter);
//        initCommentListHead();
        initWebView();
        initProgressDialog();
        addQQQZonePlatform();
        addWXPlatform();
        initComment();
        loadData();
    }

    /**
     * 添加微信平台分享
     *
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wxa0a4dcc78756d00f";
        String appSecret = "29d7f92c73f1ffff42b2512c4f28b741";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, appSecret);
        wxHandler.setTitle(mCatalogPojo.getTitle());
        wxHandler.setTargetUrl(mCatalogPojo.getUrl());
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId, appSecret);
        wxCircleHandler.setTitle(mCatalogPojo.getTitle());
        wxCircleHandler.setTargetUrl(mCatalogPojo.getUrl());
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-微信。http://www.umeng.com/social");
        weixinContent.setTitle("友盟社会化分享组件-微信");
        weixinContent.setTargetUrl("http://www.umeng.com/social");
        //weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-朋友圈。http://www.umeng.com/social");
        circleMedia.setTitle("友盟社会化分享组件-朋友圈");
        // circleMedia.setShareMedia(urlImage);
        // circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl("http://www.umeng.com/social");
        mController.setShareMedia(circleMedia);
    }

    /**
     * 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     *
     */
    private void addQQQZonePlatform() {
        String appId = "1104234972";
        String appKey = "Sgvun7Ksq6ov6VOu";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId, appKey);
        qqSsoHandler.addToSocialSDK();
        qqSsoHandler.setTargetUrl(mCatalogPojo.getUrl());
        qqSsoHandler.setTitle(mCatalogPojo.getTitle());

        /*QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("来自带你看世界，掌握世界");
        qqShareContent.setTitle(mMessage.getTitle());
        qqShareContent.setTargetUrl(mMessage.getUrl());
        qqShareContent.setShareImage(new UMImage(mContext, "http://www.baidu.com/img/bdlogo.png"));
        mController.setShareMedia(qqShareContent);*/

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), appId, appKey);
        qZoneSsoHandler.setTargetUrl(mCatalogPojo.getUrl());
        qZoneSsoHandler.addToSocialSDK();

    }

    /**
     * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
     * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
     */
    private void postShare() {
//        CustomShareBoard shareBoard = new CustomShareBoard(getActivity());
//        shareBoard.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void getCommentFromUM(long sinceTime)
    {
        mSocialService.getComments(mBaseActivity, new SocializeListeners.FetchCommetsListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(int status, List<UMComment> comments, SocializeEntity arg2) {
                Debug.d("status111 = " + status);
                if (status == 200)    //ok
                {
                    Debug.d("comments size = " + comments.size());
                    if (!comments.isEmpty()) {
                        if (comments.size() > 0) {
                            mComments.addAll(comments);
                        } else if (comments.size() == 0) {
//                            mMoreComment.setVisibility(View.GONE);
                        }
                        updateComment(mComments);
                        mLastCommentExpandNum += comments.size();
                    } else {
//                        mMoreComment.setVisibility(View.GONE);
                        Toast.makeText(mBaseActivity, R.string.no_more_comment, Toast.LENGTH_SHORT).show();
                    }
                } else if (status == -104) {
                    getCommentFromUM(-1);
                }
            }
        }, sinceTime);
    }

    private void updateComment(List<UMComment> comments)
    {
        Debug.d("udpateComment comments.size = " + comments.size());
//        mCommentAdapter.setList(comments);
//        mCommentAdapter.notifyDataSetChanged();
    }

    private void initComment()
    {
//    	mCatalogPojo.setTitle("2016.02.22-你根本不了解的地球");
        mComments = new ArrayList<UMComment>();
        if (mSocialService == null) {
            Debug.d("getUMSocial name = " + "JJYY_" + mCatalogPojo.getTitle());
            mSocialService = UMServiceFactory.getUMSocialService("JJYY_" + mCatalogPojo.getTitle());
            PersonalUtil.login(mBaseActivity, mSocialService);
        }
        getCommentFromUM(-1);
        requestLike();
    }

    private void initProgressDialog()
    {
        mProgressDialog = new ProgressDialog(mBaseActivity);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }
    public void loadData()
    {
        if(mCatalogPojo == null)
        {
            return;
        }
        mWebView.loadUrl(mCatalogPojo.getUrl());
        mProgressDialog.show();
    }

    private void initWebView() {
        registerForContextMenu(mWebView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadWithOverviewMode(true);

        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        setWebClientListener();
    }

    void setWebClientListener()
    {
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                Debug.d("onPageFinished url = " + url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl)
            {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Debug.d("onProgressChanged  = " + newProgress);
                if (newProgress >= 40) {
                    mProgressDialog.dismiss();
                }
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    private void setLike(SocializeEntity entity) {
        String str = String.format(mBaseActivity.getResources().getString(R.string.like_show), entity.getLikeCount());
        Debug.d("setLike = " + entity.getLikeCount());
        mUserCommentNumber.setText(mBaseActivity.getString(R.string.user_comment_string) + "("+entity.getCommentCount() + ")");
        
        mLikeTextView.setText(str);
    }

    private void requestLike() {

//        UMSocialService controller = UMServiceFactory.getUMSocialService("JJYY_" + mCatalogPojo.getTitle());
        if (!mSocialService.getEntity().mInitialized) {
        	mSocialService.initEntity(mBaseActivity, new SocializeListeners.SocializeClientListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onComplete(int status, SocializeEntity entity) {
                    if(entity != null) {
                        setLike(entity);
                    }
                }
            });
        } else {
            setLike(mSocialService.getEntity());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.like:
            {
            	Debug.d("like mSocialService = " + mSocialService);
                mSocialService.likeChange(mBaseActivity,
                        new SocializeListeners.SocializeClientListener() {
                            @Override
                            public void onStart() {
                            	Debug.d("start likeChange");
                            }

                            @Override
                            public void onComplete(int status,
                                                   SocializeEntity entity) {
                                if (entity != null) {
                                	Debug.d("onComplete likeChange");
                                    requestLike();
                                }
                            }
                        });
                break;
            }
            case R.id.share:
            {
                postShare();
                break;
            }
            case MORE_COMMENT_ID:
            {
            	if(mComments != null && mComments.size() > 1)
            	{
            		getCommentFromUM(mComments.get(mComments.size() -1 ).mDt);
            	}
            	else
            	{
            		Toast.makeText(mBaseActivity, "没有评论了", Toast.LENGTH_SHORT).show();
            	}
                break;
            }
            case R.id.comment:
            {
                gotoComment();
                break;
            }
        }
    }

    public void gotoComment()
    {
        Debug.d("gotoCommentActvitiy");
        if(mCommentDialog == null)
        {
            mCommentDialog = new CommentDialog(mBaseActivity);
            mCommentDialog.setSocialService(mSocialService);
            mCommentDialog.setCatalogTitle("JJYY_" + mCatalogPojo.getTitle());
            mCommentDialog.setCommentListener(new CommentDialog.CommentListener() {
                @Override
                public void OnCommentComplete() {
                	mComments.clear();
                    getCommentFromUM(-1);
                }
            });
        }
        else
        {
            mCommentDialog.clear();
        }
        mCommentDialog.show();
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() 
		{
			public boolean onMenuItemClick(MenuItem item) 
			{
				if (item.getTitle() == "保存图片") 
				{
//					Util.downloadFile(mBaseActivity, mCatalogPojo.getTitle(), mImgurl);
				} 
				else 
				{
					return false;
				}
				
				return true;
			}
		};
		
		if (v instanceof WebView) 
		{
			WebView.HitTestResult result = ((WebView) v).getHitTestResult();
			if (result != null) 
			{
				int type = result.getType();
				if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) 
				{
					mImgurl = result.getExtra();	
					menu.setHeaderTitle("目录：/叽歪/");
					menu.add(0, v.getId(), 0, "保存图片").setOnMenuItemClickListener(handler);
				}
			}
		}
	}


    
}
