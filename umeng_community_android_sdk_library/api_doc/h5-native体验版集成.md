#H5-Native体验版集成
##工程依赖
只需要使你的工程依赖我们的umeng_community_h5_library_project工程即可。
##集成方式
现在有三种集成方式可供开发者使用，他们分别是activity调用，fragment调用，webview调用
###直接使用h5的activity
在程序中首先需要设置appkey和appsecret，以及h5的网址和初始化sdk。

  	 Constants.UMENG_H5_HOST_URL = "http://umqa.test.wsq.umeng.com/";
                Constants.UMENG_APPKEY = "570607b667e58e94e9001210";
                Constants.UMENG_SECRET = "861993d8b89e7b839c17166a61e65671";
 	CommunitySDK sdk = CommunityFactory.getCommSDK(this);
        sdk.initSDK(this);                

然后在你需要的时候跳转到BrowserActivity即可打开h5界面。

 	public void onClick(View view) {
                             Intent intent = new Intent(HostActivity.this, BrowserActivity.class);
                startActivity(intent);
            }
特别注意：要在mainfest中加入` <activity android:name="com.umeng.h5.ui.activities.BrowserActivity" />`
###使用h5的fragment
你也可以在你的activity中直接使用BrowserFragment.
如果实现返回键屏蔽，可以调用如下代码：

	 @Override
    public void onBackPressed() {
        if (browserFragment.allowBackPressed()) {
            super.onBackPressed();
        }
    }
###只使用webview
首先需要初始化sdk

	 CommunitySDK sdk = CommunityFactory.getCommSDK(getActivity());
        sdk.initSDK(getActivity());          
然后设置webview和webviewclient：

	  mWebView = (UmengWebView) view.findViewById(ResFinder.getId("umeng_comm_webview"));
        mProgressBar = (ProgressBar) view.findViewById(ResFinder.getId("umeng_comm_progressbar"));
        mWebView.setFragment(this);
        mWebView.setProgressBar(mProgressBar);
        mWebView.loadUrl(Constants.UMENG_H5_HOST_URL);
        
注意mWebView.setFragment(this)必须设置mWebView的fragment或用setActivity设置activity。以及必须要实现以下代码

	 @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWebView.onActivityResult(requestCode, resultCode, data);
    }
	
至此，集成已经完毕，推荐用户直接使用activity的方式，更加简单明了。
##关于登录注入
h5界面支持开发者自己的账号系统和三方（微信qq等）登录。但是需要开发者自己实现登录。
首先需要实现接口AbsLoginImpl如下所示

	public class DefaultLoginImpl extends AbsLoginImpl {

    @Override
    protected void onLogin(Context context, final LoginListener listener) {
        // 包装一下Listener
        CommUser tempUser = new CommUser();
            Random r = new Random(10000);
            int i = r.nextInt();
            tempUser.name = "peissdsd"+i;

           tempUser.id = "000001@163.com";
            tempUser.iconUrl = "http://umeng.com/1.jpg";
            listener.onComplete(200,tempUser);	
            }

    @Override
    protected void onLogout(Context context, LoginListener listener) {
       Utils.removeCookie(context);
    }
	}
如果使用自己的账号系统，注意将tempUser.isSelfCount置成1
注意在登出的时候要调用Utils.removeCookie(context);进行cookie的清除

然后将实现注入：

  	 LoginSDKManager.getInstance().addAndUse(new DefaultLoginImpl());
##登录登出的调用：
用户想在打开网页之前用自己的账号系统登录，或者退出app时登出，可以调用如下方法：

	 public void login() {
            CommunitySDKImpl.getInstance().login(mContext, new LoginCallBack() {
                @Override
                public void onStart() {

                }

                @Override
                public void onComplete(int code) {
                    if (code == ErrorCode.NO_ERROR && !TextUtils.isEmpty(Constants.COOKIE)) {
                        
                    }
                }
            });
        }

 	CommunitySDKImpl.getInstance().logout(mContext, new 	LoginListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onComplete(int stCode, CommUser userInfo) {
                    
                }
            });

##特殊功能
如果不需要退出登录的按钮，可以设置

	Constants.IS_LOGOUTBUTTON ＝ false
如果不需要title，可以设置

    Constants.IS_TITLEBUTTON ＝ false
如果需要在首页加一个返回键，可以设置

	Constants.IS_HASTITLECALLBACK = true    
返回键的逻辑可以实现BackButtonCallBack，并在一开始的时候注入

	  BackButtonClickManager.getInstance().addAndUse(new BackButtonCallBack() {
                    @Override
                    public void onclick(Activity activity) {
                        
                    }
                });	
